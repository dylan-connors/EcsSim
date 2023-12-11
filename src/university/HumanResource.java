package university;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.Math;

public class HumanResource {
    HashMap<Staff, Float> staffSalary;

    public HumanResource() {
        this.staffSalary = new HashMap<>();
    }

    public void addStaff(Staff staff) {
        double random = ((Math.random() * 11) + 95);
        float salary = (float) random / 10;
        this.staffSalary.put(staff, salary);
    }

    public Iterator<Staff> getStaff() {
        return this.staffSalary.keySet().iterator();
    }

    public float getTotalSalary() {
        Iterator<Staff> it = this.getStaff();
        float totalSalary = 0f;
        while (it.hasNext()) {
            totalSalary += this.staffSalary.get(it.next());
        }
        return totalSalary;
    }
}
