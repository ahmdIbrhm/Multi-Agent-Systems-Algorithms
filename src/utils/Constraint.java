package utils;

public class Constraint {
    private String variable1;
    private String variable2;
    private String predicateName;
    private int k12;


    public Constraint(String variable1, String variable2,String predicateName,int k12) {
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.predicateName=predicateName;
        this.k12=k12;
    }

    @Override
    public String toString() {
        return variable1+" & " + variable2;
    }

    public String getVariable1() {
        return variable1;
    }

    public String getVariable2() {
        return variable2;
    }

    public String getPredicateName() {
        return predicateName;
    }

    public int getK12() {
        return k12;
    }
}
