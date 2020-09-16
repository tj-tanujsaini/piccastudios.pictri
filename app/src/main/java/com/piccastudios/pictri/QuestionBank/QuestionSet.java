package com.piccastudios.pictri.QuestionBank;

import android.content.Context;

import java.util.List;

public class QuestionSet {

    private String category;
    private String genre = "null";

    private List<QuesModelPic> quesModelPicList;
    private List<QuesModelMCQ> quesModelMCQList;
    private List<QuesModelOddOne> quesModelOddOneList;
    private List<QuesModelTrueFalse> quesModelTrueFalseList;

    private long points;
    private static QuestionSet instance;
    private boolean llUpdate1 = false;
    private boolean llUpdate2 = false;
    private boolean llUpdate3 = false;
    private boolean llUpdate4 = false;
    private int life = 2;

    private QuestionSet() {
    }

    public static QuestionSet getInstance(Context context) {
        if (instance == null) {
            instance = new QuestionSet();
        }
        return instance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<QuesModelPic> getQuesModelPicList() {
        return quesModelPicList;
    }

    public void setQuesModelPicList(List<QuesModelPic> quesModelPicList) {
        this.quesModelPicList = quesModelPicList;
    }

    public List<QuesModelMCQ> getQuesModelMCQList() {
        return quesModelMCQList;
    }

    public void setQuesModelMCQList(List<QuesModelMCQ> quesModelMCQList) {
        this.quesModelMCQList = quesModelMCQList;
    }

    public List<QuesModelOddOne> getQuesModelOddOneList() {
        return quesModelOddOneList;
    }

    public void setQuesModelOddOneList(List<QuesModelOddOne> quesModelOddOneList) {
        this.quesModelOddOneList = quesModelOddOneList;
    }

    public List<QuesModelTrueFalse> getQuesModelTrueFalseList() {
        return quesModelTrueFalseList;
    }

    public void setQuesModelTrueFalseList(List<QuesModelTrueFalse> quesModelTrueFalseList) {
        this.quesModelTrueFalseList = quesModelTrueFalseList;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public boolean isLlUpdate1() {
        return llUpdate1;
    }

    public void setLlUpdate1(boolean llUpdate1) {
        this.llUpdate1 = llUpdate1;
    }

    public boolean isLlUpdate2() {
        return llUpdate2;
    }

    public void setLlUpdate2(boolean llUpdate2) {
        this.llUpdate2 = llUpdate2;
    }

    public boolean isLlUpdate3() {
        return llUpdate3;
    }

    public void setLlUpdate3(boolean llUpdate3) {
        this.llUpdate3 = llUpdate3;
    }

    public boolean isLlUpdate4() {
        return llUpdate4;
    }

    public void setLlUpdate4(boolean llUpdate4) {
        this.llUpdate4 = llUpdate4;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
