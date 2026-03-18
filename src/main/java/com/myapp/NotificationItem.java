package com.myapp;

public class NotificationItem {
    private final int id;
    private final String title;
    private final String message;
    private final boolean isRead;
    private final String createdAt;

    public NotificationItem(int id, String title, String message, boolean isRead, String createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}