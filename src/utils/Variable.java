package utils;

public class Variable {
    private int domain;
    private int variableNumber;


    public Variable(int variableNumber,int domain) {
        this.domain = domain;
        this.variableNumber = variableNumber;
    }

    public int getDomain() {
        return domain;
    }

    public int getVariableNumber() {
        return variableNumber;
    }

    @Override
    public String toString() {
        return "var: "+variableNumber+" dom: "+domain;
    }
}
