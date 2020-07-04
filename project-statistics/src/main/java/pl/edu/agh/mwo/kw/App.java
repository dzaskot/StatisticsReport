package pl.edu.agh.mwo.kw;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

public class App
{
    public static void main( String[] args ) {
        Options options = new Options();
        options.addOption("e", "employee", false, "Print employees by working hours in projects")
                .addOption("m", "month", false, "Print ranking of working hours in months")
                .addOption("d", "day", false, "Print ten the most busiest days ranking")
                .addOption("p", "path", true, "Folder path")
                .addOption("h", "help", false, "Display help information");

        //HelpFormatter formatter = new HelpFormatter();
        //formatter.printHelp("App", options);
        CommandLineParser parser = new DefaultParser();

        boolean guard = true;
        while (guard) {
//            try {
//                CommandLine line = parser.parse(options, args);
//                if (line.hasOption("path")) {
//                    if (Files.exists(Paths.get(args[0]))) {
//                        DataLoader loader = new XLSDataLoader();
//                        loader.loadDataFromFiles(args[0]);
//                        guard = false;
//                    }
//                }
//            }
//            catch (ParseException exp) {
//                System.out.println("Podaj prawidłową ścieżkę do folderu");
//            }
            System.out.println("Podaj ścieżkę do pliku");
            String path = new Scanner(System.in).nextLine();
            if(Files.exists(Paths.get(path))) {
                DataLoader loader = new XLSDataLoader();
                Set<Employee> employees = loader.loadDataFromFiles(path);
//                employees.forEach(employee -> {
//                    System.out.println(employee.getName());
//                    employee.getReports().forEach(
//                            report -> {
//                                System.out.println(report.getProject() + " " + report.getDate() + " " + report.getWorkingHours());
//                            }
//                    );
//                });

                RankingGenerator generator1 = new RankingOfWorkingDays(employees);
                generator1.printRanking();
                guard = false;
            }
            else{
                System.out.println("Błędna ścieżka. Spróbuj jeszcze raz.");
            }

        }
    }
}
