package com.geoffesposito.paxsiggenerator;

import java.awt.image.BufferedImage;

/**
 * Created by Geoff on 8/28/2016.
 */
public class Badge implements Comparable<Badge>{
    private final PAX pax;
    private final int year;
    private final BadgeType type;
    private final boolean future;

    public Badge(PAX pax, int year, BadgeType type, boolean future){
        this.pax = pax;
        this.year = year;
        this.type = type;
        this.future = future;
    }

    public BufferedImage getImage(){
        return this.pax.getBadge(year, future, type);
    }

    @Override
    public int compareTo(Badge other) {
        int val = this.year - other.year;
        if(val == 0){
            return this.pax.getSortOrder() - other.pax.getSortOrder();
        } else {
            return val;
        }
    }
}
