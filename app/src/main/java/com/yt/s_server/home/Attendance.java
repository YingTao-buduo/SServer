package com.yt.s_server.home;

public class Attendance {
    private int startSection;
    private String studentLeaveFormId;
    private String week;
    private String courseItem;
    private String teacher;
    private String course;
    private String freeListenFormId;
    private String type;//[1 旷课][2 迟到][3 早退][4 调课]
    private int totalSection;
    private String version;
    private String dayOfWeek;

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public String getStudentLeaveFormId() {
        return studentLeaveFormId;
    }

    public void setStudentLeaveFormId(String studentLeaveFormId) {
        this.studentLeaveFormId = studentLeaveFormId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCourseItem() {
        return courseItem;
    }

    public void setCourseItem(String courseItem) {
        this.courseItem = courseItem;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFreeListenFormId() {
        return freeListenFormId;
    }

    public void setFreeListenFormId(String freeListenFormId) {
        this.freeListenFormId = freeListenFormId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalSection() {
        return totalSection;
    }

    public void setTotalSection(int totalSection) {
        this.totalSection = totalSection;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
