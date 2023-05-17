package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities;

import java.util.List;

public class Project {

    private String gitURL ;

    private String branchName ;

    private List<JIRARelease> releases ;

    private List<String> columns ;

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public List<JIRARelease> getReleases() {
        return releases;
    }

    public void setReleases(List<JIRARelease> releases) {
        this.releases = releases;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
