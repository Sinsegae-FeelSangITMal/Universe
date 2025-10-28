package com.sinse.universe.enums;

public enum StreamStatus {
    WAITING("대기 중"),
    LIVE("진행 중"),
    ENDED("종료됨");

    private final String description;

    StreamStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
