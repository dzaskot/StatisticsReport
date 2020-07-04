package pl.edu.agh.mwo.kw;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RankingOfEmployees implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<Employee, Double> employeeStatistics = generateRanking(employees);
        System.out.println("Lp. |Imię i nazwisko     | Liczba godzin");
        int i =1;
        for (Map.Entry<Employee,Double> entry : employeeStatistics.entrySet()) {
            System.out.print(i + " .|");
            System.out.println(entry.getKey().getName().replace("_", " ") +
                    " " + entry.getValue());
            i++;
        }
    }

    private Map<Employee, Double> generateRanking(Set<Employee> employees) {
        Map<Employee, Double> employeeStatistics =
                employees.stream()
                        .collect(Collectors.toMap(
                                employee -> employee,
                                employee -> employee.getReports().stream()
                                        .mapToDouble(report -> report.getWorkingHours())
                                        .sum()
                        ))
                        .entrySet().stream()
                        .sorted(Map.Entry.<Employee, Double>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return employeeStatistics;
    }
}
