package at.nachrichten.newsapp.article;

import java.util.Date;

/**
 * Created by Harald on 17.01.2018.
 */

public class Article {
    private int id;
    private String date;
    private String header;
    private String data;
    private String categoryEng;
    private String categoryGer;
    private String isBookMarked;

    public Article(int id, String date, String header, String data, String categoryEng, String categoryGer, String isBookMarked) {
        this.id = id;
        this.date = date;
        this.header = header;
        this.data = data;
        this.categoryEng = categoryEng;
        this.categoryGer = categoryGer;
        this.isBookMarked = isBookMarked;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getHeader() {
        return header;
    }

    public String getData() {
        return data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCategoryEng(String categoryEng) {
        this.categoryEng = categoryEng;
    }

    public void setCategoryGer(String categoryGer) {
        this.categoryGer = categoryGer;
    }

    public String getCategoryEng() {
        return categoryEng;
    }

    public String getCategoryGer() {
        return categoryGer;
    }

    public String getIsBookMarked() {
        return isBookMarked;
    }

    public void setIsBookMarked(String isBookMarked) {
        this.isBookMarked = isBookMarked;
    }
}
