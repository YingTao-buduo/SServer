package com.yt.s_server.course;

public class Homework {
    private String title;
    private String ddl;
    private String score;
    private String creator;

    public Homework(String title, String ddl, String score, String creator) {
        this.title = title;
        this.ddl = ddl;
        this.score = score;
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDdl() {
        return "截止日期：" + ddl;
    }

    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

    public String getScore() {
        return "分数：" + score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreator() {
        return creator + "：";
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
