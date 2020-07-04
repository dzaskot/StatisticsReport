package pl.edu.agh.mwo.kw;

import java.util.Set;

public interface DataLoader {
    Set<Employee> loadDataFromFiles(String folderPath);
}
