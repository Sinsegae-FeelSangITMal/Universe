package com.sinse.universe.enums;

public enum UserStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    INCOMPLETE("추가 정보 필요");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
