package com.example.server.controller;

import com.example.server.dto.TaskDto;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.service.TaskService;
import com.example.server.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("task")
@CrossOrigin
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("create")
    public ResponseEntity<ResponseBase> createTask(
            @RequestBody TaskDto taskDto,
            @RequestParam(required = false) String existingTeamId
    ){
        return taskService.createTask(taskDto,existingTeamId);
    }

    @GetMapping("all")
    public ResponseEntity<List<Task>> getTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("mine")
    public ResponseEntity<List<Task>> getMyTasks(
            @RequestParam(required = false) String staffId
    ) {
        return taskService.getMyTasks(staffId);
    }

    @GetMapping("get/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable String taskId){
        return taskService.getTaskById(taskId);
    }

    @DeleteMapping("del/{taskId}")
    public ResponseEntity<ResponseBase> deleteTask(@PathVariable String taskId){
        return taskService.deleteTask(taskId);
    }

    @PostMapping("assign")
    public ResponseEntity<ResponseBase> assignTeamToTask(
            @RequestParam String taskId,
            @RequestParam String teamId
    ){
        return taskService.assignTeam(taskId, teamId);
    }

    @PutMapping("module/progress")
    public ResponseEntity<ResponseBase> updateModuleProgress(
            @RequestParam String taskId,
            @RequestParam String moduleName,
            @RequestParam Task.ProgressStatus progressStatus,
            @RequestParam("refs") MultipartFile[] referencePics,
            @RequestParam String comment
    ){
        return taskService.updateModuleProgress(taskId, moduleName, progressStatus, referencePics, comment);
    }

    @PutMapping("updateProgress")
    public ResponseEntity<ResponseBase> updateTaskProgress(
            @RequestParam String taskId,
            @RequestParam Task.ProgressStatus progressStatus
    ){
        return taskService.updateTaskCompletionOrApproval(taskId, progressStatus);
    }

    @PutMapping("approve")
    public ResponseEntity<ResponseBase> approveTaskProgress(
            @RequestParam String taskId
    ){
        return taskService.updateTaskCompletionOrApproval(taskId, Task.ProgressStatus.APPROVED);
    }

    @PutMapping("appr-rej-module")
    public ResponseEntity<ResponseBase> approveOrRejectModuleProgress(
            @RequestParam String taskId,
            @RequestParam String progressInfoId,
            @RequestParam Task.ProgressStatus progressStatus
    ){
        return taskService.approveOrRejectModule(taskId,progressInfoId, progressStatus);
    }

    @GetMapping("in-review")
    public ResponseEntity<List<Task>> getAllTasksInReview(){
        return taskService.getAllTasksInReview();
    }

    @PostMapping("approve-task")
    public ResponseEntity<ResponseBase> AdminApproveTask(
            @RequestParam String taskId
    ){
        return taskService.adminApprove(taskId);
    }

}
