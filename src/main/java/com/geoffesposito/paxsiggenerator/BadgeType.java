package com.geoffesposito.paxsiggenerator;

/**
 * Created by Geoff on 9/9/2016.
 */
public enum BadgeType {
    ATTENDEE("Attendee", 0),
    BYOC("BYOC", 1),
    MEDIA("Media", 2),
    CONTENT_CREATOR("Content Creator", 3),
    EXHIBITOR("Exhibitor", 4),
    VENDOR("Vendor", 5),
    SPEAKER("Speaker", 6),
    SPECIAL_GUEST("Special Guest", 7),
    OMEGANAUT("Omeganaut", 8),
    VIP("VIP", 9),
    Staff("Staff", 10),
    ENFORCER("Enforcer", 11);

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
