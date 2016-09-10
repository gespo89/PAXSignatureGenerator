package com.geoffesposito.paxsiggenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Geoff on 8/28/2016.
 */
public enum PAX {
    SOUTH("/images/PAX_SOUTH.png"),
    EAST("/images/PAX_EAST.png"),
    WEST("/images/PAX_WEST.png"),
    AUS("/images/PAX_AUS.png"),
    DEV("/images/PAX_DEV.png");

    private BufferedImage image;

    PAX(String image){
        try {
            this.image = ImageIO.read(PAX.class.getResourceAsStream(image));
        } catch (IOException e) {

        }

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
