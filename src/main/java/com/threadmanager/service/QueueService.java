package com.threadmanager.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class QueueService {
    private static final int MAX_QUEUE_SIZE = 100;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    
    public boolean addMessage(String message) {
        try {
            return messageQueue.offer(message, 500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    public String getMessage() {
        try {
            return messageQueue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    public int getQueueSize() {
        System.out.println("QUEUE SIZE-------->>>>: " + messageQueue.size());
        return messageQueue.size();
    }
    
    public int getMaxQueueSize() {
        return MAX_QUEUE_SIZE;
    }
    
    public double getOccupancyRate() {
        return (double) messageQueue.size() / MAX_QUEUE_SIZE * 100;
    }
} 