package facilities.buildings;

import facilities.Facility;

public class Hall extends Facility implements Building {
    final int maxLevel = 4;
    final int baseCapacity = 6;
    final int baseBuildingCost = 100;
    int currentLevel;

    public Hall(String name) {
        super(name);
        this.currentLevel = 1;
    }

    public int getLevel() { return this.currentLevel; }

    public int getMaxLevel() { return this.maxLevel; }

    public int getCapacity() { return (int) (this.baseCapacity * Math.pow(2, this.currentLevel - 1)); }

    public int getBaseBuildingCost() {
        return this.baseBuildingCost;
    }

    public void increaseLevel() { this.currentLevel += 1; }

    public int getUpgradeCost() {
        if (this.currentLevel >= this.maxLevel) { // If it is at max level then upgrade cost is irrelevant
            return -1;
        } else {
            return this.baseBuildingCost * (this.currentLevel + 1);
        }
    }
}
