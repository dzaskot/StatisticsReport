package pl.edu.agh.mwo.kw;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    public static Set<Employee> generateEmployeeSet(){
        Set<Employee> testSet = new HashSet<>();
        Employee employee1 = new Employee("employee1");
        Employee employee2 = new Employee("employee2");
        Report report1 = new Report();
        report1.setProject("Projekt1");
        report1.setDate(LocalDate.of(2020,
                Month.JULY, 7));
        report1.setTask("testing1");
        report1.setWorkingHours(10);
        Report report2 = new Report();
        report2.setProject("Projekt2");
        report2.setDate(LocalDate.of(2020,
                Month.MAY, 5));
        report2.setWorkingHours(10);
        report2.setTask("testing2");
        employee1.getReports().add(report1);
        employee1.getReports().add(report2);
        Report report3 = new Report();
        report3.setProject("Projekt1");
        report3.setDate(LocalDate.of(2020,
                Month.JULY, 7));
        report3.setTask("testing3");
        report3.setWorkingHours(4.5);
        employee2.getReports().add(report3);
        testSet.add(employee1);
        testSet.add(employee2);
        return testSet;
    }
}
