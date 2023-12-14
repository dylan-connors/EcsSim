package university;

import facilities.Facility;
import facilities.buildings.*;

public class University {
    Estate estate;
    HumanResource humanResource;
    float budget;
    int reputation;

    public University(int funding) {
        this.budget = funding;
        this.estate = new Estate();
        this.humanResource = new HumanResource();
    }

    public Estate getEstate() { return this.estate; }

    public void payEstate() {
        this.budget -= this.estate.getMaintenanceCost();
    }

    public void payStaffSalary() {
        this.budget -= this.humanResource.getTotalSalary();
    }

    /**
     * Builds a new building by adding a facility to the estate based on the type specified in the params. This change
     * only persists if the budget permits. If build would make the budget negative, the facility is removed from the
     * estate. The facility object created is returned.
     */
    public Facility build(String type, String name) {
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

    /** Increases the level of the building specified in the params so long as it is not already at max level, and doing
     * so wouldn't put the budget in the negative. Throws an exception if either is the case.
     */
    public void upgrade(Building building) throws Exception { // Increases the level of a specified building
        if (this.estate.facilities.contains(building)) {
            if (building.getLevel() < building.getMaxLevel()) {
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

    public float getBudget() { return this.budget; }

    public void increaseBudget(float increase) { this.budget += increase; }

    public int getReputation() { return this.reputation; }
}
