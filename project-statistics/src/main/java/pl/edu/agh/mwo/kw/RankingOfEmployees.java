package pl.edu.agh.mwo.kw;

import java.util.*;
import java.util.stream.Collectors;

public class RankingOfEmployees implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<Employee, Double> employeeStatistics = generateRanking(employees);

        Optional<Employee> maybeEmployee = employees.stream()
                .max(Comparator.comparingInt(employee -> employee.getName().length()));
        int maxNameLength = 0;
        if(maybeEmployee.isPresent()){
            maxNameLength = maybeEmployee.get().getName().length();
        }


        int spacesCount = (maxNameLength > 15)?maxNameLength - 15: 0;

        StringBuilder header = new StringBuilder().append("Lp. | Imię i nazwisko");
        for(int i = 0; i<= spacesCount; i++) header.append(" ");
        header.append(" | Liczba godzin");

        System.out.println(header);
        int lp =1;
        for (Map.Entry<Employee,Double> entry : employeeStatistics.entrySet()) {
            StringBuilder rankingRow = new StringBuilder().append(lp).append(".  | ");
            String name = entry.getKey().getName().replace("_", " ");
            rankingRow.append(name);
            int nameSpaces = (maxNameLength > 15)?(maxNameLength - name.length()):15 - name.length();
            for(int j=0; j<= nameSpaces; j++) rankingRow.append(" ");
            rankingRow.append(" | ").append(entry.getValue());
            System.out.println(rankingRow);
            lp++;
        }
    }

    private Map<Employee, Double> generateRanking(Set<Employee> employees) {
        return employees.stream()
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

    }
}
