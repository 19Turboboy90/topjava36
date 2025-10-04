package ru.javawebinar.topjava.util;

public final class JspHelper {
    private JspHelper() {
    }

    public static String prefixPath(String jspName) {
        return "/WEB-INF/view/" + jspName + ".jsp";
    }
}