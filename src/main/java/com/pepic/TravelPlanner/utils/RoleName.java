package com.pepic.TravelPlanner.utils;

public enum RoleName {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER");

    private RoleName(String name)
    {
        this.name = name;
    }

    private String name;

    private String getName(){
        return this.name;
    }
}
