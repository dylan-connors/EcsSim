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

    public Facility build(String type, String name) {
        Facility facilityAdded = this.estate.addFacility(type, name);
        if (facilityAdded == null) {
            return null;
        } else {
            if (this.budget - ((Building) facilityAdded).getUpgradeCost() < 0) {
                this.budget += ((Building) facilityAdded).getUpgradeCost();
                this.estate.removeFacility(this.estate.getFacilities().length - 1);
                return null;
            } else {
                this.budget -= ((Building) facilityAdded).getUpgradeCost();
                this.reputation += 100;
            }
            return facilityAdded;
        }
    }

    public void upgrade(Building building) throws Exception {
        if (this.estate.facilities.contains(building)) {
            if (building.getLevel() < building.getMaxLevel()) {
                building.increaseLevel();
                this.budget -= building.getUpgradeCost();
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
