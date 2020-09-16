package com.piccastudios.pictri.UserSection;

public class BadgeModel {

    private String badgeName;
    private String badgeSubText;
    private long totalLevels;
    private int badgeIcon;
    private long totalSignIn;

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getBadgeSubText() {
        return badgeSubText;
    }

    public void setBadgeSubText(String badgeSubText) {
        this.badgeSubText = badgeSubText;
    }

    public long getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(long totalLevels) {
        this.totalLevels = totalLevels;
    }

    public int getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(int badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    public long getTotalSignIn() {
        return totalSignIn;
    }

    public void setTotalSignIn(long totalSignIn) {
        this.totalSignIn = totalSignIn;
    }
}
