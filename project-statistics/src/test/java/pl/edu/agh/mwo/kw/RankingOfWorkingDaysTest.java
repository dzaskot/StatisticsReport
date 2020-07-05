package pl.edu.agh.mwo.kw;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class RankingOfWorkingDaysTest {
    @Test
    public void shouldPrintEmployees() throws IOException {
        //given
        RankingOfWorkingDays rankingOfWorkingDays = new RankingOfWorkingDays(Utils.generateEmployeeSet());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        //when
        rankingOfWorkingDays.printRanking();
        //then
        os.flush();
        String allWrittenLines = new String(os.toByteArray());
        assertEquals(
                "=====TEN MOST BUSIEST DAY RANKING=====" + System.lineSeparator() +
                        "Lp  | Day            | Working hours" + System.lineSeparator() +
                        "1.  | 05 lipca 2020  | 14.5" +  System.lineSeparator() +
                        "2.  | 05 maja 2020   | 10.0" +  System.lineSeparator() +
                        System.lineSeparator(),
                allWrittenLines
        );
    }
}
