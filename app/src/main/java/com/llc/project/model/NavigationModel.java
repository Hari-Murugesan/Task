package com.llc.project.model;

/**
 * Created by Admin on 08-06-2017.
 */

public class NavigationModel {
    public int icon;
    public String name;

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

    // Constructor.
    public NavigationModel(String name , int icon) {

        this.name = name;
        this.icon = icon;
    }
}
