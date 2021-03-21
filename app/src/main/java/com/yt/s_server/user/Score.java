package com.yt.s_server.user;

public class Score {
	private String year;
	private String term;
	private String id;
	private String name;
	private String type_1;
	private String type;
	private int credit;
	private int mark;
	private float gp;
	private String type_2;
	
	
	
	public Score(String year, String term, String id, String name, String type_1, String type, int credit, int mark,
			float gp, String type_2) {
		super();
		this.year = year;
		this.term = term;
		this.id = id;
		this.name = name;
		this.type_1 = type_1;
		this.type = type;
		this.credit = credit;
		this.mark = mark;
		this.gp = gp;
		this.type_2 = type_2;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType_1() {
		return type_1;
	}
	public void setType_1(String type_1) {
		this.type_1 = type_1;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public float getGp() {
		return gp;
	}
	public void setGp(float gpa) {
		this.gp = gp;
	}
	public String getType_2() {
		return type_2;
	}
	public void setType_2(String type_2) {
		this.type_2 = type_2;
	}

	@Override
	public String toString() {
		return "Score{" +
				"year='" + year + '\'' +
				", term='" + term + '\'' +
				", id='" + id + '\'' +
				", name='" + name + '\'' +
				", type_1='" + type_1 + '\'' +
				", type='" + type + '\'' +
				", credit=" + credit +
				", mark=" + mark +
				", gp=" + gp +
				", type_2='" + type_2 + '\'' +
				'}';
	}
}
