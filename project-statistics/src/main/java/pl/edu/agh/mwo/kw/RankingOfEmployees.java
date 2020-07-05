package pl.edu.agh.mwo.kw;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RankingOfEmployees extends RankingPrinter implements Ranking {
    private final Map<Employee, Double> result;

    public RankingOfEmployees(Set<Employee> employees) {
        super("Pracownik");
        this.result = generateRanking(employees);
    }

    @Override
    public void printRanking() {
        int maxEmployeeNameLength = result.entrySet().stream()
                .max(Comparator.comparingInt(employee -> employee.getKey().getName().length()))
                .get().getKey().getName().length();
        System.out.println("===MOST BUSIEST EMPLOYEE RANKING===");
        printRow("Lp", rankingElement, "ilość godzin", maxEmployeeNameLength);

        int lp =1;
        for (Map.Entry<Employee,Double> entry : result.entrySet()) {
            String name = entry.getKey().getName().replace("_", " ");
            printRow(lp+".", name, entry.getValue().toString(), maxEmployeeNameLength);
            lp++;
        }
        System.out.println();
    }

    @Override
    public HSSFWorkbook exportRanking(HSSFWorkbook workbook) {
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
