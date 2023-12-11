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

    public int instruct(int numberOfStudents) {
        this.skill = (this.skill < 100) ? this.skill + 1 : this.skill;
        this.stamina = (int) (this.stamina - Math.ceil((double) numberOfStudents / (20 + this.skill)) * 20);
        return (100 * this.skill) / (100 + numberOfStudents);
    }

    public void replenishStamina() {
        this.stamina = (this.stamina <= 80) ? this.stamina + 20 : this.stamina;
    }

    public void increaseYearsOfTeaching() {
        ++this.yearsOfTeaching;
    }
}
