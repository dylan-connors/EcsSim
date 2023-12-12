package university;

import facilities.Facility;
import facilities.buildings.*;

public class University {
    Estate estate;
    HumanResource humanResource;
    float budget;
    int reputation;

    public University(String name, int funding) {
        this.budget = funding;
        this.estate = new Estate(name);
        this.humanResource = new HumanResource();
    }

    public Estate getEstate() { return this.estate; }

    public void payEstate() {
        this.budget -= this.estate.getMaintenanceCost();
    }

    public void payStaffSalary() {
        this.budget -= this.humanResource.getTotalSalary();
    }

    public Facility build(String type, String name) { // Adjusts the university's budget to pay for new buildings, and adjusts reputation
        Facility facilityAdded = this.estate.addFacility(type, name);
        if (facilityAdded == null) {
            return null;
        } else {
            if (this.budget - ((Building) facilityAdded).getBaseBuildingCost() < 0) {
                this.budget += ((Building) facilityAdded).getBaseBuildingCost();
                this.estate.removeFacility(this.estate.getFacilities().length - 1);
                return null;
            } else {
                this.budget -= ((Building) facilityAdded).getBaseBuildingCost();
                this.reputation += 100;
            }
            return facilityAdded;
        }
    }

    public void upgrade(Building building) throws Exception { // Increases the level of a specified building
        if (this.estate.facilities.contains(building)) {
            if (building.getLevel() < building.getMaxLevel() && this.budget - building.getUpgradeCost() > 0) {
                this.budget -= building.getUpgradeCost();
                building.increaseLevel();
                System.out.println();
                this.reputation += 50;
            } else {
                throw new Exception("Building cannot be upgraded past its maximum level.");
            }
        } else {
            throw new Exception("Building not part of university.");
        }
    }

    public float getBudget() {
        return this.budget;
    }

    public void increaseBudget(float increase) {
        this.budget += increase;
    }

    public int getReputation() {
        return this.reputation;
    }
}
