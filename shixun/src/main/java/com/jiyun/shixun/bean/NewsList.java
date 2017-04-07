package com.jiyun.shixun.bean;

/**
 * Created by Administrator on 2017/4/5.
 */

public class NewsList {
    private String id;
    private String title;
    private String body;
    private String commentCount;
    private String author;
    private String authorid;
    private String pubDate;
    private String url;
    private String newstype;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorid() {
        return authorid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "NewsList{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", author='" + author + '\'' +
                ", authorid='" + authorid + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}
