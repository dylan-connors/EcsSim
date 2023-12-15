import facilities.Facility;
import facilities.buildings.Building;
import university.Estate;
import university.HumanResource;
import university.Staff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimulationLoader {
    File saveFile;
    public SimulationLoader(String filePath) {
        this.saveFile = new File(String.format("src/saves/%s", filePath));
    }

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
