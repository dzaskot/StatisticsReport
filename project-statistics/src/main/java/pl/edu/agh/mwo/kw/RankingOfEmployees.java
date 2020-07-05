package pl.edu.agh.mwo.kw;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RankingOfEmployees extends RankingPrinter implements RankingExtractor {
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

    @Override
    public Workbook exportRanking(Workbook workbook) {
        Map<Employee, Double> employeeStatistics = generateRanking(employees);
        return null;
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
