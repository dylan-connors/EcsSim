package university;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class HumanResource {
    HashMap<Staff, Float> staffSalary;

    public HumanResource() {
        this.staffSalary = new HashMap<>();
    }

    /**
     * Uses threadLocalRandom to generate a random number to calculate the salary in the given range.
     */
    public void addStaff(Staff staff) {
        int random = ThreadLocalRandom.current().nextInt(95, 106);
        float salary = (float) (random / 10) * staff.getSkill();
        this.staffSalary.put(staff, salary);
    }

    public void addStaff(Staff staff, float salary) {
        this.staffSalary.put(staff, salary);
    }

    public void removeStaff(Staff staff) {
        System.out.printf("Goodbye %s! \n", staff.name);
        this.staffSalary.remove(staff);
    }

    public Iterator<Staff> getStaff() { return this.staffSalary.keySet().iterator(); }

    public float getStaffSalary(Staff staff) {
        return this.staffSalary.get(staff);
    }

    /**
     * Uses a while loop to add up the salaries of each staff employed at the uni.
     */
    public float getTotalSalary() { // Calculates how much salary has to be paid for all the staff
        Iterator<Staff> it = this.getStaff();
        float totalSalary = 0f;
        while (it.hasNext()) {
            totalSalary += this.staffSalary.get(it.next());
        }
        return totalSalary;
    }
}
