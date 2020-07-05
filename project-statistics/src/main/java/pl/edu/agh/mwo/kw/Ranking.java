package pl.edu.agh.mwo.kw;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface Ranking {
    void printRanking();
    HSSFWorkbook exportRanking(HSSFWorkbook workbook);
}
