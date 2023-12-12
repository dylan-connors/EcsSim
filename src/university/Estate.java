package university;

import facilities.Facility;
import facilities.buildings.*;
import java.util.ArrayList;

public class Estate extends Facility {
    ArrayList<Facility> facilities;

    public Estate(String name) {
        super(name);
        this.facilities = new ArrayList<>();
    }

    public Facility[] getFacilities() { return this.facilities.toArray(new Facility[0]); }

    public Facility addFacility(String type, String name) { // Adds a new facility based on the type parameter
        switch (type) {
            case "Hall":
                this.facilities.add(new Hall(name));
                break;
            case "Lab":
                this.facilities.add(new Lab(name));
                break;
            case "Theatre":
                this.facilities.add(new Theatre(name));
                break;
        }
        return this.facilities.get(this.facilities.size() - 1);
    }

    public void removeFacility(int index) { this.facilities.remove(index); } // Removes a facility at a specified index

    public Float getMaintenanceCost() { // Calculates the cost of maintaining a facility for one year
        float cost = 0f;
        for (Facility i : this.facilities) {
            cost += ((Building) i).getCapacity() * 0.1f;
        }
        return cost;
    }

    public int getNumberOfStudents() { // Calculates the number of students in the estate based on the lowest collective capacity of each of the facility types
        int hallsCapacity = 0;
        int labsCapacity = 0;
        int theatresCapacity = 0;
        for (Facility i : this.facilities) {
            if (i instanceof Hall) {
                hallsCapacity += ((Hall) i).getCapacity();
            } else if (i instanceof Lab) {
                labsCapacity += ((Lab) i).getCapacity();
            } else if (i instanceof Theatre) {
                theatresCapacity += ((Theatre) i).getCapacity();
            }
        }
        return Math.min(Math.min(hallsCapacity, labsCapacity), theatresCapacity);
    }
}
