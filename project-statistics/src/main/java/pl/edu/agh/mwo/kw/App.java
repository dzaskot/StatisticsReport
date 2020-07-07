package pl.edu.agh.mwo.kw;

import org.apache.commons.cli.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class App
{
    public static void main( String[] args ){

        Options options = generateOptions();
        boolean guard = true;
        while (guard) {
            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd;

            try{
                cmd = parser.parse(options, args);
                String inputPath = cmd.getOptionValue('i');

                if(!cmd.hasOption('i')) {
                    System.out.println("Missing required option: input");
                    formatter.printHelp("statistics", options);
                    System.exit(1);
                }

                if(!cmd.hasOption('d') && !cmd.hasOption('e') && !cmd.hasOption('m')){
                    System.out.println("Missing ranking type option");
                    formatter.printHelp("statistics", options);
                    System.exit(1);
                }

                List<Ranking> rankings = new LinkedList<>();
                DataLoader loader = new XLSDataLoader();
                Set<Employee> employees = loader.loadDataFromFiles(inputPath);

                if (employees.isEmpty()) {
                    System.out.println("ERROR: No *.xls files in selected folder. Try again.");
                    System.exit(1);
                }
                if (cmd.hasOption("day")) {
                    rankings.add(new RankingOfWorkingDays(employees));
                }
                if (cmd.hasOption("month")) {
                    rankings.add(new RankingOfMonths(employees));
                }
                if (cmd.hasOption("employee")) {
                    rankings.add(new RankingOfEmployees(employees));
                }

                rankings.forEach(Ranking::printRanking);

                if(cmd.hasOption("export") && !cmd.getOptionValue("x").isEmpty()) {
                    try {
                        Path exportPath = Paths.get(cmd.getOptionValue("x"));
                        if (Files.isDirectory(exportPath) && Files.isWritable(exportPath)) {
                            HSSFWorkbook workbook = new HSSFWorkbook();
                            for (Ranking ranking : rankings) {
                                workbook = ranking.exportRanking(workbook);
                            }
                            writeXLS(workbook, exportPath + "\\Rankings.xls");
                            }
                        else {
                            printError();
                            continue;
                        }
                    }
                    catch (InvalidPathException ex) {
                        printError();
                        continue;
                    }
                }
                guard = false;
            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
                formatter.printHelp("statistics", options);
                System.exit(1);
            }
//            System.out.println("Select data folder path:");
//            try {
//                Path path = Paths.get(new Scanner(System.in).nextLine());
//                if (Files.exists(path)) {
//                    System.out.println("Select ranking type. -h for help.");
//                    CommandLine cmd = generateCommandLine(options, new Scanner(System.in).nextLine().split(" "));
//                    if(cmd==null) {
//                        System.out.println("Incorrect options. Try again.");
//                        continue;
//                    }
//                    List<Ranking> rankings = new LinkedList<>();
//
//                    DataLoader loader = new XLSDataLoader();
//                    Set<Employee> employees = loader.loadDataFromFiles(path.toString());
//
//                    if (employees.isEmpty()) {
//                        System.out.println("ERROR: No *.xls files in selected folder. Try again.");
//                        continue;
//                    }
//
//                    if (cmd.hasOption("help")) {
//                        HelpFormatter formatter = new HelpFormatter();
//                        formatter.printHelp("App", options);
//                        System.out.println();
//                        continue;
//                    }
//                    if (cmd.hasOption("day")) {
//                        rankings.add(new RankingOfWorkingDays(employees));
//                    }
//                    if (cmd.hasOption("month")) {
//                        rankings.add(new RankingOfMonths(employees));
//                    }
//                    if (cmd.hasOption("employee")) {
//                        rankings.add(new RankingOfEmployees(employees));
//                    }
//
//                    rankings.forEach(Ranking::printRanking);
//
//                    if(cmd.hasOption("export") && !cmd.getOptionValue("x").isEmpty()){
//                        try{
//                            Path exportPath = Paths.get(cmd.getOptionValue("x"));
//                            if(Files.isDirectory(exportPath) && Files.isWritable(exportPath)) {
//                                HSSFWorkbook workbook = new HSSFWorkbook();
//                                for(Ranking ranking: rankings){
//                                    workbook = ranking.exportRanking(workbook);
//                                }
//                                writeXLS(workbook, path +"\\Rankings.xls");
//                            }
//                            else {
//                                printError();
//                                continue;
//                            }
//                        }
//                        catch(InvalidPathException ex){
//                            printError();
//                            continue;
//                        }
//                    }
//
//                    guard = false;
//                } else {
//                    printError();
//                }
//            }
//            catch(InvalidPathException ex){
//                printError();
//            }
        }
    }

    private static Options generateOptions(){
        final Options options = new Options();
        Option input = new Option("i", "input", true, "Input files path");
        //input.setRequired(true);
        options.addOption(input);
        Option export = new Option("x", "export", true, "Export file path");
        input.setRequired(false);
        options.addOption(export);
        options.addOption("e", "employee", false, "Print employees by working hours in projects")
                .addOption("m", "month", false, "Print ranking of working hours in months")
                .addOption("d", "day", false, "Print ten the most busiest days ranking");
        return options;
    }

//    private static CommandLine generateCommandLine(final Options options, final String[] commandLineArguments)
//    {
//        final CommandLineParser cmdLineParser = new DefaultParser();
//        CommandLine commandLine = null;
//        try
//        {
//            commandLine = cmdLineParser.parse(options, commandLineArguments);
//        }
//        catch (ParseException parseException)
//        {
//            System.out.println(
//                    "ERROR: Unable to find command-line arguments "
//                            + Arrays.toString(commandLineArguments));
//            //System.out.println("Select -h for help.");
//        }
//        return commandLine;
//    }

    private static void printError(){
        System.out.println("ERROR: Wrong path. Try again.");
    }

    private static void writeXLS(HSSFWorkbook workbook, String path){
        try {
            FileOutputStream out =
                    new FileOutputStream(new File(path));
            workbook.write(out);
            out.close();
            System.out.println("Excel file Rankings.xls written successfully.");

        } catch (IOException e) {
            System.out.println("Excel file writing failed.");
        }
    }

}
