package com.threadmanager.service;

import com.threadmanager.model.ThreadInfo;
import com.threadmanager.thread.CustomThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.threadmanager.model.ThreadInfo.ThreadType;

@Service
public class ThreadManagerService {
    private final Map<Long, CustomThread> activeThreads = new ConcurrentHashMap<>();
    private final QueueService queueService;

    @Autowired
    public ThreadManagerService(QueueService queueService) {
        this.queueService = queueService;
    }

    public List<ThreadInfo> getAllThreadsInfo() {
        System.out.println("All threads info serviceeeeeee: " + activeThreads.size());
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

            System.out.println("Creating threads");
            // create sender threads
            for (int i = 0; i < senderCount; i++) {
                CustomThread sender = new CustomThread(
                        ThreadType.SENDER,
                        queueService);
                activeThreads.put(sender.getId(), sender);
                sender.start();
                System.out.println("Sender thread created with ID: " + sender.getId());
            }

            // create receiver threads
            for (int i = 0; i < receiverCount; i++) {
                CustomThread receiver = new CustomThread(
                        ThreadType.RECEIVER,
                        queueService);
                activeThreads.put(receiver.getId(), receiver);
                receiver.start();
                System.out.println("Receiver thread created with ID: " + receiver.getId());
            }
        } catch (Exception e) {
            System.out.println("Error creating threads: " + e);
            throw new RuntimeException("Thread creation failed", e);
        }
    }

    public void updateThreadStatus(Long threadId, boolean active) {
        CustomThread thread = activeThreads.get(threadId);
        if (thread != null) {
            if (active) {
                thread.setActive(true);
                System.out.println("Thread " + threadId + " resumed");
            } else {
                thread.setActive(false);
                System.out.println("Thread " + threadId + " paused");
            }
        } else {
            System.out.println("Thread " + threadId + " not found");
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
                thread.setPriority(priority);
                System.out.println("Thread " + threadId + " priority updated to " + priority);
            } else {
                System.out.println("Thread " + threadId + " not found");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority value for thread " + threadId + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error updating thread priority: " + e);
            throw new RuntimeException("Failed to update thread priority", e);
        }
    }

    
}