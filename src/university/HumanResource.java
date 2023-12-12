package university;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.Math;

public class HumanResource {
    HashMap<Staff, Float> staffSalary;

    public HumanResource() {
        this.staffSalary = new HashMap<>();
    }

    public void addStaff(Staff staff) { // Adds a new staff member and randomly assigns a salary between a range
        double random = ((Math.random() * 11) + 95);
        float salary = (float) random / 10;
        this.staffSalary.put(staff, salary);
    }

    public void removeStaff(Staff staff) {
        System.out.printf("Goodbye %s! \n", staff.name);
        this.staffSalary.remove(staff);
    }

    public Iterator<Staff> getStaff() { return this.staffSalary.keySet().iterator(); }

    public float getTotalSalary() { // Calculates how much salary has to be paid for all the staff
        Iterator<Staff> it = this.getStaff();
        float totalSalary = 0f;
        while (it.hasNext()) {
            totalSalary += this.staffSalary.get(it.next());
        }
        return totalSalary;
    }
}
