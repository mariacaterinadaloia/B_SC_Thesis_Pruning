package tesi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ParserOutPDDL{
    private static String domain;
    public ParserOutPDDL(String domain){
        ParserOutPDDL.domain = domain;
    }
    public static void writeProblemFile(String fileName, Problem problem) {
        File f;
        if((f= new File(fileName)).exists())
            f.delete();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writeHeader(writer, problem.getName());
            writeObjectsSection(writer, problem.getObjects());
            writeInitialStateSection(writer, problem.getInitialState());
            writeGoalStateSection(writer, problem.getGoalState());
            writer.write(")\n"); // Closing parenthesis for the problem definition
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHeader(BufferedWriter writer, String problemName) throws IOException {
        writer.write("(define (problem " + problemName + ")\n");
        writer.write("\t(:domain "+domain+")\n");
    }

    private static void writeObjectsSection(BufferedWriter writer, ArrayList<ObjectPDDL> objects) throws IOException {
        writer.write("  (:objects\n");
        for (ObjectPDDL object : objects) {
            String s = object.phrase();
            writer.write("    " + object.phrase() + "\n");
        }
        writer.write("  )\n");
    }

    private static void writeInitialStateSection(BufferedWriter writer, ArrayList<Fact<Predicate,ObjectPDDL>> initialState) throws IOException {
        writer.write("  (:init\n");
        for (Fact<Predicate,ObjectPDDL> fact: initialState) {
            writer.write("    " + fact.phrase() + "\n");
        }
        writer.write("  )\n");
    }

    private static void writeGoalStateSection(BufferedWriter writer, ArrayList<Fact<Predicate,ObjectPDDL>> goalState) throws IOException {
        writer.write("  (:goal\n \t (and");
        for (Fact<Predicate,ObjectPDDL> fact : goalState) {
            writer.write("    " + fact.phrase() + "\n");
        }
        writer.write("  ))\n");
    }

    public static void writeDomainFile(String fileName, Domain domain) {
            File f;
            if((f= new File(fileName)).exists())
                f.delete();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writedomainHeader(writer, domain.getName(), domain);
                writeTypesSection(writer, domain.getTypes());
                writePredicatesSection(writer, domain.getPredicates());
                writeActionsSection(writer, domain.getActions());
                writer.write(")\n"); // Closing parenthesis for the domain definition
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void writedomainHeader(BufferedWriter writer, String domainName, Domain domain) throws IOException {
            StringBuilder r = new StringBuilder();
            writer.write("(define (domain " + domainName + ")\n");
            for(String s : domain.getRequirements()){
                r.append(s).append(" ");
            }
            writer.write("\t(:requirements " + r +")\n");
        }

        private static void writeTypesSection(BufferedWriter writer, ArrayList<Type> types) throws IOException {
            writer.write("\t(:types\n");
            for (Type type : types) {
                if(!type.phrase().isEmpty()) {
                    writer.write("\t\t" + type.phrase() + "\n");
                }
            }
            writer.write("\t)\n");
        }

        private static void writePredicatesSection(BufferedWriter writer, ArrayList<Predicate> predicates) throws IOException {
            writer.write("\t(:predicates\n");
            for (Predicate predicate : predicates) {
                writer.write("\t\t" + predicate.phrase() + "\n");
            }
            writer.write("\t)\n");
        }

        private static void writeActionsSection(BufferedWriter writer, ArrayList<Action> actions) throws IOException {
            for (Action action : actions) {
                writer.write("\t \n"+ action.phrase() + "\n");
            }
        }

}

