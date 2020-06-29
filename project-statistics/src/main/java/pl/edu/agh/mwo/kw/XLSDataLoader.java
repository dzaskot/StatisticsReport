package pl.edu.agh.mwo.kw;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class XLSDataLoader implements DataLoader{

    private List<File> files = new ArrayList<>();
    @Override
    public HashSet<Employee> loadDataFromFiles(String folderPath) {
        loadFilesFromPath(folderPath);
        return null;
    }

    private void loadFilesFromPath(String folderPath){

        File directory = new File(folderPath);

        File[] fileArray = directory.listFiles();
        if(fileArray != null)
            for (File file : fileArray) {
                if (file.isFile() && FilenameUtils.isExtension(file.getAbsolutePath(),"xls")) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    loadFilesFromPath(file.getAbsolutePath());
                }
            }
    }
}
