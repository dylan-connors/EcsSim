import university.University;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        EcsSim e = new EcsSim();
        e.simulate();
        e.simulate();
        e.simulate();
        e.simulate();
        e.simulate();
        System.out.println(Arrays.toString(e.estate.getFacilities()));
    }
}
