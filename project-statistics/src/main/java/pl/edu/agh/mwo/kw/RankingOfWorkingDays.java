package pl.edu.agh.mwo.kw;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingOfWorkingDays extends RankingPrinter implements Ranking {
    private final Map<LocalDateTime, Double> result;

    public RankingOfWorkingDays(Set<Employee> employees) {
        super("Dzień");
        this.result = generateRanking(employees);
    }

    @Override
    public void printRanking() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        int maxDayValue = result.keySet().stream()
                .max(Comparator.comparingInt(day -> day.format(dateTimeFormatter).length()))
                .get().format(dateTimeFormatter).length();

        System.out.println("=====TEN MOST BUSIEST DAY RANKING=====");
        printRow("Lp", rankingElement, "ilość godzin", maxDayValue);

        AtomicInteger lp = new AtomicInteger(1);
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.<LocalDateTime, Double>comparingByValue().reversed())
                .limit(10)
                .forEach((day -> {
                    String dayName = day.getKey().format(dateTimeFormatter);
                    printRow(lp+".", dayName, day.getValue().toString(), maxDayValue );
                    lp.getAndIncrement();
                }));

        System.out.println();
    }

    @Override
    public HSSFWorkbook exportRanking(HSSFWorkbook workbook) {
        return null;
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
