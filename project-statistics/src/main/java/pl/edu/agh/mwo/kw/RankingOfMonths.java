package pl.edu.agh.mwo.kw;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RankingOfMonths implements RankingGenerator{
    private Set<Employee> employees;

    public RankingOfMonths(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public void printRanking() {
        Map<String, Double> monthsRanking = generateRanking(employees);
        System.out.println("Lp. | miesiąc | ilość godzin");
        int lp = 1;
        monthsRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach((month -> System.out.println(lp + "." + month.getKey() + "|  " +  month.getValue())));
    }

    private Map<String, Double> generateRanking(Set<Employee> employees) {
        Map<String, Double> monthsMap = new HashMap<>();
        employees.forEach(employee -> employee.getReports().forEach(report -> {
            String monthInYear = report.getDate().getMonth() + " " + report.getDate().getYear();
            if(!monthsMap.containsKey(monthInYear))
                monthsMap.put(monthInYear, report.getWorkingHours());
            else
                monthsMap.put(monthInYear,monthsMap.get(monthInYear) + report.getWorkingHours());
        }));
//        for(Employee employee: employees){
//            for(Report report: employee.getReports()){
//                String monthInYear = report.getDate().getMonth() + " " + report.getDate().getYear();
//                if(!monthsMap.containsKey(monthInYear))
//                    monthsMap.put(monthInYear, report.getWorkingHours());
//                else
//                    monthsMap.put(monthInYear,monthsMap.get(monthInYear) + report.getWorkingHours());
//            }
//        }
        return monthsMap;
    }
}
