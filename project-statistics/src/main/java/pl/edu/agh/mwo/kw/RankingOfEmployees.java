package pl.edu.agh.mwo.kw;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RankingOfEmployees extends RankingPrinter implements Ranking {
    private final Map<Employee, Double> result;

    public RankingOfEmployees(Set<Employee> employees) {
        super("Employee");
        this.result = generateRanking(employees);
    }

    @Override
    public void printRanking() {
        int maxEmployeeNameLength = result.entrySet().stream()
                .max(Comparator.comparingInt(employee -> employee.getKey().getName().length()))
                .get().getKey().getName().length();
        System.out.println("====MOST BUSIEST EMPLOYEE RANKING====");
        printRow("Lp", rankingElement, "Working hours", maxEmployeeNameLength);

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
        String[] columns = {"Lp.", "Employee", "Working hours"};
        HSSFSheet employeeSheet = workbook.createSheet("Employee Ranking");
        Row headerRow = employeeSheet.createRow(0);
        for(int i=0; i<columns.length; i++) headerRow.createCell(i).setCellValue(columns[i]);
        int lp =1;
        for (Map.Entry<Employee, Double> employee: result.entrySet() ){
            Row row = employeeSheet.createRow(lp);
            Cell cellLp = row.createCell(0);
            cellLp.setCellValue(lp);
            Cell cellEmployee = row.createCell(1);
            cellEmployee.setCellValue(employee.getKey().getName().replace("_", " "));
            Cell cellHours = row.createCell(2);
            cellHours.setCellValue(employee.getValue());
            lp++;
        }
        return workbook;
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
