package com.threadmanager.thread;

import com.threadmanager.model.ThreadInfo;
import com.threadmanager.model.ThreadInfo.ThreadType;
import com.threadmanager.service.QueueService;

public class CustomThread extends Thread {
    private final ThreadType type;
    private final QueueService queueService;
    private volatile boolean active = true;
    private static final int FIXED_PRIORITY = 5;
    private static final long FIXED_FREQUENCY = 1000;

    
    public CustomThread(ThreadType type, QueueService queueService) {
        this.type = type;
        this.queueService = queueService;
        setPriority(FIXED_PRIORITY); // Varsayılan priority: 5
    }
    
    @Override
    public void run() {
        while (active) {
            try {
                if (type == ThreadType.SENDER) {
                    String message = "Message from Thread-" + getId();
                    queueService.addMessage(message);
                    System.out.println("Thread-" + getId() + " (Priority:" + getPriority() + ") sent message");
                } else {
                    String message = queueService.getMessage();
                    if (message != null) {
                        System.out.println("Thread-" + getId() + " (Priority:" + getPriority() + ") received message");
                    }
                }
                Thread.sleep(FIXED_FREQUENCY);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void updatePriority(int newPriority) {
        if (newPriority >= Thread.MIN_PRIORITY && newPriority <= Thread.MAX_PRIORITY) {
            super.setPriority(newPriority);
            System.out.println("Thread-" + getId() + " priority changed to " + newPriority);
        } else {
            throw new IllegalArgumentException("Invalid priority value");
        }
    }

    public ThreadInfo getThreadInfo() {
        return new ThreadInfo(
            this.getId(),
            "Thread-" + getId(),
            this.isActive(),
            this.getPriority(),
            this.type
        );
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
} 