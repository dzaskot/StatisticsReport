package pl.edu.agh.mwo.kw;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RankingOfWorkingDays implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfWorkingDays(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<LocalDateTime, Double> dayRanking = generateRanking(employees);
        System.out.println("Lp. | miesiąc | ilość godzin");
        int lp = 1;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dayRanking.entrySet().stream()
                .sorted(Map.Entry.<LocalDateTime, Double>comparingByValue().reversed())
                .limit(10)
                .forEach((day -> {
                    System.out.println( lp + "." + day.getKey().format(dateTimeFormatter) + "|  " + day.getValue());
                }));
    }

    private Map<LocalDateTime, Double> generateRanking(Set<Employee> employees) {
        Map<LocalDateTime, Double> dayMap = new HashMap<>();
        employees.forEach(employee -> employee.getReports().forEach(
                report -> {
                    if(!dayMap.containsKey(report.getDate())){
                        dayMap.put(report.getDate(),report.getWorkingHours());
                    }
                    else{
                        dayMap.put(report.getDate(), dayMap.get(report.getDate()) + report.getWorkingHours());
                    }
                }
        ));
        return dayMap;
    }
}
