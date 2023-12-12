package facilities.buildings;

public interface Building {
    public abstract int getLevel();

    public abstract int getMaxLevel();

    public abstract void increaseLevel(); // Used to increase a building's level (by one)

    public abstract int getUpgradeCost(); // Calculates the upgrade cost of a building based on a formula

    public abstract int getCapacity(); // Gets the current capacity of the building (which changes based on level)
}

