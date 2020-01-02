package translator;


import utils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class InstanceTranslator {
    private String fileVariables;
    private String fileConstraints;
    private String fileDomains;

    public InstanceTranslator(String fileVariables, String fileConstraints, String fileDomains) {
        this.fileVariables = fileVariables;
        this.fileConstraints = fileConstraints;
        this.fileDomains = fileDomains;
    }

    public ArrayList<Variable> parseVaribales(){
        ArrayList variables = new ArrayList();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileVariables)));
            String line = "";
            while ( (line = reader.readLine()) != null ){
                String values[] = line.trim().split("\\s+");
                int variableNumber=Integer.parseInt(values[0]);
                int domainNumber=Integer.parseInt(values[1]);
                variables.add(new Variable(variableNumber,domainNumber));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variables;
    }
    public ArrayList<Domain> parseDomains()
    {
        ArrayList domains = new ArrayList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileDomains)));
            String line = "";
            while ( (line = reader.readLine()) != null )
            {
                String values[] = line.trim().split("\\s+");
                int domainNumber=Integer.parseInt(values[0]);
                int domainCardinalty=Integer.parseInt(values[1]);
                ArrayList<Integer> valuesOfDomain=new ArrayList<>();
                for (int i=0;i<domainCardinalty;i++)
                {
                    valuesOfDomain.add(Integer.parseInt(values[i+2]));
                }
                domains.add(new Domain(domainNumber,valuesOfDomain));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domains;
    }
    public ConstraintAndPredicate parseConstraintsAndPredicates()
    {
        ArrayList<Constraint> constraints=new ArrayList<>();
        ArrayList<Predicate> predicates=new ArrayList<>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileConstraints)));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                String values[] = line.trim().split("\\s+");
                int varaible1 = Integer.parseInt(values[0]);
                int varaible2 = Integer.parseInt(values[1]);
//                char type=values[2].charAt(0);
                char operator=values[3].charAt(0);
                int k12=Integer.parseInt(values[4]);
//                int weight=0;
//                if(values.length==6)
//                    weight=Integer.parseInt(values[5]);

                String predicateName=varaible1+" "+operator+" "+varaible2;
                constraints.add(new Constraint("variable"+varaible1,"variable"+varaible2,predicateName,k12));
                predicates.add(new Predicate(predicateName,varaible1,varaible2,k12,operator));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ConstraintAndPredicate(constraints,predicates);
    }
    public class ConstraintAndPredicate {
        ArrayList<Constraint> constraintArrayList;
        ArrayList<Predicate> predicateArrayList;

        public ConstraintAndPredicate(ArrayList<Constraint> constraintArrayList,ArrayList<Predicate> predicateArrayList)
        {
            this.constraintArrayList=constraintArrayList;
            this.predicateArrayList=predicateArrayList;
        }

        public ArrayList<Constraint> getConstraintArrayList() {
            return constraintArrayList;
        }

        public ArrayList<Predicate> getPredicateArrayList() {
            return predicateArrayList;
        }
    }
}
