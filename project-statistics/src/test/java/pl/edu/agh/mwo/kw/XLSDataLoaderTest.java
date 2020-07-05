package pl.edu.agh.mwo.kw;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class XLSDataLoaderTest {

    @Test
    public void shouldLoadDataFromFile(){
        // given
        XLSDataLoader xlsDataLoader = new XLSDataLoader();

        // when
        Set<Employee> employees = xlsDataLoader.loadDataFromFiles(
                "./src/test/resources/"
        );

        // then
        assertEquals(2, employees.size());
    }
}
