package com.yt.s_server.home;

import androidx.annotation.NonNull;

public class ApplyCourse {
    private String course;
    private String courseClassId;
    private String courseClassName;
    private String courseItem;
    private String dayOfWeek;
    private String endWeek;
    private String id;
    private String oddEven;
    private String place;
    private String startSection;
    private String startWeek;
    private String taskId;
    private String teacherId;
    private String teacherName;
    private String totalSection;
    private Boolean checked = false;

    public ApplyCourse(String course, String courseClassId, String courseClassName, String courseItem, String dayOfWeek, String endWeek, String id, String oddEven, String place, String startSection, String startWeek, String taskId, String teacherId, String teacherName, String totalSection) {
        this.course = course;
        this.courseClassId = courseClassId;
        this.courseClassName = courseClassName;
        this.courseItem = courseItem;
        this.dayOfWeek = dayOfWeek;
        this.endWeek = endWeek;
        this.id = id;
        this.oddEven = oddEven;
        this.place = place;
        this.startSection = startSection;
        this.startWeek = startWeek;
        this.taskId = taskId;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.totalSection = totalSection;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(String courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public String getCourseItem() {
        return courseItem;
    }

    public void setCourseItem(String courseItem) {
        this.courseItem = courseItem;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(String endWeek) {
        this.endWeek = endWeek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOddEven() {
        return oddEven;
    }

    public void setOddEven(String oddEven) {
        this.oddEven = oddEven;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartSection() {
        return startSection;
    }

    public void setStartSection(String startSection) {
        this.startSection = startSection;
    }

    public String getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(String startWeek) {
        this.startWeek = startWeek;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTotalSection() {
        return totalSection;
    }

    public void setTotalSection(String totalSection) {
        this.totalSection = totalSection;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "ApplyCourse{" +
                "course='" + course + '\'' +
                ", courseClassId='" + courseClassId + '\'' +
                ", courseClassName='" + courseClassName + '\'' +
                ", courseItem='" + courseItem + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", endWeek='" + endWeek + '\'' +
                ", id='" + id + '\'' +
                ", oddEven='" + oddEven + '\'' +
                ", place='" + place + '\'' +
                ", startSection='" + startSection + '\'' +
                ", startWeek='" + startWeek + '\'' +
                ", taskId='" + taskId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", totalSection='" + totalSection + '\'' +
                ", checked=" + checked +
                '}';
    }
}
