package pl.edu.agh.mwo.kw;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class RankingOfWorkingDays extends RankingPrinter implements Ranking {
    private final Map<LocalDate, Double> result;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public RankingOfWorkingDays(Set<Employee> employees) {
        super("Day");
        this.result = generateRanking(employees);
    }

    @Override
    public void printRanking() {

        int maxDayValue = result.keySet().stream()
                .max(Comparator.comparingInt(day -> day.format(dateTimeFormatter).length()))
                .get().format(dateTimeFormatter).length();

        System.out.println("=====TEN MOST BUSIEST DAY RANKING=====");
        printRow("Lp", rankingElement, "Working hours", maxDayValue);

        int lp = 1;
        for(Map.Entry<LocalDate,Double> entry : result.entrySet() ){
                    String dayName = entry.getKey().format(dateTimeFormatter);
                    printRow(lp+".", dayName, entry.getValue().toString(), maxDayValue );
                    lp++;
                }
        System.out.println();
    }

    @Override
    public HSSFWorkbook exportRanking(HSSFWorkbook workbook) {
        String[] columns = {"Lp.", "Working Day", "Working hours"};
        HSSFSheet daySheet = workbook.createSheet("Day Ranking");
        Row headerRow = daySheet.createRow(0);
        for (int i = 0; i < columns.length; i++) headerRow.createCell(i).setCellValue(columns[i]);
        int lp = 1;
        for (Map.Entry<LocalDate, Double> day : result.entrySet()) {
            Row row = daySheet.createRow(lp);
            Cell cellLp = row.createCell(0);
            cellLp.setCellValue(lp);
            Cell cellDay = row.createCell(1);
            cellDay.setCellValue(day.getKey().format(dateTimeFormatter));
            Cell cellHours = row.createCell(2);
            cellHours.setCellValue(day.getValue());
            lp++;
        }
        return workbook;
    }

    private Map<LocalDate, Double> generateRanking(Set<Employee> employees) {
        Map<LocalDate, Double> dayMap = new HashMap<>();
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
        return dayMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.<LocalDate, Double>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
