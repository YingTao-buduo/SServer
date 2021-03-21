package com.yt.s_server.home;

public class Schedule {
    private String termid;    //学期id
    private String termStartWeek;   //学期开始周
    private String termEndWeek;   //学期结束周
    private String termCurrentWeek;    //学期持续周

    private String startWeek;   //开始周
    private String courseClassId;
    private String teacherName;  //教学老师
    private String totalSection;  //总课时
    private String startSection;   //开始节数
    private String teacherId;
    private String oddEven;  //0每周，1单周，2双周
    private String dayOfWeek;   //星期
    private String course;   //课程名字
    private String id;
    private String courseClassName;   //教学班名字
    private String endWeek;   //结束周
    private String place;   //上课地点
    private String courseItem;  //课程类型
    private String taskId;
    public String getTermid() {
        return termid;
    }
    public void setTermid(String termid) {
        this.termid = termid;
    }
    public String getTermStartWeek() {
        return termStartWeek;
    }
    public void setTermStartWeek(String termStartWeek) {
        this.termStartWeek = termStartWeek;
    }
    public String getTermEndWeek() {
        return termEndWeek;
    }
    public void setTermEndWeek(String termEndWeek) {
        this.termEndWeek = termEndWeek;
    }
    public String getTermCurrentWeek() {
        return termCurrentWeek;
    }
    public void setTermCurrentWeek(String termCurrentWeek) {
        this.termCurrentWeek = termCurrentWeek;
    }
    public String getStartWeek() {
        return startWeek;
    }
    public void setStartWeek(String startWeek) {
        this.startWeek = startWeek;
    }
    public String getCourseClassId() {
        return courseClassId;
    }
    public void setCourseClassId(String courseClassId) {
        this.courseClassId = courseClassId;
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
    public String getStartSection() {
        return startSection;
    }
    public void setStartSection(String startSection) {
        this.startSection = startSection;
    }
    public String getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    public String getOddEven() {
        return oddEven;
    }
    public void setOddEven(String oddEven) {
        this.oddEven = oddEven;
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCourseClassName() {
        return courseClassName;
    }
    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }
    public String getEndWeek() {
        return endWeek;
    }
    public void setEndWeek(String endWeek) {
        this.endWeek = endWeek;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getCourseItem() {
        return courseItem;
    }
    public void setCourseItem(String courseItem) {
        this.courseItem = courseItem;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "termid='" + termid + '\'' +
                ", termStartWeek='" + termStartWeek + '\'' +
                ", termEndWeek='" + termEndWeek + '\'' +
                ", termCurrentWeek='" + termCurrentWeek + '\'' +
                ", startWeek='" + startWeek + '\'' +
                ", courseClassId='" + courseClassId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", totalSection='" + totalSection + '\'' +
                ", startSection='" + startSection + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", oddEven='" + oddEven + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", course='" + course + '\'' +
                ", id='" + id + '\'' +
                ", courseClassName='" + courseClassName + '\'' +
                ", endWeek='" + endWeek + '\'' +
                ", place='" + place + '\'' +
                ", courseItem='" + courseItem + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}

