package com.yt.s_server.course;

public class CourseMenuItem {
    private String menuWrap;
    private String menuWrapUrl;

    public CourseMenuItem(String menuWrap, String menuWrapUrl) {
        this.menuWrap = menuWrap;
        this.menuWrapUrl = menuWrapUrl;
    }

    public String getMenuWrap() {
        return menuWrap;
    }

    public void setMenuWrap(String menuWrap) {
        this.menuWrap = menuWrap;
    }

    public String getMenuWrapUrl() {
        return menuWrapUrl;
    }

    public void setMenuWrapUrl(String menuWrapUrl) {
        this.menuWrapUrl = menuWrapUrl;
    }
}
