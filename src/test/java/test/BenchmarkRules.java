package test;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
public class BenchmarkRules{
    String domainFile = "C:\\Users\\maria\\Documents\\GitHub\\Euristiche_Di_Pruning\\src\\Tesi\\domainEx.pddl";
    String problemFile = "C:\\Users\\maria\\Documents\\GitHub\\Euristiche_Di_Pruning\\src\\Tesi\\pEx.pddl";
    @Benchmark
    public void benchRules() {
        setCurrentBenchmarkMethod("benchRuleOne");
        ParserInPDDL parser = new ParserInPDDL();
        Domain domain = parser.parseDomain(domainFile);
        Problem problem = ParserInPDDL.parseProblem(problemFile);
        ArrayList<String> s = new ArrayList<>();

        Resolution resolution = new Resolution(problem, domain);
        resolution.init();

        ParserOutPDDL parserOut = new ParserOutPDDL(domain.getName());
        ParserOutPDDL.writeProblemFile("finalProblem.pddl", problem);
        ParserOutPDDL.writeDomainFile("finalDomain.pddl", domain);
    }

}
