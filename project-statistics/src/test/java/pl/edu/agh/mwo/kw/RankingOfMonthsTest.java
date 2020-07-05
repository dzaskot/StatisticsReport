package pl.edu.agh.mwo.kw;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class RankingOfMonthsTest {
    @Test
    public void shouldPrintEmployees() throws IOException {
        // given
        RankingOfMonths rankingOfMonths = new RankingOfMonths(Utils.generateEmployeeSet());
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));

        // when
        rankingOfMonths.printRanking();

        // then
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        assertEquals(
                "====MOST BUSIEST MONTH RANKING====" + System.lineSeparator() +
                        "Lp  | Month        | Working hours" + System.lineSeparator() +
                        "1.  | lipiec 2020  | 14.5" +  System.lineSeparator() +
                        "2.  | maj 2020     | 10.0" +  System.lineSeparator() +
                        System.lineSeparator(),
                allWrittenLines
        );
    }
}
