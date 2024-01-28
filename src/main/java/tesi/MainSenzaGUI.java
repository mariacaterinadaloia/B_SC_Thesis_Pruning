package tesi;

import java.io.File;
import java.io.IOException;

public class MainSenzaGUI {
    public static void main(String[] args) throws IOException {
        //inizia a chiamare il parser
        //fa le regole sul file PDDL
        //richiama il parser per creare il nuovo file con pruning effettuato
        String domainFile;
        String problemFile;
        if (args.length == 2) {
            domainFile = args[0];
            problemFile = args[1];
        } else {
            domainFile = //"C:\\Users\\maria\\Desktop\\esempi\\esempio3\\domainAgricola.pddl";
                    "src/test/java/test/domain.pddl";
            problemFile = //"C:\\Users\\maria\\Desktop\\esempi\\esempio3\\p01.pddl";
                    "src/test/java/test/problem.pddl";
        }

        try {
            ParserInPDDL parser = new ParserInPDDL();
            Domain domain = parser.parseDomain(domainFile);
            Problem problem = ParserInPDDL.parseProblem(problemFile);
            int initActions = domain.getActions().size();
            int Predicates = domain.getPredicates().size();

            System.out.println("Azioni iniziali:"+domain.getActions().size());
            System.out.println("Predicati iniziali:"+domain.getPredicates().size());
            IntermediateModel<Action, ObjectPDDL, Predicate> model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain.getActions(), problem.getObjects2(), domain.getPredicates(), problem.getInitialState(), problem.getGoalState());
            Resolution<Predicate,ObjectPDDL, Action> resolution = new Resolution<Predicate,ObjectPDDL, Action>(model);
            resolution.init();

            System.out.println("-------------------------");

            int finalActions = domain.getActions().size();
            int finalPredicates = domain.getPredicates().size();

            System.out.println("Azioni rimosse:"+(initActions-finalActions));
            System.out.println("Predicati rimossi:"+(Predicates-finalPredicates));
            ParserOutPDDL parserOut = new ParserOutPDDL(domain.getName());
            ParserOutPDDL.writeProblemFile("final"+problem.getName()+".pddl", problem);
            ParserOutPDDL.writeDomainFile("final"+domain.getName()+".pddl", domain);
            System.out.println("-------------------------");
            System.out.println("File di output scritti con successo," + "problem:"+ "final"+problem.getName()+".pddl e domain:"+"final"+domain.getName()+".pddl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}