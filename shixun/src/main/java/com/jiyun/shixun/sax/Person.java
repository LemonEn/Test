package com.jiyun.shixun.sax;

/**
 * Created by 张萌 on 2017/4/6.
 */
public class Person {
    private String  id;
    private String  title;
    private String  body;

    public Person() {

    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Person(String id, String title, String body) {

        this.id = id;
        this.title = title;
        this.body = body;
    }
}
