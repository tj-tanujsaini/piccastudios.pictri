package com.piccastudios.pictri.Genre;

import android.content.Context;

import java.util.List;

public class GenreListModel {

    private List<String> textGenre;
    private List<String> pictureGenre;
    private List<String> trueFalseGenre;
    private List<String> oddOneGenre;
    private String wbGenre;
    private String wbCategory;
    private String posterUrl;

    public List<String> getTextGenre() {
        return textGenre;
    }

    public void setTextGenre(List<String> textGenre) {
        this.textGenre = textGenre;
    }

    public List<String> getPictureGenre() {
        return pictureGenre;
    }

    public void setPictureGenre(List<String> pictureGenre) {
        this.pictureGenre = pictureGenre;
    }

    public List<String> getTrueFalseGenre() {
        return trueFalseGenre;
    }

    public void setTrueFalseGenre(List<String> trueFalseGenre) {
        this.trueFalseGenre = trueFalseGenre;
    }

    public List<String> getOddOneGenre() {
        return oddOneGenre;
    }

    public void setOddOneGenre(List<String> oddOneGenre) {
        this.oddOneGenre = oddOneGenre;
    }

    public String getWbGenre() {
        return wbGenre;
    }

    public void setWbGenre(String wbGenre) {
        this.wbGenre = wbGenre;
    }

    public String getWbCategory() {
        return wbCategory;
    }

    public void setWbCategory(String wbCategory) {
        this.wbCategory = wbCategory;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
