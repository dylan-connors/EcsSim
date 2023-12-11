package facilities.buildings;

public interface Building {
    public abstract int getLevel();

    public abstract int getMaxLevel();

    public abstract void increaseLevel();

    public abstract int getUpgradeCost();

    public abstract int getCapacity();
}

