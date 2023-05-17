package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans;

import java.util.List;

public class ProjectsBean {

    private List<ProjectBean> beans ;

    public List<ProjectBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ProjectBean> beans) {
        this.beans = beans;
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

        ProjectsBean cast = (ProjectsBean) obj ;

        return this.beans.equals(cast.beans) ;
    }
}
