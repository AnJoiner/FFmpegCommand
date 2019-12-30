package com.coder.ffmpegtest.model;

/**
 * @author: AnJoiner
 * @datetime: 19-12-30
 */
public class CommandBean {
    private String name;
    private int id;

    public CommandBean(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
