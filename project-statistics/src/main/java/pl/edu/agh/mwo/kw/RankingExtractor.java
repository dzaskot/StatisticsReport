package pl.edu.agh.mwo.kw;

import org.apache.poi.ss.usermodel.Workbook;

public interface RankingExtractor {
    void printRanking();
    Workbook exportRanking(Workbook workbook);
}
