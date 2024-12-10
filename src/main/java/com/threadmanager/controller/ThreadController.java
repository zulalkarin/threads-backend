package com.threadmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

import com.threadmanager.service.ThreadManagerService;
import com.threadmanager.model.ThreadInfo;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {
    private final ThreadManagerService threadManagerService;
    private final Logger logger = LoggerFactory.getLogger(ThreadController.class);
    
    @Autowired
    public ThreadController(ThreadManagerService threadManagerService) {
        this.threadManagerService = threadManagerService;
    }

    @Operation(
        summary = "Get all threads",
        description = "Returns all threads information"
    )
    @ApiResponse(responseCode = "200", description = "Threads fetched successfully")
    @GetMapping
    public ResponseEntity<List<ThreadInfo>> getAllThreads() {
        try {
            List<ThreadInfo> threads = threadManagerService.getAllThreadsInfo();
            return ResponseEntity.ok(threads);
        } catch (Exception e) {
            logger.error("Error while fetching threads: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    
    // /api/threads/create?senderCount=5&receiverCount=3
    @Operation(
        summary = "Create new threads",
        description = "Creates new sender and receiver threads"
    )
    @ApiResponse(responseCode = "200", description = "Threads created successfully")
    @PostMapping("/create")
    public ResponseEntity<String> createThreads(
        @RequestParam int senderCount,
        @RequestParam int receiverCount
    ) {
        try {
            threadManagerService.createThreads(senderCount, receiverCount);
            return ResponseEntity.ok("Threads created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Thread creation failed: " + e.getMessage());
        }
    }
    
    // /api/threads/active
    @Operation(
        summary = "Get active threads",
        description = "Returns all active threads information"
    )
    @ApiResponse(responseCode = "200", description = "Active threads fetched successfully")
    @GetMapping("/active")
    public ResponseEntity<List<ThreadInfo>> getActiveThreads() {
        try {
            List<ThreadInfo> threads = threadManagerService.getActiveThreadsInfo();
            return ResponseEntity.ok(threads);
        } catch (Exception e) {
            logger.error("Error while fetching active threads: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // /api/threads/{threadId}?active=true
    @Operation(
        summary = "Update thread status",
        description = "Updates the status of a specified thread"
    )
    @ApiResponse(responseCode = "200", description = "Thread status updated successfully")
    @PutMapping("/{threadId}/active")
    public ResponseEntity<String> updateThreadStatus(
            @PathVariable Long threadId,
            @RequestParam boolean active) {
        try {
            threadManagerService.updateThreadStatus(threadId, active);
            String status = active ? "resumed" : "paused";
            return ResponseEntity.ok("Thread " + status + " successfully");
        } catch (Exception e) {
            logger.error("Error while updating thread status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating thread status: " + e.getMessage());
        }
    }

    // /api/threads/{threadId}?priority=1
    @Operation(
        summary = "Update thread priority",
        description = "Updates the priority of a specified thread"
    )
    @ApiResponse(responseCode = "200", description = "Thread priority updated successfully")
    @PutMapping("/{threadId}/priority")
    public ResponseEntity<String> updatePriority(
        @PathVariable Long threadId,
        @RequestParam int priority
    ) {
        try {
            threadManagerService.updateThreadPriority(threadId, priority);
            return ResponseEntity.ok("Thread priority updated successfully");
        } catch (Exception e) {
            logger.error("Error while updating thread priority: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
        summary = "Delete all threads",
        description = "Deletes all threads"
    )
    @ApiResponse(responseCode = "200", description = "All threads deleted successfully")
    @DeleteMapping
    // /api/threads 
    public ResponseEntity<String> deleteAllThreads() {
        try {
            threadManagerService.deleteAllThreads();
            return ResponseEntity.ok("All threads deleted successfully");
        } catch (Exception e) {
            logger.error("Error while deleting threads: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting threads: " + e.getMessage());
        }
    }
} 