package test;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import tesi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResolutionTest {
    private UtilityHashMap<Predicate,ObjectPDDL> uhm;
    private HashMap<Coppia<Predicate,ObjectPDDL>, Coppia<Integer, Boolean>> hm;
    private IntermediateModel<Action, ObjectPDDL, Predicate> model;
    private Domain domain;
    private Problem problem;
    private Resolution<Predicate, ObjectPDDL, Action> resolution;
        @BeforeEach
        public void setUp() throws IOException {
            ParserInPDDL parser = new ParserInPDDL();
            domain = parser.parseDomain("C:\\Users\\maria\\Desktop\\TESI\\EuristicheDiPruningPerLaVerificaAutomaticaDeiSistemiBasatiSuRegole\\src\\test\\java\\test\\domain.pddl");
            problem = ParserInPDDL.parseProblem("C:\\Users\\maria\\Desktop\\TESI\\EuristicheDiPruningPerLaVerificaAutomaticaDeiSistemiBasatiSuRegole\\src\\test\\java\\test\\problem.pddl");
            uhm = new UtilityHashMap<>();
            model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain.getActions(), problem.getObjects(), domain.getPredicates(), problem.getInitialState(), problem.getGoalState());
            resolution =  new Resolution<>(model);
        }

        @Test
        void testRuleOne(){
            System.out.println("Test rule one");
            ArrayList<Predicate> removable = new ArrayList<>();
            ArrayList<Predicate> cmp = new ArrayList<>();
            ArrayList<Action> cmpActions = new ArrayList<>();
            for(Predicate predicate : model.getProperties()) {
                if(predicate.getComplete().contains("on")) cmp.add(predicate);
            }

            for(Action action : model.getFunctions()){
                if(action.getName().equals("rimuoviOggetto")) cmpActions.add(action) ;
            }

            for(Predicate predicate : model.getProperties()) {
                hm = uhm.createMap(predicate, model.getObjects(), model.getGoalState());
                if(resolution.ruleOne(hm, predicate)!=null)
                    removable.add(predicate);
            }

            for (Predicate predicate : removable) {
                model.getProperties().removeIf(predicate::equals);
            }

            Assertions.assertFalse(model.getProperties().contains(cmp));
        }

        @Test
        void testRuleTwo() throws Exception {
            System.out.println("Test rule two");
            ArrayList<Predicate> removable = new ArrayList<>();
            ArrayList<Predicate> cmp = new ArrayList<>();
            ArrayList<Action> cmpActions = new ArrayList<>();
            for(Predicate predicate : model.getProperties()) {
                if(predicate.getComplete().contains("to")) cmp.add(predicate);
            }

            for(Action action : model.getFunctions()){
                if(action.getName().equals("aggiungiOggetto")) cmpActions.add(action) ;
            }

            for(Predicate predicate : model.getProperties()) {
                hm = uhm.createMap(predicate, model.getObjects(), model.getGoalState());
                if(resolution.ruleTwo(hm, predicate)!=null)
                    removable.add(predicate);
            }
            for(Predicate predicate : removable){
                model.getProperties().removeIf(predicate::equals);
            }
            Assertions.assertFalse(model.getProperties().contains(cmp));
        }

    @Test
    void testRuleThree() throws Exception {
        ArrayList<Predicate> removable = new ArrayList<>();
        ArrayList<Predicate> cmp = new ArrayList<>();
        ArrayList<Action> cmpActions = new ArrayList<>();
        for(Predicate predicate : model.getProperties()) {
            if(predicate.getComplete().contains("as")) cmp.add(predicate);
        }

        for(Action action : model.getFunctions()){
            if(action.getName().contains("aggiungiOggetto2")) cmpActions.add(action) ;
        }

        for(Predicate predicate : model.getProperties()) {
            hm = uhm.createMap(predicate, model.getObjects(), model.getGoalState());
            if(resolution.ruleThree(hm, predicate)!=null)
                removable.add(predicate);
        }
        for(Predicate predicate : removable){
            model.getProperties().removeIf(predicate::equals);
        }

        Assertions.assertFalse(model.getProperties().contains(cmp));
    }

    @Test
    void testRuleFour() throws Exception {
            int size = model.getFunctions().size();
            resolution.ruleFour();
            Assertions.assertTrue(model.getFunctions().size()!=size);
    }

    @Test
    void testRuleFive() throws Exception {
        int size = model.getFunctions().size();
        resolution.ruleFive();
        Assertions.assertTrue(model.getFunctions().size()!=size);
    }
    @Test
    void testRuleSix() throws Exception {
        int size = model.getFunctions().size();
        resolution.ruleSix();
        Assertions.assertTrue(model.getFunctions().size()!=size);
    }

    @Test
    void testAll(){
            resolution.init();
            Predicate p = model.getProperties().get(0);
            Action a = model.getFunctions().get(0);
            Assertions.assertTrue(model.getProperties().size()==1&&model.getFunctions().size()==1&&a.getName().contains("super")&&p.getComplete().contains("super"));
    }
}
