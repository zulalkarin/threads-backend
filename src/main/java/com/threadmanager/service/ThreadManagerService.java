package com.threadmanager.service;

import com.threadmanager.model.ThreadInfo;
import com.threadmanager.thread.CustomThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.threadmanager.model.ThreadInfo.ThreadType;

@Service
public class ThreadManagerService {
    private final Map<Long, CustomThread> activeThreads = new ConcurrentHashMap<>();
    private final QueueService queueService;
    private static final Logger logger = LoggerFactory.getLogger(ThreadManagerService.class);

    @Autowired
    public ThreadManagerService(QueueService queueService) {
        this.queueService = queueService;
    }

    public List<ThreadInfo> getAllThreadsInfo() {
        return activeThreads.values().stream()
                .map(CustomThread::getThreadInfo)
                .collect(Collectors.toList());
    }

    public List<ThreadInfo> getActiveThreadsInfo() {

        return activeThreads.values()
                .stream()
                .filter(CustomThread::isActive)
                .map(CustomThread::getThreadInfo)
                .collect(Collectors.toList());
    }

    public void createThreads(int senderCount, int receiverCount) {
        try {
            // create sender threads
            for (int i = 0; i < senderCount; i++) {
                CustomThread sender = new CustomThread(
                        ThreadType.SENDER,
                        queueService);
                activeThreads.put(sender.getId(), sender);
                sender.start();
                logger.info("Sender thread created with ID: " + sender.getId());
            }

            // create receiver threads
            for (int i = 0; i < receiverCount; i++) {
                CustomThread receiver = new CustomThread(
                        ThreadType.RECEIVER,
                        queueService);
                activeThreads.put(receiver.getId(), receiver);
                receiver.start();
                logger.info("Receiver thread created with ID: " + receiver.getId());
            }
        } catch (Exception e) {
            logger.error("Error creating threads: " + e);
            throw new RuntimeException("Thread creation failed", e);
        }
    }

    public void updateThreadStatus(Long threadId, boolean active) {
        CustomThread thread = activeThreads.get(threadId);
        if (thread != null) {
            if (active) {
                thread.setActive(true);
                synchronized (thread) {
                    thread.notify();
                }
                logger.info("Thread " + threadId + " resumed");
            } else {
                thread.setActive(false);
                logger.info("Thread " + threadId + " paused");
            }
        } else {
            logger.error("Thread " + threadId + " not found");
            throw new RuntimeException("Thread not found with ID: " + threadId);
        }
    }

    public void updateThreadPriority(Long threadId, int priority) {
        try {
            CustomThread thread = activeThreads.get(threadId);
            if (thread != null) {
                if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
                    throw new IllegalArgumentException("Invalid priority value");
                }
                thread.updatePriority(priority);
                logger.info("Thread " + threadId + " priority updated to " + priority);
            } else {
                logger.error("Thread " + threadId + " not found");
                throw new RuntimeException("Thread not found with ID: " + threadId);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid priority value for thread " + threadId + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating thread priority: " + e);
            throw new RuntimeException("Failed to update thread priority", e);
        }
    }

    public void deleteAllThreads() {

        try {
            activeThreads.values().forEach(thread -> thread.setActive(false));

            // interrupt threads
            for (CustomThread thread : activeThreads.values()) {
                thread.interrupt();
                try {
                    thread.join(1000);
                } catch (InterruptedException e) {
                    logger.warn("Thread interruption failed for thread: " + thread.getId());
                }
            }

            // clear threads
            activeThreads.clear();
            logger.info("All threads have been stopped and cleared successfully.");
        } catch (Exception e) {
            logger.error("Error while stopping threads: " + e.getMessage());
            throw new RuntimeException("Failed to stop all threads", e);
        }
    }

}