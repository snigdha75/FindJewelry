package com.example.findjewelry.models;

public class category {
    private String name;
    private int icon;
    private String color,brief;
    private int id;

    public category(String name, int icon, String color, String brief, int id) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.brief = brief;
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
