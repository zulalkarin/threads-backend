package com.threadmanager.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class QueueService {
    private static final int MAX_QUEUE_SIZE = 100;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
    public boolean addMessage(String message) {
        try {
            logger.info("Adding message to queue: " + message);
            return messageQueue.offer(message, 500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    public String getMessage() {
        try {
            logger.info("Getting message from queue");
            return messageQueue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    public int getQueueSize() {
        logger.info("Queue size now: " + messageQueue.size());
        return messageQueue.size();
    }
    
    public int getMaxQueueSize() {
        return MAX_QUEUE_SIZE;
    }
    
    public double getOccupancyRate() {
        return (double) messageQueue.size() / MAX_QUEUE_SIZE * 100;
    }
    
    public void clearQueue() {
        logger.info("Clearing all messages from queue");
        messageQueue.clear();
        logger.info("Queue cleared successfully. New size: " + messageQueue.size());
    }
} 