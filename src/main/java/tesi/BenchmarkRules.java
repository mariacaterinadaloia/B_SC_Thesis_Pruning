package tesi;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
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

    public static void main(String[] args) {
        Options opt = new OptionsBuilder()
                .include(BenchmarkRules.class.getSimpleName())
                .addProfiler(GCProfiler.class).forks(1)
                .build();

        try {
            new Runner(opt).run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
