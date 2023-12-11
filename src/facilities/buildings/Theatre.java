package facilities.buildings;

import facilities.Facility;

public class Theatre extends Facility implements Building {
    final int maxLevel = 6;
    final int baseCapacity = 10;
    final int baseBuildingCost = 200;
    int currentLevel;
    
    public Theatre(String name) {
        super(name);
        this.currentLevel = 1;
    }

    public int getLevel() { return this.currentLevel; }

    public int getMaxLevel() { return this.maxLevel; }

    public int getCapacity() { return (int) (this.baseCapacity * Math.pow(2, this.currentLevel - 1)); }

    public void increaseLevel() { this.currentLevel += 1; }

    public int getUpgradeCost() {
        if (this.currentLevel >= this.maxLevel) {
            return -1;
        } else {
            return this.baseBuildingCost * (this.currentLevel + 1);
        }
    }
}
