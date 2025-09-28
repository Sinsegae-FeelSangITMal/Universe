package com.sinse.universe.enums;

public enum UserStatus {
    ACTIVE("활성"),       // enum 생성자
    INACTIVE("비활성");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
