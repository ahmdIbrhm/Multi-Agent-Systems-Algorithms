import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import translator.InstanceTranslator;
import utils.Constraint;
import utils.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//            String fileVariables = "/home/ahmad/Documents/FullRLFAP/SUBCELAR6/CELAR6-SUB0/var.txt";
//            String fileDomains = "/home/ahmad/Documents/FullRLFAP/SUBCELAR6/CELAR6-SUB0/dom.txt";
//            String fileConstraints = "/home/ahmad/Documents/FullRLFAP/SUBCELAR6/CELAR6-SUB0/ctr.txt";
            String fileVariables="";
            String fileDomains="";
            String fileConstraints="";

            File directory=new File(args[0]);
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].getName().equals("var.txt"))
                {
                    fileVariables = files[i].getPath();
                }
                else if(files[i].getName().equals("dom.txt"))
                {
                    fileDomains = files[i].getPath();
                }
                else if (files[i].getName().equals("ctr.txt"))
                {
                    fileConstraints = files[i].getPath();
                }
            }

            if(!(fileVariables.equals("") && fileDomains.equals("") && fileConstraints.equals(""))) {
                InstanceTranslator translator = new InstanceTranslator(fileVariables, fileConstraints, fileDomains);

                ArrayList<Variable> variables = translator.parseVaribales();
                ArrayList<Domain> domains = translator.parseDomains();
                ArrayList<Constraint> constraints = translator.parseConstraintsAndPredicates().getConstraintArrayList();
                ArrayList<Predicate> predicatesArray = translator.parseConstraintsAndPredicates().getPredicateArrayList();

                writeXCSP(variables, domains, constraints, predicatesArray, directory.getName());
            }
            else
            {
                System.out.println("Error");
            }
    }
    public static void writeXCSP(ArrayList<Variable> variablesArray,ArrayList<Domain> domainsArray,ArrayList<Constraint> constraintsArray,ArrayList<Predicate> predicatesArray, String directory)
    {
        try
        {
            Element instance=new Element("instance");

            Document document = new Document(instance);
//            document.setRootElement(instance);
            Element presentation=new Element("presentation");
            presentation.setAttribute("name","sampleProblem");
            presentation.setAttribute("maxConstraintArity","2");
            presentation.setAttribute("maximize","false");
            presentation.setAttribute("format","XCSP 2.1_FRODO");
            document.getRootElement().addContent(presentation);


            ArrayList<Element> pairOfAgentsVariables=getAgentsAndVariables(variablesArray);
            Element agents=pairOfAgentsVariables.get(0);
            Element variables=pairOfAgentsVariables.get(1);
            Element domains=getDomains(domainsArray);
            Element constraints=getConstraints(constraintsArray);
            Element predicates=getPredicates(predicatesArray);

            document.getRootElement().addContent(agents);
            document.getRootElement().addContent(domains);
            document.getRootElement().addContent(variables);
            document.getRootElement().addContent(predicates);
            document.getRootElement().addContent(constraints);


            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            File outputFile=new File(System.getProperty("user.dir")+"/xcspInstances/"+directory+".xml");
            xmlOutput.output(document, new FileWriter(outputFile));
            System.out.println("File Saved!");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static ArrayList<Element> getAgentsAndVariables(ArrayList<Variable> variablesArray)
    {
        Element agents = new Element("agents");
        agents.setAttribute(new Attribute("nbAgents", String.valueOf(variablesArray.size())));

        Element variables=new Element("variables");
        variables.setAttribute("nbVariables",String.valueOf(variablesArray.size()));

        for (Variable variable: variablesArray)
        {
            Element agentElement=new Element("agent");
            agentElement.setAttribute("name","agent"+variable.getVariableNumber());
            agents.addContent(agentElement);

            Element variableElement=new Element("variable");
            variableElement.setAttribute("name","variable"+variable.getVariableNumber());
            variableElement.setAttribute("domain","domain"+variable.getDomain());
            variableElement.setAttribute("agent","agent"+variable.getVariableNumber());
            variables.addContent(variableElement);
        }
        ArrayList<Element> agentsAndVariables=new ArrayList<>();
        agentsAndVariables.add(agents);
        agentsAndVariables.add(variables);
        return agentsAndVariables;
    }

    public static Element getDomains(ArrayList<Domain> domainsArray)
    {
        Element domains=new Element("domains");
        domains.setAttribute(new Attribute("nbDomains",String.valueOf(domainsArray.size())));
        for(Domain domain:domainsArray)
        {
            Element domainElement=new Element("domain");
            domainElement.setAttribute("name",domain.getName());
            domainElement.setAttribute("nbValues",String.valueOf(domain.getNbValues()));
            String domainValuesString="";
            for(int domainValues: domain.getVariablesArrayList())
            {
                domainValuesString+=domainValues+" ";
            }
            domainElement.setText(domainValuesString);
            domains.addContent(domainElement);
        }
        return domains;
    }
    public static Element getConstraints(ArrayList<Constraint> constraintsArray)
    {
        Element constraints = new Element("constraints");
        constraints.setAttribute(new Attribute("nbConstraints", String.valueOf(constraintsArray.size())));
        for(Constraint constraint:constraintsArray)
        {
            Element constraintElement=new Element("constraint");
            constraintElement.setAttribute("name",constraint.toString());
            constraintElement.setAttribute("arity","2");
            constraintElement.setAttribute("scope",constraint.getVariable1()+" "+constraint.getVariable2());
            constraintElement.setAttribute("reference",constraint.getPredicateName());
            Element parametersElement=new Element("parameters");
            parametersElement.setText(constraint.getVariable1()+" "+constraint.getVariable2()+" "+constraint.getK12());
            constraintElement.addContent(parametersElement);
            constraints.addContent(constraintElement);
        }
        return constraints;
    }

    public static Element getPredicates(ArrayList<Predicate> predicatesArray)
    {
        Element predicates=new Element("predicates");
        predicates.setAttribute("nbPredicates",predicatesArray.size()+"");
        for(Predicate predicate:predicatesArray)
        {
            Element predicateElement=new Element("predicate");
            predicateElement.setAttribute("name",predicate.getName());

            Element parametersElement=new Element("parameters");
            parametersElement.setText("int V1 int V2 int K12");

            Element expressionElement=new Element("expression");
            Element functionalElement=new Element("functional");
            if(predicate.getOperator()=='=')
                functionalElement.setText("eq(abs(sub(V1, V2)),K12)");
            else if(predicate.getOperator()=='>')
                functionalElement.setText("gt(abs(sub(V1, V2)),K12)");
            expressionElement.addContent(functionalElement);
            predicateElement.addContent(parametersElement);
            predicateElement.addContent(expressionElement);

            predicates.addContent(predicateElement);
        }
        return predicates;
    }
}
