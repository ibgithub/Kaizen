package com.dev.kaizen.adapter;

public class Program {
    private int id;
    private String programName, background, totalDays, totalBudget, problemList, taskList,
            resultList, urlPhotoBefore, urlPhotoAfter, urlVideo, notes, participantGroup;

    public Program() {

    }

    public Program(int id, String programName, String background, String totalDays, String totalBudget,
                   String problemList, String taskList, String resultList, String urlPhotoBefore,
                   String urlPhotoAfter, String urlVideo, String notes, String participantGroup) {
        this.id = id;
        this.programName = programName;
        this.background = background;
        this.totalDays = totalDays;
        this.totalBudget = totalBudget;
        this.problemList = problemList;
        this.taskList = taskList;
        this.resultList = resultList;
        this.urlPhotoBefore = urlPhotoBefore;
        this.urlPhotoAfter = urlPhotoAfter;
        this.urlVideo = urlVideo;
        this.notes = notes;
        this.participantGroup = participantGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(String totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getProblemList() {
        return problemList;
    }

    public void setProblemList(String problemList) {
        this.problemList = problemList;
    }

    public String getTaskList() {
        return taskList;
    }

    public void setTaskList(String taskList) {
        this.taskList = taskList;
    }

    public String getResultList() {
        return resultList;
    }

    public void setResultList(String resultList) {
        this.resultList = resultList;
    }

    public String getUrlPhotoBefore() {
        return urlPhotoBefore;
    }

    public void setUrlPhotoBefore(String urlPhotoBefore) {
        this.urlPhotoBefore = urlPhotoBefore;
    }

    public String getUrlPhotoAfter() {
        return urlPhotoAfter;
    }

    public void setUrlPhotoAfter(String urlPhotoAfter) {
        this.urlPhotoAfter = urlPhotoAfter;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getParticipantGroup() {
        return participantGroup;
    }

    public void setParticipantGroup(String participantGroup) {
        this.participantGroup = participantGroup;
    }
}
