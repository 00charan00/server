package com.example.server.service;

import com.example.server.dto.TaskDto;
import com.example.server.dto.TeamDto;
import com.example.server.entity.*;
import com.example.server.exception.InvalidDataException;
import com.example.server.repository.ImageDetailRepository;
import com.example.server.repository.ProgressInfoRepository;
import com.example.server.repository.TaskRepository;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TeamService teamService;
    @Autowired
    ProgressInfoRepository progressInfoRepository;
    @Autowired
    ImageDetailRepository imageDetailRepository;
    @Autowired
    StaffService staffService;

    public ResponseEntity<ResponseBase> createTask(TaskDto taskDto, String existingTeamId) {

        Task task = new Task();
        task.setTaskTittle(taskDto.getTaskTittle());
        task.setTaskDescription(taskDto.getTaskDescription());
        task.setTaskId("Task" + UUID.randomUUID());
        task.setTaskProgress(Task.ProgressStatus.INITIATED);
        task.setModules(taskDto.getModules());
        task.setDeadline(taskDto.getDeadline());

        List<String> modules = taskDto.getModules();
        if (modules != null) {
            Map<String, Task.ProgressStatus> moduleLevelProgress = modules.stream().collect(Collectors.toMap(
                    Function.identity(),
                    m -> Task.ProgressStatus.INITIATED
            ));
            task.setModuleLevelProgress(moduleLevelProgress);
        }
        List<String> teamMembers = taskDto.getTeamMembers();
        String teamLeader = taskDto.getTeamLeader();
        if(existingTeamId != null){
            Team team = teamService.getTeamById(existingTeamId);
            task.setTeam(team);
            task.setTeamAssigned(true);
            staffService.sendNotificationsToStaffs(
                    team.getTeamMembers().stream().map(Staff::getStaffId).collect(Collectors.toList()),
                    task.getTaskDescription(),
                    "Task Assigned : " + task.getTaskTittle()
            );
        }else if (teamMembers != null && teamLeader != null) {
            Team team = teamService.formTeam(new TeamDto(taskDto.getTeamName(), teamMembers, teamLeader));
            task.setTeam(team);
            task.setTeamAssigned(true);
            staffService.sendNotificationsToStaffs(
                    teamMembers,
                    task.getTaskDescription(),
                    "Task Assigned : " + task.getTaskTittle()
            );
        } else task.setTeamAssigned(false);

        return ResponseEntity.ok(new ResponseBase(taskRepository.save(task).getTaskId(), true));

    }

    public ResponseEntity<ResponseBase> assignTeam(String taskId, String teamId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("No such Task found"));
        Team team = teamService.getTeamById(teamId);
        task.setTeam(team);
        task.setTeamAssigned(true);
        taskRepository.save(task);
        staffService.sendNotificationsToStaffs(
                team.getTeamMembers().stream().map(Staff::getStaffId).toList(),
                task.getTaskDescription(),
                "Task Assigned : " + task.getTaskTittle()
        );
        return ResponseEntity.ok(new ResponseBase("Assigned", true));
    }

    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasksAll = taskRepository.findAll();
        tasksAll.forEach(t -> {
            Set<ProgressInfo> piList = t.getProgressDetails();
            piList.forEach(p -> {
                List<ImageDetail> idetList = p.getReferences();
                idetList.forEach(i -> {
                    try {
                        i.setImage(ImageUtils.decompressImage(i.getImage()));
                    } catch (DataFormatException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });

        });
        return ResponseEntity.ok(tasksAll);

    }

    public ResponseEntity<Task> getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("error"));
        task.getProgressDetails().forEach(t -> {
            t.getReferences().forEach(ref -> {
                try {
                    ref.setImage(ImageUtils.decompressImage(ref.getImage()));
                } catch (DataFormatException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        return ResponseEntity.ok(task);
    }

    public ResponseEntity<ResponseBase> deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok(new ResponseBase("deleted", true));
    }


    public ResponseEntity<ResponseBase> updateModuleProgress(String taskId, String moduleName, Task.ProgressStatus progressStatus, MultipartFile[] referencePics, String comment) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("No such Task found"));
        Map<String, Task.ProgressStatus> moduleProgress = task.getModuleLevelProgress();
        if (moduleProgress != null && moduleProgress.containsKey(moduleName)) {
            try {
                ProgressInfo progressInfo = extractProgressInfo(referencePics, comment, progressStatus.equals(Task.ProgressStatus.DONE) ? Task.ProgressStatus.IN_REVIEW : progressStatus, moduleName);
                Set<ProgressInfo> preProgressInfo = task.getProgressDetails();
                if (preProgressInfo == null) {
                    Set<ProgressInfo> progressInfoSet = new HashSet<>();
                    progressInfoSet.add(progressInfo);
                    task.setProgressDetails(progressInfoSet);
                } else {
                    preProgressInfo.add(progressInfo);
                    task.setProgressDetails(preProgressInfo);
                }
                if(progressStatus.equals(Task.ProgressStatus.DONE))moduleProgress.put(moduleName, Task.ProgressStatus.IN_REVIEW);
                else moduleProgress.put(moduleName, progressStatus);

            } catch (Exception e) {
                throw new InvalidDataException("Invalid References");
            }

            taskRepository.save(task);
        }else throw new InvalidDataException("Module Update is Incorrect");

        return ResponseEntity.ok(new ResponseBase("Task Progress updated", true));
    }

    private ProgressInfo extractProgressInfo(MultipartFile[] referencePics, String comment, Task.ProgressStatus progressStatus, String module) throws IOException {
        ProgressInfo progressInfo = new ProgressInfo();
        List<ImageDetail> imageDetails = new ArrayList<>();
        for (MultipartFile multipart : referencePics) {
            byte[] pic = ImageUtils.compressImage(multipart.getBytes());
            ImageDetail imgDetail = new ImageDetail(
                    "Pic:" + UUID.randomUUID(),
                    multipart.getName(),
                    pic
            );
            imageDetailRepository.save(imgDetail);
            imageDetails.add(imgDetail);

        }
        progressInfo.setProgressInfoId("ProInfo:" + UUID.randomUUID());
        progressInfo.setComment(comment);
        progressInfo.setReferences(imageDetails);
        progressInfo.setProgressStatus(progressStatus);
        progressInfoRepository.save(progressInfo);
        return progressInfo;
    }

    public ResponseEntity<ResponseBase> updateTaskCompletionOrApproval(String taskId, Task.ProgressStatus progressStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("No such Task found"));
        if(task.getProgressPercent() == 100){
            if(progressStatus.equals(Task.ProgressStatus.DONE))task.setTaskProgress(Task.ProgressStatus.IN_REVIEW);
            else task.setTaskProgress(progressStatus);
        }
        taskRepository.save(task);
        return ResponseEntity.ok(new ResponseBase("Task Progress updated", true));
    }

    public ResponseEntity<ResponseBase> approveOrRejectModule(String taskId,String progressId, Task.ProgressStatus progressStatus){
        ProgressInfo progressInfo = progressInfoRepository.findById(progressId).orElseThrow(() -> new RuntimeException("No such Task found"));
        progressInfo.setProgressStatus(progressStatus);
        progressInfoRepository.save(progressInfo);
        if(progressStatus.equals(Task.ProgressStatus.APPROVED)) {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("No such Task found"));
            Map<String, Task.ProgressStatus> moduleProgress = task.getModuleLevelProgress();
            double modulesSize = moduleProgress.size();
            double completedModuleSize = moduleProgress.values().stream().filter(m -> m.equals(Task.ProgressStatus.IN_REVIEW)).count();
            double progressPercentage = (completedModuleSize / modulesSize) * 100;
            task.setProgressPercent(progressPercentage);
            if (progressPercentage == 100) task.setTaskProgress(Task.ProgressStatus.IN_REVIEW);
            else task.setTaskProgress(Task.ProgressStatus.IN_PROGRESS);

            taskRepository.save(task);
        }
        return ResponseEntity.ok(new ResponseBase("Module Progress updated.",true));
    }

    public ResponseEntity<List<Task>> getAllTasksInReview(){
        List<Task> tasksInReview = taskRepository.findAllByTaskProgress(Task.ProgressStatus.IN_REVIEW);
        return ResponseEntity.ok(tasksInReview);
    }

    public ResponseEntity<List<Task>> getMyTasks(String staffId) {
        ResponseEntity<List<Task>> tasks = getAllTasks();
        if(staffId == null)return tasks;
        List<Task> myTasks = tasks.getBody();
        return ResponseEntity.ok(myTasks.stream().filter(t -> {
            Team team = t.getTeam();
            return team.getTeamLeader().getStaffId().equals(staffId) ||
                    team.getTeamMembers().stream().map(stf -> stf.getStaffId()).toList().contains(staffId);
        }).toList());
    }

    public ResponseEntity<ResponseBase> adminApprove(String taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("no such task"));
        if(task.getProgressPercent() == 100 && task.getTaskProgress().equals(Task.ProgressStatus.IN_REVIEW)){
            task.setTaskProgress(Task.ProgressStatus.APPROVED);
            taskRepository.save(task);
        }
        return new ResponseEntity<>(new ResponseBase("Approved Task",true), HttpStatus.ACCEPTED);

    }
}
