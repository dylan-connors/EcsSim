package facilities.buildings;

import facilities.Facility;

public class Theatre extends Facility implements Building {
    final int maxLevel = 6;
    final int baseCapacity = 10;
    final int baseBuildingCost = 200;
    int level;
    
    public Theatre(String name) {
        super(name);
        this.level = 1;
    }

    public int getLevel() { return this.level; }

    public int getMaxLevel() { return this.maxLevel; }

    public int getCapacity() { return (int) (this.baseCapacity * Math.pow(2, this.level - 1)); }

    public void increaseLevel() { this.level += 1; }

    public int getUpgradeCost() {
        if (this.level >= this.maxLevel) { // If it is at max level then upgrade cost is irrelevant
            return -1;
        } else {
            return this.baseBuildingCost * (this.level + 1);
        }
    }

    public int getBaseBuildingCost() {
        return this.baseBuildingCost;
    }
}
