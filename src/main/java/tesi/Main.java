package tesi;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //inizia a chiamare il parser
        //fa le regole sul file PDDL
        //richiama il parser per creare il nuovo file con pruning effettuato
        String domainFile = "C:\\Users\\maria\\Documents\\GitHub\\Euristiche_Di_Pruning\\src\\Tesi\\domainEx.pddl";
        String problemFile = "C:\\Users\\maria\\Documents\\GitHub\\Euristiche_Di_Pruning\\src\\Tesi\\pEx.pddl";
        //GUITesi gui = new GUITesi();
        try {
            ParserInPDDL parser = new ParserInPDDL();
            Domain domain = parser.parseDomain(domainFile);
            Problem problem = ParserInPDDL.parseProblem(problemFile);

            IntermediateModel<Action, ObjectPDDL, Predicate> model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain.getActions(), problem.getObjects(), domain.getPredicates(), problem.getInitialState(), problem.getGoalState());
            Resolution<Predicate,ObjectPDDL, Action> resolution = new Resolution<Predicate,ObjectPDDL, Action>(model);
            resolution.init();

            ParserOutPDDL parserOut = new ParserOutPDDL(domain.getName());
            ParserOutPDDL.writeProblemFile("finalProblem.pddl", problem);
            ParserOutPDDL.writeDomainFile("finalDomain.pddl", domain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}