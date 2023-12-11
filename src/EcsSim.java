import university.*;
import facilities.buildings.*;
import facilities.Facility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

public class EcsSim {
    University university;
    Estate estate;
    ArrayList<Staff> staffMarket;
    Scanner scanner;
    int yearsElapsed;

    public EcsSim() {
        this.university = new University("Cool University", 20000);
        this.estate = this.university.getEstate();
        this.staffMarket = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.yearsElapsed = 0;

        this.university.build("Hall", "Auspices Hall");
        this.university.build("Theatre", "Plunkett Lecture Theatre");
        this.university.build("Lab", "Foundry Laboratory");
        // University is initialised with some buildings

        this.staffMarket.add(new Staff("Methuselah", 75));
        this.staffMarket.add(new Staff("Jared", 15));
        this.staffMarket.add(new Staff("Enoch", 21));
        this.staffMarket.add(new Staff("Cain", 4));
        this.staffMarket.add(new Staff("Abel", 5));
    }

    public void simulate() {
        try {
            System.out.printf("Dawn of year %d \n", this.yearsElapsed + 1);
            this.considerHalls();
            this.considerLabs();
            // this.considerTheatres();
            this.university.increaseBudget(10 * this.estate.getNumberOfStudents());
            this.yearsElapsed += 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void considerHalls() throws Exception {
        // If the halls capacity is the limiting factor for the number of students, then will attempt to upgrade or build.
        // Only build a new one if all existing halls are at max level.
        int hallsCapacity = 0;
        for (Facility i : this.estate.getFacilities()) {
            if (i instanceof Hall) {
                hallsCapacity += ((Hall) i).getCapacity();
            }
        }

        boolean hallUpgraded = false;
        if (hallsCapacity == this.estate.getNumberOfStudents()) {
            for (Facility i : this.estate.getFacilities()) {
                if (i instanceof Hall) {
                    if (((Hall) i).getLevel() < ((Hall) i).getMaxLevel()) {
                        this.university.upgrade((Hall) i);
                        hallUpgraded = true;
                        break;
                    }
                }
            }

            if (!hallUpgraded) {
                this.university.build("Hall", this.takeBuildingNameFromUser("Hall"));
            }
        }
    }

    private void considerLabs() throws Exception {
        // try and upgrade an existing lab. If they're all max level, build a new one. Labs are very expensive compared
        // to their capacity, so will EITHER be built or upgraded, never both.
        boolean labUpgraded = false;
        for (Facility i : this.estate.getFacilities()) {
            if (i instanceof Lab && ((Lab) i).getLevel() < ((Lab) i).getMaxLevel()) {
                this.university.upgrade((Building) i);
                labUpgraded = true;
                break;
            }
        }

        if (!labUpgraded) {
            this.university.build("Lab", this.takeBuildingNameFromUser("Lab"));
        }
    }

    private void considerTheatres() throws Exception {
        // Theatres will attempt to be upgraded yearly, but new ones will only attempt to be built every two years, as their base
        // capacity is so high.
        for (Facility i : this.estate.getFacilities()) {
            if (i instanceof Theatre && ((Theatre) i).getMaxLevel() > ((Theatre) i).getLevel() && this.university.getBudget() - ((Theatre) i).getUpgradeCost() > 0) {
                this.university.upgrade((Building) i);
                break;
            }
        }

        if (this.yearsElapsed % 2 != 0) {
            this.university.build("Theatre", this.takeBuildingNameFromUser("Theatre"));
        }
    }

    private String takeBuildingNameFromUser(String type) {
        System.out.printf("Name the new %s: ", type);
        return this.scanner.nextLine();
    }

}

