public class Setup {
    String fileName;
    int startingBudget;
    int yearsToSimulate;

    public Setup(String name, String budget, String years) {
        this.fileName = name;
        this.startingBudget = Integer.parseInt(budget);
        this.yearsToSimulate = Integer.parseInt(years);
    }
}
