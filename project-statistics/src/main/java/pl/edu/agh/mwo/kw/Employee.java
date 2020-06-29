package pl.edu.agh.mwo.kw;


import java.util.HashSet;

public class Employee {

    private HashSet<Report> reports;

    public HashSet<Report> getReports() {
        return reports;
    }

    public void setReports(HashSet<Report> reports) {
        this.reports = reports;
    }


}
