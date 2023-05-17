package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans;

import java.util.List;

public class ProjectBean {

    private String gitURL ;

    private String branchName ;

    private List<JIRAReleaseBean> releases ;

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

    public List<JIRAReleaseBean> getReleases() {
        return releases;
    }

    public void setReleases(List<JIRAReleaseBean> releases) {
        this.releases = releases;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) {
            return false ;
        }

        ProjectBean cast = (ProjectBean) obj ;

        boolean condition1 = this.gitURL.equals(cast.gitURL) ;
        boolean condition2 = this.branchName.equals(cast.branchName) ;
        boolean condition3 = this.releases.equals(cast.releases) ;
        boolean condition4 = this.columns.equals(cast.columns) ;

        return condition1 && condition2 && condition3 && condition4 ;
    }
}
