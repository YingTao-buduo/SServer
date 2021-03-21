package com.yt.s_server.user;

public class Exam {
    private String num;
    private String college;
    private String id;
    private String course;
    private String startWeek;
    private String endWeek;
    private String time;
    private String classroom;
    private String seat;
    private String remarks;
    private String delay;
    private String teacher;
    
	public Exam(String num, String college, String id, String course, String startWeek, String endWeek,
				String time, String classroom, String seat, String remarks, String delay, String teacher) {
		super();
		this.num = num;
		this.college = college;
		this.id = id;
		this.course = course;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.time = time;
		this.classroom = classroom;
		this.seat = seat;
		this.remarks = remarks;
		this.delay = delay;
		this.teacher = teacher;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getStartWeek() {
		return startWeek;
	}
	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}
	public String getEndWeek() {
		return endWeek;
	}
	public void setEndWeek(String endWeek) {
		this.endWeek = endWeek;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	@Override
	public String toString() {
		return "Exam [num=" + num + ", college=" + college + ", id=" + id + ", course=" + course + ", startWeek="
				+ startWeek + ", endWeek=" + endWeek + ", time=" + time + ", classroom=" + classroom + ", seat=" + seat
				+ ", remarks=" + remarks + ", delay=" + delay + ", teacher=" + teacher + "]";
	}

    
}
