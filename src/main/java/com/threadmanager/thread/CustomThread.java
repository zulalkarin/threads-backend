package com.threadmanager.thread;

import com.threadmanager.model.ThreadInfo;
import com.threadmanager.model.ThreadInfo.ThreadType;
import com.threadmanager.service.QueueService;

public class CustomThread extends Thread {
    private final String threadName;
    private final ThreadType type;
    private final QueueService queueService;
    private volatile boolean running = true;
    private volatile boolean active = true;
    
    public CustomThread(String name, ThreadType type, QueueService queueService) {
        this.threadName = name;
        this.type = type;
        this.queueService = queueService;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                if (type == ThreadType.SENDER) {
                    String message = "Message from " + threadName + " at " + System.currentTimeMillis();
                    queueService.addMessage(message);
                    System.out.printf("%s sent: %s%n", threadName, message);
                } else {
                    String message = queueService.getMessage();
                    if (message != null) {
                        System.out.printf("%s received: %s%n", threadName, message);
                    }
                }   
                Thread.sleep(1000); // 1 saniye bekle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        active = false;
    }
    
    public void stopThread() {
        running = false;
    }
    
    public ThreadInfo getThreadInfo() {
       return new ThreadInfo(this.getId(), this.threadName, this.isActive(), this.getPriority(), this.type);
    }
    
    public boolean isActive() {
        return active;
    }
} 