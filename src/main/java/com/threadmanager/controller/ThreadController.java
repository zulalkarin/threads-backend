package com.threadmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.threadmanager.service.ThreadManagerService;
import com.threadmanager.model.ThreadInfo;

@RestController
@RequestMapping("/api/threads")
@CrossOrigin(origins = "*")
public class ThreadController {
    private final ThreadManagerService threadManagerService;
    
    @Autowired
    public ThreadController(ThreadManagerService threadManagerService) {
        this.threadManagerService = threadManagerService;
    }
    
    // /api/threads/create?senderCount=5&receiverCount=3
    @PostMapping("/create")
    public ResponseEntity<String> createThreads(
        @RequestParam int senderCount,
        @RequestParam int receiverCount
    ) {
        threadManagerService.createThreads(senderCount, receiverCount);
        return ResponseEntity.ok("Threads created successfully");
    }
    
    // /api/threads/active
    @GetMapping("/active")
    public ResponseEntity<List<ThreadInfo>> getThreadsInfo() {
        return ResponseEntity.ok(threadManagerService.getAllThreadsInfo());
    }

    // /api/threads/{threadId}/stop
    @PutMapping("/{threadId}/stop")
    public ResponseEntity<String> stopThread(@PathVariable Long threadId) {
        threadManagerService.stopThread(threadId);
        return ResponseEntity.ok("Thread stopped successfully");
    }
    
    // /api/threads/{threadId}/priority?priority=5
    @PutMapping("/{threadId}/priority")
    public ResponseEntity<String> updatePriority(
        @PathVariable Long threadId,
        @RequestParam int priority
    ) {
        threadManagerService.updateThreadPriority(threadId, priority);
        return ResponseEntity.ok("Thread priority updated successfully");
    }
} 