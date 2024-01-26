package tesi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ParserInPDDL {
    private static String[] SUPPORTED_REQUIREMENTS = {":strips", ":negative-preconditions", ":typing", ":action-costs"};
    //basato sul PDDL Parser in Python <https://github.com/pucrs-automated-planning/pddl-parser>.
    private static final Domain domain = new Domain();
    private static final Problem problem = new Problem();
        public Domain parseDomain(String filename) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("(define (domain")) {
                    domain.setName(line.trim().split(" ")[2].replace(")",""));

                    parseFileDomain(filename);
                }
            }

            reader.close();
            return domain;
        }


    public static void parseFileDomain(String domainFileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(domainFileName));
            String line;
            ArrayList<Predicate> predicates = new ArrayList<>();
            ArrayList<Type> types = new ArrayList<>();
            ArrayList<String> requirements = new ArrayList<>();
            ArrayList<Action> actions = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                // Parse requirements
                if (line.contains("(:requirements")&&line!=null) {

                        String[] typeParts = line.split("\\s+");
                        for (String s : SUPPORTED_REQUIREMENTS) {
                            for (String a : typeParts) {
                                if(a.contains(")")) {
                                    a=a.replace(")", "");
                                }
                                if (!requirements.contains(s) && a.equals(s))
                                    requirements.add(s);

                            }
                        }
                    }
                domain.setRequirements(requirements);


                // Parse types
                if (line.contains("(:types")) {
                    types.add(new Type("object", null));
                    while (!(line = br.readLine()).contains(")")){
                        String[] typeParts = line.split("-");

                        Type type = new Type(typeParts[0].trim(), typeParts.length > 1 ? typeParts[1] : "object");
                        types.add(type);
                    }
                    domain.setTypes(types);
                }

                // Skip comments
                if (line.contains(";") || line.isEmpty()) {
                    continue;
                }

                // Parse predicates
                if (line.contains("(:predicates")) {
                    while (null != (line = br.readLine())){
                        if(line.contains("(")&&line.contains(")")) {
                            String[] parts = line.trim().split("\\s+");
                            String predicateName = parts[0].replaceAll("[()]", ""); // Assuming the predicate name is the second element
                            int numParameters = 1;
                            ArrayList<String> parameters = new ArrayList<>();
//aggiungi il break da qualche parte
                            // Extract parameters
                            for (int i = 2; i < parts.length; i++) {
                                if (!parts[i].contains("?"))
                                    parameters.add(parts[i]);
                                else
                                    numParameters++;
                            }
                            predicates.add(new Predicate(line, parameters, numParameters));
                        }else
                            break;
                    }
                    domain.setPredicates(predicates);
                }

                assert line != null;
                if(line.contains("(:action")) {
                    //Parse actions
                    String actionName = line.replace("(:action","").trim() ;
                    ArrayList<String> parameters = new ArrayList<>();
                    ArrayList<Fact<Predicate, ObjectPDDL>> precondition = new ArrayList<>();
                    ArrayList<Fact<Predicate, ObjectPDDL>> effect = new ArrayList<>();
                    // Read parameters
                    do{
                        do{
                            if (line.contains(":parameters")) {
                                String[] strings = line.split(" ");
                                if (line.contains(")") && line.contains("(")) {
                                    if (line.contains(":parameters")) line = line.replace(":parameters", "").trim();
                                    parameters.add(line);
                                    break;
                                } else if (line.contains(")")) {
                                    break;
                                }
                            }else break;
                        }while ((line = br.readLine()) != null);

                        // Read preconditions
                        if (line != null) {
                            if ((line.contains(":precondition"))) {

                                do{
                                    if (line.trim().equals("(and"))
                                        continue;
                                    else if (line.contains(")") && line.contains("("))
                                        precondition.add(parseFacts(line));

                                    else if (line.contains(")")) {
                                        break;
                                    }
                                }while ((line=br.readLine()) != null);
                            }

                            // Read effects
                            if ((line != null && line.contains(":effect"))) {
                                do {
                                    if (line.trim().equals("(and"))
                                        continue;
                                    else if (line.contains(")") && line.contains("("))
                                        effect.add(parseFacts(line));
                                    else if (line.contains(")")) {
                                        break;
                                    }
                                }while ((line = br.readLine()) != null);
                            }
                        }
                        if(line==null||((line.trim().equals(")")&&!effect.isEmpty()&&!precondition.isEmpty()&&!parameters.isEmpty()))) break;
                    }while((line=br.readLine())!=null);
                    //String name, ArrayList<Fact> predicates, ArrayList<Fact> effects, ArrayList<String> parameters

                    actions.add(new Action(actionName, precondition, effect, parameters));
                }
                domain.setActions(actions);


                //parse costants

                if (line!=null&&line.contains("(:constants")) {
                    List<ObjectPDDL> objects = parseList(br);
                    domain.setCostants((ArrayList<ObjectPDDL>) objects);
                }


            }
    }

    public static Problem parseProblem(String problemFileName) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(problemFileName));
        String line;

            while ((line =br.readLine()) != null) {
                if (line.startsWith("(define (problem")) {
                    problem.setName(line.split(" ")[2].replace(")",""));
                }
                // Skip comments and empty lines
                if (line.startsWith(";") || line.isEmpty()) {
                    continue;
                }

                // Parse objects
                if (line.contains("(:objects")) {
                        List<ObjectPDDL> objects = parseList(br);
                        problem.setObjects((ArrayList<ObjectPDDL>) objects);
                        problem.setObjects2((ArrayList<ObjectPDDL>) objects);
                        problem.getObjects2().addAll(domain.getCostants());
                }

                // Parse initial state
                if (line.contains("(:init")) {
                    //List<String> l1 = parseList(br);
                    ArrayList<Fact<Predicate,ObjectPDDL>> initialState= new ArrayList<>();
                    do {
                        if(line.contains("(")&&line.contains(")")) {
                            initialState.add( parseFacts(line));
                        }else if(line.contains(")")) break;
                    }while ((line = br.readLine())!= null);
                    problem.setInitialState(initialState);
                }

                // Parse goal state
                if (line.contains("(:goal")) {
                    //List<String> l1 = parseList(br);
                    ArrayList<Fact<Predicate,ObjectPDDL>> goalState=new ArrayList<>();
                    do {
                        if(line.contains("(")&&line.contains(")")) {
                            if(line.contains("and")){
                                line = line.replace("and", "");
                            }
                            goalState.add(parseFacts(line));
                        }else if(line.contains(")")) break;
                    }while ((line = br.readLine())!= null);
                    problem.setGoalState(goalState);
                }
            }


        return problem;
    }

    private static List<ObjectPDDL> parseList(BufferedReader br) throws IOException {
        List<ObjectPDDL> list = new ArrayList<>();
        String line;

        while ((line = br.readLine())!=null && !line.contains(")")) {
            Type type = new Type("object", null);
            if(line.contains("-")){
                String[] typeStr = line.split("-");
                for(Type t : domain.getTypes()) {
                    if (typeStr[1].trim().contains(t.getName())){
                        type = t;
                        break;
                    }
                }
                    if(typeStr[0].trim().contains(" ")){
                        String[] names = typeStr[0].trim().split(" ");
                        for(String s : names){
                            if(!s.isEmpty() )
                                list.add(new ObjectPDDL(s, type));
                        }
                    }else {
                        list.add(new ObjectPDDL(typeStr[0].trim(), type));
                    }

            }else{
                if(line.trim().contains(" ")){
                    String[] names = line.split(" ");
                    for(String s : names){
                        list.add(new ObjectPDDL(s, type));
                    }
                }else{
                    String name = line.trim();
                    list.add(new ObjectPDDL(name, type));
                }
            }
        }
        return list;
    }
    private static Fact<Predicate, ObjectPDDL> parseFacts(String input) {
        List<Fact<Predicate, ObjectPDDL>> facts = new ArrayList<>();
        Fact<Predicate, ObjectPDDL> fact = null;
        boolean negate = false;
        ArrayList<Coppia<Integer, ObjectPDDL>> pos = new ArrayList<>();
        Predicate predicate = null;
        ObjectPDDL object = null;
        ArrayList<ObjectPDDL> objectPDDLS = new ArrayList<>();

        // Rimuovi parentesi e suddividi la stringa in sottostringhe separate da spazi
        if(input.contains("not")){
            negate = true;
            input = input.replace("not", "");
        }

        String complete = input.trim();
        complete = complete.replaceAll("[()]", "");
        complete = "("+complete+")";
        String[] tokens = input.replaceAll("[()]", "").trim().split("\\s+");
        String predicateName=tokens[0].trim().replaceAll("[()]", "");
        int numero = 1;

        for (int i = 1; i < tokens.length; i ++) {
            String stringa = tokens[i];

            for(ObjectPDDL obj : problem.getObjects()){
                if(stringa.contains(obj.getName())) {
                    object = obj;
                    objectPDDLS.add(obj);
                }
            }

            Coppia<Integer, ObjectPDDL> coppia = new Coppia<>(numero, object);
            pos.add(coppia);

            numero++;

            for(Predicate pred : domain.getPredicates()){
                if(pred.getComplete().contains(predicateName.trim())) {
                    predicate = pred;
                    break;
                }
            }
        }

        if(numero==1){
            for(Predicate pred : domain.getPredicates()){
                if(pred.getComplete().contains(predicateName.trim())) {
                    predicate = pred;
                    break;
                }
            }
        }
        fact = new Fact<Predicate,ObjectPDDL>(complete, predicate, pos, negate, objectPDDLS);
        return fact;
    }
}





