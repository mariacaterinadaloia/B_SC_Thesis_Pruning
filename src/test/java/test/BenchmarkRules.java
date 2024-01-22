package test;
import org.openjdk.jmh.annotations.*;
import tesi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
public class BenchmarkRules{
    String domainFile = "domain.pddl";
    String problemFile = "problem.pddl";
    @Benchmark
    public void benchRules() throws IOException {
        ParserInPDDL parser = new ParserInPDDL();
        Domain domain = parser.parseDomain(domainFile);
        Problem problem = ParserInPDDL.parseProblem(problemFile);

        IntermediateModel<Action, ObjectPDDL, Predicate> model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain.getActions(), problem.getObjects(), domain.getPredicates(), problem.getInitialState(), problem.getGoalState());
        Resolution<Predicate,ObjectPDDL, Action> resolution = new Resolution<Predicate,ObjectPDDL, Action>(model);
        resolution.init();

        ParserOutPDDL parserOut = new ParserOutPDDL(domain.getName());
        ParserOutPDDL.writeProblemFile("finalProblem.pddl", problem);
        ParserOutPDDL.writeDomainFile("finalDomain.pddl", domain);
    }


}
