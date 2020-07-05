package pl.edu.agh.mwo.kw;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingOfMonths extends RankingPrinter implements RankingExtractor {
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

    @Override
    public Workbook exportRanking(Workbook workbook) {
        Map<String, Double> monthsRanking = generateRanking(employees);
        String[] columns = {"Lp.", "Months", "Working hours"};
        Sheet monthsSheet = workbook.createSheet("Months Ranking");
        Row headerRow = monthsSheet.createRow(0);
        int lp =1;
        for (Map.Entry<String, Double> month: monthsRanking.entrySet() ){
            Row row = monthsSheet.createRow(lp);
            Cell cellLp = row.createCell(0);
            cellLp.setCellValue(lp);
            Cell cellMonth = row.createCell(1);
            cellMonth.setCellValue(month.getKey());
            Cell cellHours = row.createCell(2);
            cellHours.setCellValue(month.getValue());
            lp++;
        }
        return workbook;
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
