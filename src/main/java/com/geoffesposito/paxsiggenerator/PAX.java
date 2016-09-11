package com.geoffesposito.paxsiggenerator;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geoff on 8/28/2016.
 */
public enum PAX implements Comparable<PAX>{
    WEST("PAX West", "/images/PAX_WEST.png", 2004, 3),
    EAST("PAX East", "/images/PAX_EAST.png", 2010, 1),
    AUS("PAX Aus", "/images/PAX_AUS.png", 2013, 4),
    SOUTH("PAX South", "/images/PAX_SOUTH.png", 2015, 0),
    DEV("PAX Dev", "/images/PAX_DEV.png", 2011, 2);

    private final String displayName;
    private final BufferedImage image;
    private final int startYear;
    private final int sortOrder;

    PAX(String displayName, String image, int startYear, int sortOrder) {
        this.displayName = displayName;
        this.image = readImage(image);
        this.startYear = startYear;
        this.sortOrder = sortOrder;
    }

    private static BufferedImage readImage(String path){
            try {
                return ImageIO.read(PAX.class.getResourceAsStream(path));
            } catch (IOException e) {
                return null;
            }
    }

    public String getName(){
        return this.name();
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public int getSortOrder(){
        return this.sortOrder;
    }

    public List<Integer> getYears(){
        ArrayList<Integer> years = new ArrayList<>();
        int last = Year.now().getValue() + 1;
        for(int i = this.startYear; i <= last; i++){
            years.add(i);
        }
        return years;
    }

    public BufferedImage getBadge(int year, boolean future, BadgeType badgeType){
        int y = future ? Constants.BADGE_HEIGHT : 0;
        int x = badgeType.ordinal() * Constants.BADGE_WIDTH;
        BufferedImage badge = new BufferedImage(Constants.BADGE_WIDTH, Constants.BADGE_HEIGHT,  BufferedImage.TYPE_INT_ARGB);
        badge.getGraphics().drawImage(image.getSubimage(x, y, Constants.BADGE_WIDTH, Constants.BADGE_HEIGHT), 0, 0, null);
        drawNumber(badge, future, year, badgeType);
        return badge;
    }

    private void drawNumber(BufferedImage badge, boolean future, int year, BadgeType badgeType){
        year = year % 100;
        int onesDigit = year % 10;
        int tensDigit = year / 10;
        int numWidth = Constants.NUM_WIDTHS[onesDigit] + Constants.NUM_WIDTHS[tensDigit] + 1;
        int textOffsetX = (Constants.BADGE_WIDTH - numWidth) / 2 ;
        int textOffsetY = badgeType == BadgeType.ATTENDEE ? Constants.TEXT_OFFSET_Y : Constants.SPECIAL_TEXT_OFFSET_Y;

        boolean color = badgeType == BadgeType.ENFORCER || future;
        Graphics g = badge.getGraphics();
        drawDigit(g, tensDigit, color, textOffsetX, textOffsetY);
        drawDigit(g, onesDigit, color, textOffsetX + Constants.NUM_WIDTHS[tensDigit] + 1, textOffsetY);
    }

    private void drawDigit(Graphics g, int digit, boolean color, int x, int y){
        int digitOffsetX = BadgeType.values().length * Constants.BADGE_WIDTH;
        for(int i = digit - digit % 5; i < digit; i++) digitOffsetX += Constants.NUM_WIDTHS[i];
        int digitOffsetY = digit < 5 ? 0 : Constants.NUM_HEIGHT;
        if(!color ) digitOffsetY += (Constants.NUM_HEIGHT * 2);
        BufferedImage digitImg = image.getSubimage(digitOffsetX, digitOffsetY, Constants.NUM_WIDTHS[digit], Constants.NUM_HEIGHT);
        g.drawImage(digitImg, x, y, null);
    }
}
