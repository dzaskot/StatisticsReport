package pl.edu.agh.mwo.kw;

import java.time.LocalDate;
import java.util.Objects;

public class Report {

    private LocalDate date;
    private String project;
    private String task;
    private double workingHours;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return date.equals(report.date) &&
                project.equals(report.project) &&
                task.equals(report.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, project);
    }
}
