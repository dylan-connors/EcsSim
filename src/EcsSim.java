import university.*;
import facilities.buildings.*;
import facilities.Facility;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EcsSim {
    University university;
    Estate estate;
    HumanResource hr; // hr is a well known acronym for human resource
    ArrayList<Staff> staffMarket;
    Scanner scanner;
    int yearsElapsed;

    public EcsSim(int funding, ArrayList<Staff> staffList) {
        this.university = new University("Cool University", funding);
        this.estate = this.university.getEstate();
        this.hr = new HumanResource();
        this.staffMarket = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.yearsElapsed = 0;

        this.university.build("Hall", "Auspices Hall");
        this.university.build("Theatre", "Plunkett Lecture Theatre");
        this.university.build("Lab", "Foundry Laboratory");
        // University is initialised with some buildings

        this.staffMarket = new ArrayList<>();
        staffMarket.addAll(staffList);
    }

    public static void main(String[] args) {
        Setup setup = new Setup(args[0], args[1], args[2]);
        EcsSim ecsSim = new EcsSim(setup.getStartingBudget(), setup.extractStaffFromFile());
        ecsSim.simulate(setup.yearsToSimulate);
    }

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
            this.waxStaff();
            this.recuseStaff();
            this.replenishStaffStamina();

            this.yearsElapsed += 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulate(int years) {
        for (int i = 1; i <= years; i++) {
            this.simulate();
        }
        System.out.println("Simulation ended!");
    }

    private void considerHalls() throws Exception { /* If the halls capacity is the limiting factor for the number of
    students, then will attempt to upgrade or build. Only build a new one if all existing halls are at max level. */
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

    private void considerLabs() throws Exception { /* try and upgrade an existing lab. If they're all max level, build
    a new one. Labs are very expensive compared to their capacity, so will EITHER be built or upgraded, never both. Only
    builds a new lab if no lab was upgraded because they were all max level. If a lab failed to upgrade because it could
    not be afforded, then no new lab is built to save some coin. */
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

    private void considerTheatres() throws Exception { /* Theatres will attempt to be upgraded yearly, but new ones will
    only attempt to be built every two years, 50% of the time, as their base capacity is so high. */
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

    private String takeBuildingNameFromUser(String type) { /* Prints out a specific request based on the building type
    in order to get the user specified name */
        System.out.printf("Name the new %s: ", type);
        return this.scanner.nextLine();
    }

    private void hireStaff() { /* The more staff the better. Attempt to hire one new staff per year, only failing if
    there's no money for it. Should attempt to add the staff with the highest skill level that can be afforded
    (using 10.5% to ensure that they can be afforded) */
        this.staffMarket.sort(Comparator.comparingInt(Staff::getSkill).reversed()); /* Sorts staffMarket from the
        highest skill to lowest */
        for (Staff s : this.staffMarket) {
            if ((this.university.getBudget() - (this.hr.getTotalSalary() + 10.5 * s.getSkill())) > 0) {
                this.hr.addStaff(s);
                this.staffMarket.remove(s);
                break;
            }
        }
    }

    private void allocateStaff() { /* Allocates class sizes based on staff skill level, with higher skill levels
    teaching larger groups. */
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

    private void waxStaff() { // Increases the years of employment for all currently employed staff
        Iterator<Staff> it = this.hr.getStaff();
        while (it.hasNext()) {
            it.next().increaseYearsOfTeaching();
        }
    }

    private void recuseStaff() { // Determines which staff leave the uni at the end of the year.
        Iterator<Staff> it = this.hr.getStaff();
        while (it.hasNext()) {
            Staff s = it.next();
            if (s.getYearsOfTeaching() == 30) {
                this.hr.removeStaff(s);
            } else {
                if (ThreadLocalRandom.current().nextInt(101) > s.getStamina()) {
                    this.hr.removeStaff(s);
                    // If the staff leaves because of stamina, they rejoin the staff market and have a little rest
                    s.replenishStamina();
                    this.staffMarket.add(s);
                }
            }
        }

    }

    private void replenishStaffStamina() {
        Iterator<Staff> it = this.hr.getStaff();
        while (it.hasNext()) {
            it.next().replenishStamina();
        }
    }

}

