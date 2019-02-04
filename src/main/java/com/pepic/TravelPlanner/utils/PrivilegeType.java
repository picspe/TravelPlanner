package com.pepic.TravelPlanner.utils;

public enum PrivilegeType {
    READ_TRAVEL("READ_TRAVEL"),
    WRITE_TRAVEL("WRITE_TRAVEL"),
    READ_ADMIN("READ_ADMIN"),
    WRITE_ADMIN("WRITE_ADMIN"),
    READ_MANAGER("READ_MANAGER"),
    WRITE_MANAGER("WRITE_MANAGER");


    private PrivilegeType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
