package test;

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
        @BeforeAll
        public void setUp() throws IOException {
            ParserInPDDL parser = new ParserInPDDL();
            Domain domain = parser.parseDomain("domain.pddl");
            Problem problem = ParserInPDDL.parseProblem("problem.pddl");

            model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain.getActions(), problem.getObjects(), domain.getPredicates(), problem.getInitialState(), problem.getGoalState());
        }

        @AfterAll
        public void tearDown(){

        }

        @Test
        void testRuleOne(){
            uhm = new UtilityHashMap<Predicate,ObjectPDDL>();
            ArrayList<Properties> removable = new ArrayList<>();

            Assertions.assertTrue(!(model.getProperties().contains(removable)));
        }

        @Test
        void testRuleTwo() throws Exception {
            Assertions.assertTrue(true);
        }

    @Test
    void testRuleThree() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    void testRuleFour() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    void testRuleFive() throws Exception {
        Assertions.assertTrue(true);
    }
}
