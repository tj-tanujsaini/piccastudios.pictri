package com.piccastudios.pictri.HomeScreens;

import com.piccastudios.pictri.R;

import java.util.List;

public class UserModel {

    private String userName;
    private String userId;
    private String UserPhoto;
    private String BadgeName = "Quiz Scout";
    private long totalQuestions = 0;
    private long totalCorrectAnswer = 0;
    private long levelsCompleted = 0;
    private long totalSignIn = 1;
    private long maxSignInStreak = 1;
    private long lastSignIn;
    private int appliedBadge = R.drawable.ach_scout;
    private long totalStars = 0;
    private boolean LifeLine1 = true;
    private boolean LifeLine2 = true;
    private boolean LifeLine3 = true;
    private boolean LifeLine4 = true;
    private long LLTS1;
    private long LLTS2;
    private long LLTS3;
    private long LLTS4;
    private List<String> quesDone;
    private boolean audioStatus = true;

    public List<String> getQuesDone() {
        return quesDone;
    }

    public void setQuesDone(List<String> quesDone) {
        this.quesDone = quesDone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getTotalCorrectAnswer() {
        return totalCorrectAnswer;
    }

    public void setTotalCorrectAnswer(long totalCorrectAnswer) {
        this.totalCorrectAnswer = totalCorrectAnswer;
    }

    public long getLevelsCompleted() {
        return levelsCompleted;
    }

    public void setLevelsCompleted(long levelsCompleted) {
        this.levelsCompleted = levelsCompleted;
    }

    public int getAppliedBadge() {
        return appliedBadge;
    }

    public void setAppliedBadge(int appliedBadge) {
        this.appliedBadge = appliedBadge;
    }

    public long getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(long totalStars) {
        this.totalStars = totalStars;
    }

    public String getBadgeName() {
        return BadgeName;
    }

    public void setBadgeName(String badgeName) {
        BadgeName = badgeName;
    }

    public boolean isLifeLine1() {
        return LifeLine1;
    }

    public void setLifeLine1(boolean lifeLine1) {
        LifeLine1 = lifeLine1;
    }

    public boolean isLifeLine2() {
        return LifeLine2;
    }

    public void setLifeLine2(boolean lifeLine2) {
        LifeLine2 = lifeLine2;
    }

    public boolean isLifeLine3() {
        return LifeLine3;
    }

    public void setLifeLine3(boolean lifeLine3) {
        LifeLine3 = lifeLine3;
    }

    public boolean isLifeLine4() {
        return LifeLine4;
    }

    public void setLifeLine4(boolean lifeLine4) {
        LifeLine4 = lifeLine4;
    }


    public long getLLTS1() {
        return LLTS1;
    }

    public void setLLTS1(long LLTS1) {
        this.LLTS1 = LLTS1;
    }

    public long getLLTS2() {
        return LLTS2;
    }

    public void setLLTS2(long LLTS2) {
        this.LLTS2 = LLTS2;
    }

    public long getLLTS3() {
        return LLTS3;
    }

    public void setLLTS3(long LLTS3) {
        this.LLTS3 = LLTS3;
    }

    public long getLLTS4() {
        return LLTS4;
    }

    public void setLLTS4(long LLTS4) {
        this.LLTS4 = LLTS4;
    }

    public long getTotalSignIn() {
        return totalSignIn;
    }

    public void setTotalSignIn(long totalSignIn) {
        this.totalSignIn = totalSignIn;
    }

    public long getLastSignIn() {
        return lastSignIn;
    }

    public void setLastSignIn(long lastSignIn) {
        this.lastSignIn = lastSignIn;
    }

    public long getMaxSignInStreak() {
        return maxSignInStreak;
    }

    public void setMaxSignInStreak(long maxSignInStreak) {
        this.maxSignInStreak = maxSignInStreak;
    }

    public boolean isAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(boolean audioStatus) {
        this.audioStatus = audioStatus;
    }
}
