import university.*;
import facilities.buildings.*;
import facilities.Facility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EcsSim {
    University university;
    Estate estate;
    HumanResource hr; // hr is a well known acronym for human resource
    ArrayList<Staff> staffMarket;
    Scanner scanner;
    int yearsElapsed;

    public EcsSim(float funding, ArrayList<Staff> staffList, boolean loadFlag) {
        this.university = new University(funding);
        this.estate = this.university.getEstate();
        this.hr = new HumanResource();
        this.staffMarket = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.yearsElapsed = 0;

        if (!loadFlag) {
            this.university.build("Hall", "Auspices Hall");
            this.university.build("Theatre", "Plunkett Lecture Theatre");
            this.university.build("Lab", "Foundry Laboratory");
            // University is initialised with some buildings
        }

        this.staffMarket = new ArrayList<>();
        staffMarket.addAll(staffList);
    }

    /**
     * Uses a switch case statement to determine if a new simulation should be started, or an existing one should be
     * loaded, based on the number of arguments.
     */
    public static void main(String[] args) throws FileNotFoundException {
        EcsSim ecsSim;
        switch (args.length) {
            case(3):
                Setup setup = new Setup(args[0], args[1], args[2]);
                ecsSim = new EcsSim(setup.getStartingBudget(), setup.extractStaffFromFile(), false);
                ecsSim.simulate(setup.getYearsToSimulate());
            case(2):
                SimulationLoader simLoader = new SimulationLoader(args[0]);
                ecsSim = new EcsSim(simLoader.extractBudget(), simLoader.extractStaffMarket(), true);
                simLoader.extractFacilities(ecsSim.estate);
                ecsSim.setYearsElapsed(simLoader.extractStartingYear());
                simLoader.extractEmployedStaff(ecsSim.hr);
                ecsSim.simulate(Integer.parseInt(args[1]));
        }

    }

    private void setYearsElapsed(int years) { this.yearsElapsed = years; }

    public void simulate() {
        try {
            Thread.sleep(500);

            System.out.printf("Dawn of year %d \n", this.yearsElapsed + 1);

            this.considerHalls();
            this.considerLabs();
            this.considerTheatres();

            this.university.increaseBudget(10 * this.estate.getNumberOfStudents());

            this.hireStaff();
            this.allocateStaff();

            this.university.payEstate();
            this.university.payStaffSalary();
            this.increaseStaffYearsOfEmployment();
            this.recuseStaff();
            this.replenishStaffStamina();

            this.yearsElapsed += 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void simulate(int years) {
        for (int i = 1; i <= years; i++) {
            this.simulate();
        }
        this.endMenu();
    }

    /**
     * Displays a menu once the simulation is over. Uses the scanner class to take an input from the user, then uses a
     * switch statement to execute their choice.
     */
    private void endMenu() {
        System.out.println("[1] Quit");
        System.out.println("[2] Save & Quit");
        System.out.print("::");
        int menuChoice;
        while (true) {
            menuChoice = scanner.nextInt();
            try {
                if (menuChoice == 1 || menuChoice == 2) {
                    break;
                } else {
                    System.out.println("Not an option");
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        switch (menuChoice) {
            case(1):
                System.out.println("Easy come, easy go...");
                System.exit(0);
            case(2):
                this.saveState();
                System.out.println("You're gonna carry that weight.");
                System.exit(0);
        }
    }

    /**
     * Executes methods related to saving the state of the simulated university.
     */
    private void saveState() {
        File file = this.createSaveFile();
        writeSaveDataToFile(file);
    }

    /**
     * Uses the File class to create a new file which the save data will be written to. Uses a while loop to ensure that
     * the save file has a unique name, in the format save(number).txt. Returns the File object created.
     */
    private File createSaveFile() {
        int fileNum = 1;
        try {
            while (true) {
                File file = new File(String.format("src/saves/save%d.txt", fileNum));
                if (file.createNewFile()) {
                    System.out.printf("Saved as save%d.txt%n", fileNum);
                    return file;
                } else {
                    fileNum++;
                }
            }
        } catch(IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Uses the fileWriter class to write save data to the file specified in the params. Writes years elapsed, the
     * currently built facilities, the current staff in the staff market, and the currently employed staff.
     */
    private void writeSaveDataToFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);

            writer.write(String.format("year:%n%d%n", this.yearsElapsed));

            writer.write(String.format("budget:%n%f%n", this.university.getBudget()));

            writer.write("facilities:\n");
            for (Facility i : this.estate.getFacilities()) {
                writer.write(String.format("%s:%s:%d%n",
                        i.getClass().getSimpleName(),
                        i.getName(),
                        ((Building) i).getLevel()));
            }

            writer.write("staff market:\n");
            for (Staff i: this.staffMarket) {
                writer.write(String.format("%s:%d:%d%n", i.getName(), i.getSkill(), i.getStamina()));
            }

            writer.write("staff:\n");
            Iterator<Staff> it = this.hr.getStaff();
            Staff s;
            while (it.hasNext()) {
                s = it.next();
                writer.write(String.format("%s:%f:%d:%d%n", s.getName(),
                        this.hr.getStaffSalary(s),
                        s.getStamina(),
                        s.getSkill()));
            }

            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Builds and upgrades halls when they become the limiting factor for capacity. Uses a for each loop to calculate
     * the capacity provided by the halls, then compares this to the number of students. If these are equal, then halls
     * are currently limiting the number of students, so should be upgraded/ built. Will attempt to upgrade an existing
     * halls, only attempting to build new ones if all current halls are max level.
     */
    private void considerHalls() throws Exception {
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

    /**
     * Attempts to upgrade one lab yearly, and will build a new one if all are max level. If the upgrade fails because
     * it cannot be afforded, no new lab is built (even if it could be afforded) to save coin.
     */
    private void considerLabs() throws Exception {
        boolean labUpgradedAttempted = false;
        for (Facility i : this.estate.getFacilities()) {
            if (i instanceof Lab && ((Lab) i).getLevel() < ((Lab) i).getMaxLevel()) {
                labUpgradedAttempted = true;
                if (this.university.getBudget() - ((Lab) i).getUpgradeCost() > 0) {
                    this.university.upgrade((Building) i);
                    break;
                }
            }
        }

        if (!labUpgradedAttempted) {
            this.university.build("Lab", this.takeBuildingNameFromUser("Lab"));
        }
    }

    /** Theatres have a high base capacity, so are built at a lower rate than the other two facilities. Every two years,
     * there is a 50% chance for a new theatre to be built (budget permitting). One existing theatre will attempt to be
     * upgraded yearly.
     */
    private void considerTheatres() throws Exception {
        for (Facility i : this.estate.getFacilities()) {
            if (i instanceof Theatre && ((Theatre) i).getMaxLevel() > ((Theatre) i).getLevel() && this.university.getBudget() - ((Theatre) i).getUpgradeCost() > 0) {
                this.university.upgrade((Building) i);
                break;
            }
        }

        if (this.yearsElapsed % 2 != 0 && ThreadLocalRandom.current().nextInt(2) == 0) {
            this.university.build("Theatre", this.takeBuildingNameFromUser("Theatre"));
        }
    }

    /**
     * Takes the building type as a param and prints out a message based on this, then uses the scanner class to take
     * the user's input. A string of the user's input is returned. */
    private String takeBuildingNameFromUser(String type) {
        System.out.printf("Name the new %s: ", type);
        return this.scanner.nextLine();
    }

    /**
     * Attempts to hire one staff per year, only failing if there is no budget for it. This is calculated based on the
     * max possible salary for that staff member (10.5 * skill). The staff member with the highest skill level that
     * can be afforded that year is hired. The List.sort method and the Comparator class are used to sort the staff
     * market by skill, then a for each loop is used to find the most skilled staff that can be afforded.
     * Staff are removed from staff market once hired.
     */
    private void hireStaff() {
        this.staffMarket.sort(Comparator.comparingInt(Staff::getSkill).reversed());
        for (Staff s : this.staffMarket) {
            if ((this.university.getBudget() - (this.hr.getTotalSalary() + 10.5 * s.getSkill())) > 0) {
                this.hr.addStaff(s);
                this.staffMarket.remove(s);
                break;
            }
        }
    }

    /**
     * Higher skilled staff teach larger classes to manage stamina loss. The staff in the estate are sorted by skill.
     * The students are split into groups of descending size, and then allocated to staff based on skill level.
     */
    private void allocateStaff() {
        ArrayList<Staff> staffBySkillLvl = new ArrayList<>();
        Iterator<Staff> it = this.hr.getStaff();
        int uninstructedStudents = this.estate.getNumberOfStudents();
        while (it.hasNext()) {
            Staff s = it.next();
            staffBySkillLvl.add(s);
        }
        staffBySkillLvl.sort(Comparator.comparingInt(Staff::getSkill).reversed());

        ArrayList<Integer> groupSizes = new ArrayList<>();
        for (int i = 0; i < staffBySkillLvl.size(); i++) {
            if (i + 1 == staffBySkillLvl.size()) {
                groupSizes.add(uninstructedStudents);
            } else {
                groupSizes.add(Math.floorDiv(uninstructedStudents, 2));
                uninstructedStudents -= Math.floorDiv(uninstructedStudents, 2);
            }
        }
        groupSizes.sort(Collections.reverseOrder());

        for (int i = 0; i < staffBySkillLvl.size(); i++) {
            Staff s = staffBySkillLvl.get(i);
            s.instruct(groupSizes.get(i));
        }
    }

    /**
     * Uses the Iterator from the human resource class to iterate over the staff objects and apply the
     * increaseYearsOfTeaching method to them.
     */
    private void increaseStaffYearsOfEmployment() { // Increases the years of employment for all currently employed staff
        Iterator<Staff> it = this.hr.getStaff();
        while (it.hasNext()) {
            it.next().increaseYearsOfTeaching();
        }
    }

    /** Handles the removal of staff at the end of each year. Uses threadLocalRandom to remove staff with a stamina%
     * chance
     */
    private void recuseStaff() {
        Iterator<Staff> it = this.hr.getStaff();
        while (it.hasNext()) {
            Staff s = it.next();
            if (s.getYearsOfTeaching() == 30) {
                it.remove();
            } else {
                if (ThreadLocalRandom.current().nextInt(101) > s.getStamina()) {
                    it.remove();
                    // If the staff leaves because of stamina, they rejoin the staff market and have a little rest
                    s.replenishStamina();
                    this.staffMarket.add(s);
                }
            }
        }

    }

    /** Uses the staff iterator to iterate over the staff and replenish their stamina with the replenishStamina method
     */
    private void replenishStaffStamina() {
        Iterator<Staff> it = this.hr.getStaff();
        while (it.hasNext()) {
            it.next().replenishStamina();
        }
    }
}

