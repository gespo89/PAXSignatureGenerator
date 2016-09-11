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
            if(this.year == 2013){
                return sort2013(other);
            } else {
                return this.pax.getSortOrder() - other.pax.getSortOrder();
            }
        } else {
            return val;
        }
    }

    private int sort2013(Badge other){
        //TODO: This is functional. But I don't like it.
        //Can we programmatically get PAX dates and use actual dates instead?
        if(this.pax == PAX.AUS && (other.pax == PAX.DEV || other.pax == PAX.WEST)){
            return -1;
        }
        if(other.pax == PAX.AUS && (this.pax == PAX.DEV || this.pax == PAX.WEST)){
            return 1;
        }
        return this.pax.getSortOrder() - other.pax.getSortOrder();
    }
}
