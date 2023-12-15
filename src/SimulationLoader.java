import facilities.Facility;
import facilities.buildings.Hall;
import facilities.buildings.Lab;
import facilities.buildings.Theatre;
import university.HumanResource;
import university.Staff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SimulationLoader {
    File saveFile;
    int yearsToSimulate;
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

    public ArrayList<Facility> extractFacilities() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.saveFile);
        ArrayList<Facility> listOfFacilities = new ArrayList<>();

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("facilities:")) {
                while (scanner.hasNext()) {
                    String line2 = scanner.nextLine();
                    if (line2.equals("staff market:")) {
                        return listOfFacilities;
                    } else {
                        String[] facilityData = line2.split(" ");
                        System.out.println(Arrays.toString(facilityData));
                        String buildingName = "";
                        for (int i = 0; i < facilityData.length; i++) {
                            if (i != 0 && !facilityData[i].matches(".*\\d.*")) {
                                buildingName = buildingName + " " + facilityData[i];
                            }
                        }
                        System.out.println(buildingName.trim());
                        switch (facilityData[0]) {
                            case ("Hall"):
                                Hall h = new Hall(buildingName);
                                h.setLevel(Integer.parseInt(facilityData[facilityData.length - 1]));
                                listOfFacilities.add(h);
                            case ("Lab"):
                                Lab l = new Lab(buildingName);
                                l.setLevel(Integer.parseInt(facilityData[facilityData.length - 1]));
                                listOfFacilities.add(l);
                            case ("Theatre"):
                                Theatre t = new Theatre(buildingName);
                                t.setLevel((Integer.parseInt(facilityData[facilityData.length - 1])));
                                listOfFacilities.add(t);
                        }
                    }
                }
            }
        }
        return null;
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
                        String[] lineAsArray = line2.split(" ");
                        String[] staffData = {"", "", ""};
                        for (int i = 0; i < lineAsArray.length; i++) {
                            if (!lineAsArray[i].matches(".*\\d.*")) {
                                staffData[0] = staffData[0] + " " + lineAsArray[i];
                            } else if (staffData[1].isEmpty() && staffData[2].isEmpty()) {
                                staffData[1] = lineAsArray[i];
                            } else if (!staffData[1].isEmpty() && staffData[2].isEmpty()) {
                                staffData[2] = lineAsArray[i];
                            }
                        }
                        Staff s = new Staff(staffData[0].trim(), Integer.parseInt(staffData[1]));
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
                        String[] staffData = line2.split(" ");
                        Staff s = new Staff(staffData[0], Integer.parseInt(staffData[3]));
                        s.setStamina(Integer.parseInt(staffData[2]));
                        hr.addStaff(s, Float.parseFloat(staffData[1]));
                    }
                }
            }
        }
    }
}
