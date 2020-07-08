# project-statistics
Console program to calculate project statistics.

User runs the program with the option input and following it - the path to the folder with the data.
User needs to determine which of the options (m, d, e - see the usage below) of ranking should be printed to the console.
By choosing export option with the following path (with name of the file ex. c:\Rancings.xls" we will get the xls file exported,
where the selected statistics will be exported to separated sheet - named according to the name of ranking.


usage:
 -d,--day            Print ten the most busiest days ranking
 -e,--employee       Print employees by working hours in projects
 -h,--help           Display help information
 -i,--input <arg>    Input files path
 -m,--month          Print ranking of working hours in months
 -x,--export <arg>   Export file path
