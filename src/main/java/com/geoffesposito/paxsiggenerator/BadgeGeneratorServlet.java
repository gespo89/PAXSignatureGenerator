package com.geoffesposito.paxsiggenerator;

import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Geoff on 9/9/2016.
 */
@WebServlet(urlPatterns = "/signature.png")
public class BadgeGeneratorServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Integer> years = Arrays.stream(request.getParameterValues("Year[]")).map(Integer::parseInt).collect(Collectors.toList());
        List<PAX> paxen = Arrays.stream(request.getParameterValues("PAX[]")).map(PAX::valueOf).collect(Collectors.toList());
        List<BadgeType> badgeTypes = Arrays.stream(request.getParameterValues("Badge[]")).map(BadgeType::valueOf).collect(Collectors.toList());
        List<Boolean> futures = new ArrayList<>(Collections.nCopies(years.size(), false));
        int wrap = getWrap(request);

        String[] futureValues = request.getParameterValues("future[]");
        if(futureValues != null){
            for (String s: futureValues){
                futures.set(Integer.parseInt(s), true);
            }
        }

        List<Badge> badges = new ArrayList<>(years.size());
        for(int i = 0; i < years.size(); i++){
            Badge badge = new Badge(paxen.get(i), years.get(i), badgeTypes.get(i), futures.get(i));
            badges.add(badge);
        }
        Collections.sort(badges);

        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        ImageIO.write(generate(badges, wrap), "PNG", out);
        out.close();
    }

    private int getWrap(HttpServletRequest request){
        Integer wrap = null;
        String wrapString = request.getParameter("wrap");
        if(wrapString != null){
            wrap = Ints.tryParse(wrapString);
        }
        return wrap != null ? wrap : Integer.MAX_VALUE;
    }

    private BufferedImage generate(List<Badge> badges, int wrap) throws IOException {
        int offsetWidth = Constants.BADGE_WIDTH + 2;
        int offsetHeight = Constants.BADGE_HEIGHT + 2;
        int rows = (int)Math.ceil((double)badges.size() / (double)wrap);
        BufferedImage image = new BufferedImage(offsetWidth* badges.size(), rows * offsetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        if(wrap > 0){
            for(int i = 0; i < badges.size(); i++){
                BufferedImage badgeImage = badges.get(i).getImage();
                g.drawImage(badgeImage, offsetWidth * (i % wrap), offsetHeight * (i / wrap), null);
            }
        }

        return image;
    }
}
