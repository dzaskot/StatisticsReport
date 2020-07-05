package pl.edu.agh.mwo.kw;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class RankingOfEmployeesTest {

    @Test
    public void shouldPrintEmployees() throws IOException {
        // given
        RankingOfEmployees rankingOfEmployees = new RankingOfEmployees(Utils.generateEmployeeSet());
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));

        // when
        rankingOfEmployees.printRanking();

        // then
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        assertEquals(
                "====MOST BUSIEST EMPLOYEE RANKING====" + System.lineSeparator() +
                "Lp  | Employee   | Working hours" + System.lineSeparator() +
                "1.  | employee1  | 20.0" +  System.lineSeparator() +
                "2.  | employee2  | 4.5" +  System.lineSeparator() +
                        System.lineSeparator(),
                allWrittenLines
        );
    }


}
