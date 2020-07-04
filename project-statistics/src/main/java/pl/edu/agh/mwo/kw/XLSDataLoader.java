package pl.edu.agh.mwo.kw;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class XLSDataLoader implements DataLoader{

    @Override
    public Set<Employee> loadDataFromFiles(String folderPath) {
        List<File> files = loadFilesFromPath(folderPath);
        Map<String, Employee> employeesMap = new HashMap<>();

        for(File file: files){
            String name = FilenameUtils.removeExtension(file.getName());
            Set<Report> reports = readXLSFile(file);
//            Optional<Employee> maybeEmployee = employees.stream()
//                    .filter(employee -> employee.getName().equals(name))
//                    .findAny();
//            if(maybeEmployee.isPresent()){}
            employeesMap.computeIfAbsent(name, key ->
                new Employee(name))
                .getReports().addAll(reports);
        }

        return new HashSet<>(employeesMap.values());
    }

    private List<File> loadFilesFromPath(String folderPath){
        List<File> files = new ArrayList<>();
        File directory = new File(folderPath);

        File[] fileArray = directory.listFiles();
        if(fileArray != null)
            for (File file : fileArray) {
                if (file.isFile() && FilenameUtils.isExtension(file.getAbsolutePath(),"xls")) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    files.addAll(loadFilesFromPath(file.getAbsolutePath()));
                }
            }
        return files;
    }

    private Set<Report> readXLSFile(File file){
        Set<Report> reports = new HashSet<>();

        try (InputStream xslStream = new FileInputStream(file)){
            Workbook workbook = WorkbookFactory.create(xslStream);
            for(Sheet sheet: workbook){
                Iterator<Row> rowIterator = sheet.rowIterator();
                rowIterator.next(); //skip first row
                while(rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    Cell dataCell = row.getCell(0);
                    Cell hoursCell = row.getCell(2);

                    if(dataCell == null || hoursCell == null
                            || dataCell.getCellType() == CellType.BLANK
                            || hoursCell.getCellType() == CellType.BLANK){
                        break;
                    }
                    //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDateTime date;
                    try {
                        date = row.getCell(0).getLocalDateTimeCellValue();
                        //date = LocalDateTime.parse(row.getCell(0).getStringCellValue(), dateFormatter);
                    }
                    catch(DateTimeParseException ex) {
                        break;
                    }

                    String task = row.getCell(1).getStringCellValue();

                    double workingHours;
                    try {
                        workingHours = row.getCell(2).getNumericCellValue();
                    }
                    catch(IllegalStateException | NumberFormatException ex){
                        break;
                    }
                    Report report = new Report();
                    report.setProject(sheet.getSheetName());
                    report.setDate(date);
                    report.setTask(task);
                    report.setWorkingHours(workingHours);
                    reports.add(report);
                }
                workbook.close();
                xslStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reports;
    }

    private static boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        DataFormatter dataFormatter = new DataFormatter();

        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }
}
