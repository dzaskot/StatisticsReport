package pl.edu.agh.mwo.kw;

import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingOfMonths extends RankingPrinter implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfMonths(Set<Employee> employees) {
        super("Miesiąc");
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<String, Double> monthsRanking = generateRanking(employees);
        int maxMonthLength = monthsRanking.keySet().stream()
                .max(Comparator.comparingInt(String::length))
                .get().length();
        printRow("Lp", rankingElement, "ilość godzin", maxMonthLength);
        AtomicInteger lp = new AtomicInteger(1);
        monthsRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach((month ->
                        printRow(lp + ".", month.getKey(), month.getValue().toString(), maxMonthLength)));
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
