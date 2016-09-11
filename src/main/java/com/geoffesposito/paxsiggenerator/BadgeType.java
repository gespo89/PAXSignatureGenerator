package com.geoffesposito.paxsiggenerator;

/**
 * Created by Geoff on 9/9/2016.
 */
public enum BadgeType {
    ATTENDEE("Attendee", 0),
    BYOC("BYOC", 1),
    OMEGANAUT("Omeganaut", 3),
    MEDIA("Media", 2),
    ENFORCER("Enforcer", 4);

    private final String displayName;
    private final int offSet;

    public String getDisplayName(){
        return this.displayName;
    }

    public String getName(){
        return this.name();
    }

    public int getOffset(){
        return this.offSet;
    }

    BadgeType(String displayName, int offset){
        this.displayName = displayName;
        this.offSet = offset;
    }
}
