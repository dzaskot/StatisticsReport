package pl.edu.agh.mwo.kw;

import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class App
{
    public static void main( String[] args ){

        Options options = generateOptions();
        CommandLineParser parser = new DefaultParser();

        boolean guard = true;
        while (guard) {
            System.out.println("Select data folder path:");
            try {
                Path path = Paths.get(new Scanner(System.in).nextLine());
                if (Files.exists(path)) {
                    DataLoader loader = new XLSDataLoader();
                    Set<Employee> employees = loader.loadDataFromFiles(path.toString());

                    if (employees.isEmpty()) {
                        System.out.println("ERROR: No *.xls files in selected folder. Try again.");
                        continue;
                    }
                    System.out.println("Select ranking type. -h for help.");
                    CommandLine cmd = generateCommandLine(options, new Scanner(System.in).nextLine().split(" "));

                    if (cmd.hasOption("help")) {
                        HelpFormatter formatter = new HelpFormatter();
                        formatter.printHelp("App", options);
                        System.out.println();
                        continue;
                    }
                    if (cmd.hasOption("day")) {
                        System.out.println("=====TEN MOST BUSIEST DAY RANKING=====");
                        RankingExtractor ranking1 = new RankingOfWorkingDays(employees);
                        ranking1.printRanking();
                        System.out.println();
                    }
                    if (cmd.hasOption("month")) {
                        System.out.println("====MOST BUSIEST MONTH RANKING====");
                        RankingExtractor ranking2 = new RankingOfMonths(employees);
                        ranking2.printRanking();
                        System.out.println();
                    }
                    if (cmd.hasOption("employee")) {
                        System.out.println("===MOST BUSIEST EMPLOYEE RANKING===");
                        RankingExtractor ranking3 = new RankingOfEmployees(employees);
                        ranking3.printRanking();
                        System.out.println();
                    }
                    guard = false;
                } else {
                    printError();
                }
            }
            catch(InvalidPathException ex){
                printError();
            }

        }
    }

    private static Options generateOptions(){
        final Options options = new Options();
        options.addOption("e", "employee", false, "Print employees by working hours in projects")
                .addOption("m", "month", false, "Print ranking of working hours in months")
                .addOption("d", "day", false, "Print ten the most busiest days ranking")
                .addOption("x", "export", true, "Folder export path")
                .addOption("h", "help", false, "Display help information");
        return options;
    }

    private static CommandLine generateCommandLine(final Options options, final String[] commandLineArguments)
    {
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try
        {
            commandLine = cmdLineParser.parse(options, commandLineArguments);
        }
        catch (ParseException parseException)
        {
            System.out.println(
                    "ERROR: Unable to find command-line arguments "
                            + Arrays.toString(commandLineArguments));
            //System.out.println("Select -h for help.");
        }
        return commandLine;
    }

    private static void printError(){
        System.out.println("ERROR: Wrong path. Try again.");
    }
}
