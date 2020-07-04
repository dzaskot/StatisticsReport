package pl.edu.agh.mwo.kw;

import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingOfMonths implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfMonths(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<String, Double> monthsRanking = generateRanking(employees);
        int maxMonthLength = monthsRanking.keySet().stream()
                .max(Comparator.comparingInt(month -> month.length()))
                .get().length();
        StringBuilder header = new StringBuilder().append("Lp. | miesiąc");
        int spacesCount = (maxMonthLength > 7)? maxMonthLength - 7: 0;
        for(int i = 0; i<= spacesCount; i++) header.append(" ");
        header.append(" | ilość godzin");
        System.out.println(header);
        AtomicInteger lp = new AtomicInteger(1);
        monthsRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach((month -> {
                    StringBuilder rankingRow = new StringBuilder().append(lp).append(".  | ").append(month.getKey());
                    int spacesMonth = (month.getKey().length() > 7)? maxMonthLength - month.getKey().length(): 0;
                    for(int j=0; j<= spacesMonth; j++) rankingRow.append(" ");
                    rankingRow.append(" | ").append(month.getValue());
                    System.out.println(rankingRow);
                    lp.getAndIncrement();
                }));
    }

    private Map<String, Double> generateRanking(Set<Employee> employees) {
        Map<String, Double> monthsMap = new HashMap<>();

        employees.forEach(employee -> employee.getReports().forEach(report -> {
            String monthInYear = report.getDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl")) + " " + report.getDate().getYear();
            if(!monthsMap.containsKey(monthInYear))
                monthsMap.put(monthInYear, report.getWorkingHours());
            else
                monthsMap.put(monthInYear,monthsMap.get(monthInYear) + report.getWorkingHours());
        }));
        return monthsMap;
    }
}
