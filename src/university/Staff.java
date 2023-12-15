package university;

import java.lang.Math;

public class Staff {
    String name;
    int skill;
    int yearsOfTeaching;
    int stamina;

    public Staff(String name, int skill) {
        this.name = name;
        this.skill = skill;
        this.yearsOfTeaching = 0;
        this.stamina = 100;
    }

    public String getName() { return this.name; }

    /**
     * Takes the number of students to instruct and uses it to calculate the stamina drain on the staff.
     */
    public int instruct(int numberOfStudents) {
        this.skill = (this.skill < 100) ? this.skill + 1 : this.skill;
        this.stamina = (int) (this.stamina - Math.ceil((double) numberOfStudents / (20 + this.skill)) * 20);
        return (100 * this.skill) / (100 + numberOfStudents);
    }

    /**
     * Uses a ternary operator to increase the stamina by 20, unless this would exceed 100, in which case the stamina
     * is set to 100.
     */
    public void replenishStamina() { this.stamina = (this.stamina <= 80) ? this.stamina + 20 : 100; }

    public void increaseYearsOfTeaching() { ++this.yearsOfTeaching; }

    public int getSkill() { return this.skill; }

    public int getYearsOfTeaching() { return this.yearsOfTeaching; }

    public int getStamina() { return this.stamina; }

    public void setStamina(int stam) { this.stamina = stam; }
}
