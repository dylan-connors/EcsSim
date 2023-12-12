package facilities.buildings;

import facilities.Facility;

public class Lab extends Facility implements Building {
    final int maxLevel = 5;
    final int baseCapacity = 5;
    final int baseBuildingCost = 300;
    int currentLevel;

    public Lab(String name) {
        super(name);
        this.currentLevel = 1;
    }

    public int getLevel() { return this.currentLevel; }

    public int getMaxLevel() { return this.maxLevel; }

    public int getCapacity() { return (int) (this.baseCapacity * Math.pow(2, this.currentLevel - 1)); }

    public void increaseLevel() { this.currentLevel += 1; }

    public int getUpgradeCost() {
        if (this.currentLevel >= this.maxLevel) { // If it is at max level then upgrade cost is irrelevant
            return -1;
        } else {
            return this.baseBuildingCost * (this.currentLevel + 1);
        }
    }
}
