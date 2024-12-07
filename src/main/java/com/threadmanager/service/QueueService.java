package com.threadmanager.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class QueueService {
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    
    public void addMessage(String message) {
        messageQueue.offer(message);
    }
    
    public String getMessage() {
        return messageQueue.poll();
    }
    
    public int getQueueSize() {
        return messageQueue.size();
    }
} 