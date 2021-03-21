package com.yt.s_server.course;

public class Course {
    private String name;    // 课程名
    private String teacher; // 授课教师
    private String number;      // 课程编号
    private String coverUrl;// 课程图片
    private String id;     // 课程链接

    private boolean notice = false;
    private boolean homework = false;
    private boolean test = false;
    private boolean questionnaire = false;

    public Course(String name, String teacher, String number, String coverUrl, String id) {
        this.name = name;
        this.teacher = teacher;
        this.number = number;
        this.coverUrl = coverUrl;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNotice() {
        return notice;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    public boolean isHomework() {
        return homework;
    }

    public void setHomework(boolean homework) {
        this.homework = homework;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean isQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(boolean questionnaire) {
        this.questionnaire = questionnaire;
    }
}
