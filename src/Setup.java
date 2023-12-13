import university.Staff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class to open and read the configuration file, parse the starting params to integers, and provide methods to get
 * this data
 */
public class Setup {
    String fileName;
    int startingBudget;
    int yearsToSimulate;

    public Setup(String name, String budget, String years) {
        this.fileName = name;
        this.startingBudget = Integer.parseInt(budget);
        this.yearsToSimulate = Integer.parseInt(years);
    }

    /**
     * Uses a while loop and the scanner class to read the config file, and extract the name and skill by splitting
     * each string at the "(" character. It returns an arraylist of this data.
     */
    public ArrayList<Staff> extractStaffFromFile() {
        ArrayList<Staff> staffList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File("src\\" + this.fileName));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] lineComponents = line.split("\\(");
                lineComponents[1] = lineComponents[1].replace(")", "");
                staffList.add(new Staff(lineComponents[0].trim(), Integer.parseInt(lineComponents[1])));
            }

            return staffList;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return null;
        }
    }

    public int getStartingBudget() { return this.startingBudget; }

    public int getYearsToSimulate() { return this.yearsToSimulate; }
}
