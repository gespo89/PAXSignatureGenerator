package com.geoffesposito.paxsiggenerator;

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
        List<Boolean> futures = new ArrayList<Boolean>(Collections.nCopies(years.size(), false));
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
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        ImageIO.write(generate(badges), "PNG", out);
        out.close();
    }

    private BufferedImage generate(List<Badge> badges) throws IOException {
        BufferedImage image = new BufferedImage((Constants.BADGE_WIDTH + 2)* badges.size(), Constants.BADGE_HEIGHT + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        int offset = 1;
        for(Badge badge: badges){
            BufferedImage badgeImage = badge.getImage();
            g.drawImage(badgeImage, offset, 0, null);
            offset += (Constants.BADGE_WIDTH + 2);
        }
        return image;
    }
}
