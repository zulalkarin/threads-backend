package com.threadmanager.model;

public class ThreadInfo {
    private final long id;
    private final ThreadType type;
    private final boolean active;
    private final int priority;

    public enum ThreadType {
        SENDER, RECEIVER
    }

    public ThreadInfo(long id, String name, boolean active, int priority, ThreadType type) {
        this.id = id;
        this.active = active;
        this.priority = priority;
        this.type = type;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    public boolean isActive() {
        return active;
    }

    public int getPriority() {
        return priority;
    }

    public ThreadType getType() {
        return type;
    }

    
    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", type=" + type +
                ", active=" + active +
                ", priority=" + priority +
                '}';
    }
} 