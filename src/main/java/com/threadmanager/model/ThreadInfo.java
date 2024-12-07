package com.threadmanager.model;

public class ThreadInfo {
    private final long id;
    private final String name;
    private final boolean active;
    private final int priority;
    private final ThreadType type;

    
    public enum ThreadType {
        SENDER, RECEIVER
    }

    public ThreadInfo(long id, String name, boolean active, int priority, ThreadType type) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.priority = priority;
        this.type = type;
    }
 

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
                ", name='" + name + '\'' +
                ", active=" + active +
                ", priority=" + priority +
                ", type=" + type +
                '}';
    }
} 