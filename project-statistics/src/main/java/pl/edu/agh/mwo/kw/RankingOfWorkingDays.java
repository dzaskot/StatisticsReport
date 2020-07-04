package pl.edu.agh.mwo.kw;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingOfWorkingDays implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfWorkingDays(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<LocalDateTime, Double> dayRanking = generateRanking(employees);
        AtomicInteger lp = new AtomicInteger(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        int maxDayValue = dayRanking.keySet().stream()
                .max(Comparator.comparingInt(day -> day.format(dateTimeFormatter).length()))
                .get().format(dateTimeFormatter).length();
        StringBuilder header = new StringBuilder("Lp. | dzień");
        int spacesHeader = (maxDayValue> 5)? maxDayValue - 5: 0;
        for(int i = 0; i<=spacesHeader; i++) header.append(" ");
        System.out.println(header.append("| liczba godzin"));

        dayRanking.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.<LocalDateTime, Double>comparingByValue().reversed())
                .limit(10)
                .forEach((day -> {
                    String dayName = day.getKey().format(dateTimeFormatter);
                    StringBuilder rowRanking = new StringBuilder(lp + ".  | "+ dayName);
                    int spacesDay = (dayName.length() < maxDayValue)? maxDayValue - dayName.length(): 0;
                    for(int j =0; j<= spacesDay; j++) rowRanking.append(" ");
                    System.out.println( rowRanking+ "| " + day.getValue());
                    lp.getAndIncrement();
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
