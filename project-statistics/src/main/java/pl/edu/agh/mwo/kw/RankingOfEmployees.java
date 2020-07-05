package pl.edu.agh.mwo.kw;

import java.util.*;
import java.util.stream.Collectors;

public class RankingOfEmployees extends RankingPrinter implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfEmployees(Set<Employee> employees) {
        super("Pracownik");
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<Employee, Double> employeeStatistics = generateRanking(employees);

        int maxEmployeeNameLength = employees.stream()
                .max(Comparator.comparingInt(employee -> employee.getName().length()))
                .get().getName().length();
        printRow("Lp", rankingElement, "ilość godzin", maxEmployeeNameLength);

        int lp =1;
        for (Map.Entry<Employee,Double> entry : employeeStatistics.entrySet()) {
            String name = entry.getKey().getName().replace("_", " ");
            printRow(lp+".", name, entry.getValue().toString(), maxEmployeeNameLength);
            lp++;
        }
    }

    private Map<Employee, Double> generateRanking(Set<Employee> employees) {
        return employees.stream()
                        .collect(Collectors.toMap(
                                employee -> employee,
                                employee -> employee.getReports().stream()
                                        .mapToDouble(Report::getWorkingHours)
                                        .sum()
                        ))
                        .entrySet().stream()
                        .sorted(Map.Entry.<Employee, Double>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    }
}
