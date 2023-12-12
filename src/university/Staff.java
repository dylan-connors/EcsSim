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

    public int instruct(int numberOfStudents) { /* Returns the calculated reputation gain, and the stamina loss for the
    staff member */
        this.skill = (this.skill < 100) ? this.skill + 1 : this.skill;
        this.stamina = (int) (this.stamina - Math.ceil((double) numberOfStudents / (20 + this.skill)) * 20);
        return (100 * this.skill) / (100 + numberOfStudents);
    }

    public void replenishStamina() { // Increases stamina by 20 as long as this would not exceed 100 stamina (the max)
        this.stamina = (this.stamina <= 80) ? this.stamina + 20 : this.stamina;
    }

    public void increaseYearsOfTeaching() {
        ++this.yearsOfTeaching;
    }

    public int getSkill() { return this.skill; }
}
