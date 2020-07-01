package pl.edu.agh.mwo.kw;


import java.util.HashSet;

public class Employee {
    private String name;
    private HashSet<Report> reports = new HashSet<>();

    public Employee(String name) {
        this.name = name;
    }

    public HashSet<Report> getReports() {
        return reports;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
