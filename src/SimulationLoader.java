import facilities.Facility;
import facilities.buildings.Building;
import university.Estate;
import university.HumanResource;
import university.Staff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to restore the state of a simulation based on the contents of a save file passed in as a parameter. The file
 * contains the years that have elapsed, the budget of the university, the facilities built, the staff employed, and the
 * staff available in the staff market.
 */
public class SimulationLoader {
    File saveFile;
    public SimulationLoader(String filePath) {
        this.saveFile = new File(String.format("src/saves/%s", filePath));
    }

    /**
     * Uses the scanner class to search the save file for the year, then returns it. Returns a -1 to indicate no year
     * was found.
     */
    public int extractStartingYear() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.saveFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("year:") && scanner.hasNext()) {
                return scanner.nextInt();
            }
        }
        return -1;
    }

    /**
     * Uses the scanner class to search the save file for the budget, then returns it. Returns a -1 to indicate no year
     * was found.
     */
    public float extractBudget() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.saveFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("budget:") && scanner.hasNext()) {
                return scanner.nextFloat();
            }
        }
        return -1;
    }

    /**
     * Uses the scanner class to search the save file for the facilities that need to be restored. Uses nested while
     * loops, along with the estate instance from ecsSim to add the facilities specified to the estate.
     */
    public void extractFacilities(Estate estate) throws FileNotFoundException {
        Scanner scanner = new Scanner(this.saveFile);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("facilities:")) {
                while (scanner.hasNext()) {
                    String line2 = scanner.nextLine();
                    if (line2.equals("staff market:")) {
                        break;
                    } else {
                       String[] facilitiesData = line2.split(":");
                       Facility facility = estate.addFacility(facilitiesData[0], facilitiesData[1]);
                       ((Building) facility).setLevel(Integer.parseInt(facilitiesData[2]));
                    }
                }
            }
        }
    }

    /**
     * Uses the scanner class, along with nested while loops, to add the staff in the staff market to an array list that
     * is returned to EcsSim.
     */
    public ArrayList<Staff> extractStaffMarket() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.saveFile);
        ArrayList<Staff> listOfStaffMarketStaff = new ArrayList<>();

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("staff market:")) {
                while (scanner.hasNext()) {
                    String line2 = scanner.nextLine();
                    if (line2.equals("staff:")) {
                        return listOfStaffMarketStaff;
                    } else {
                        String[] staffData = line2.split(":");
                        Staff s = new Staff(staffData[0], Integer.parseInt(staffData[1]));
                        s.setStamina(Integer.parseInt(staffData[2]));
                        listOfStaffMarketStaff.add(s);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Uses the scanner class and the HumanResource instance to add staff members to the university
     */
    public void extractEmployedStaff(HumanResource hr) throws FileNotFoundException {
        Scanner scanner = new Scanner(this.saveFile);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("staff:")) {
                while (scanner.hasNext()) {
                    String line2 = scanner.nextLine();
                    if (line2.isEmpty()) {
                        break;
                    } else {
                        String[] staffData = line2.split(":");
                        Staff s = new Staff(staffData[0], Integer.parseInt(staffData[3]));
                        s.setStamina(Integer.parseInt(staffData[2]));
                        hr.addStaff(s, Float.parseFloat(staffData[1]));
                    }
                }
            }
        }
    }
}
