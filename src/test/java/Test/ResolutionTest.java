package Test;

import org.junit.jupiter.api.*;
import Tesi.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResolutionTest {
    private IntermediateModel<Action, ObjectPDDL, Predicate> model;
        @BeforeAll
        public void setUp() throws Exception {
            ParserInPDDL parser = new ParserInPDDL();
            Domain domain = parser.parseDomain(domainFile);
            Problem problem = ParserInPDDL.parseProblem(problemFile);

            model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain.getActions(), problem.getObjects(), domain.getPredicates(), problem.getInitialState(), problem.getGoalState());
        }

        @AfterAll
        public void tearDown() throws Exception {

        }

        @Test
        void testRuleOne() throws Exception {
            uhm = new UtilityHashMap<T, V>();
            ArrayList<Properties> removable = new ArrayList<>();

            Assertions.assertTrue(!(model.getProperties().contains(removable)));
        }

        @Test
        void testRuleTwo() throws Exception {
            Assertions.assertTrue(dao.doRetrieveByKey(bean.getRecensione())!=null);
        }

    @Test
    void testRuleThree() throws Exception {
        Assertions.assertTrue(dao.doRetrieveByKey(bean.getRecensione())!=null);
    }

    @Test
    void testRuleFour() throws Exception {
        Assertions.assertTrue(dao.doRetrieveByKey(bean.getRecensione())!=null);
    }

    @Test
    void testRuleFive() throws Exception {
        Assertions.assertTrue(dao.doRetrieveByKey(bean.getRecensione())!=null);
    }
}
