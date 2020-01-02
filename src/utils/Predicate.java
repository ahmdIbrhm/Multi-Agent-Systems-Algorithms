package utils;

import javafx.util.Pair;

import java.util.ArrayList;

public class Predicate {
    private String name;
    private int variable1;
    private int variable2;
    private int k12;
    private char operator;



//    private ArrayList<Pair<Integer,Integer>> tuples;
//
//
//    private int arity;
//    private int weight;
//    private int nbTuples;
//
//    private int defaultCost;
//
//    private char constraintType;


    public Predicate(String name, int variable1, int variable2, int k12, char operator) {
        this.name = name;
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.k12 = k12;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public int getVariable1() {
        return variable1;
    }

    public int getVariable2() {
        return variable2;
    }

    public int getK12() {
        return k12;
    }

    public char getOperator() {
        return operator;
    }
}
