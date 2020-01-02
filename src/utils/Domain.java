package utils;
import java.util.ArrayList;


public class Domain {
    int id;
    ArrayList<Integer> variablesArrayList;
    int nbValues;
    public Domain(int id,ArrayList<Integer> variablesArrayList)
    {
        this.id=id;
        this.variablesArrayList=variablesArrayList;
        this.nbValues=variablesArrayList.size();
    }

    @Override
    public String toString() {
        return id+" "+variablesArrayList+ " Size: "+nbValues+"\n";
    }

    public String getName() {
        return "domain"+id;
    }

    public ArrayList<Integer> getVariablesArrayList() {
        return variablesArrayList;
    }

    public int getNbValues() {
        return nbValues;
    }
}
