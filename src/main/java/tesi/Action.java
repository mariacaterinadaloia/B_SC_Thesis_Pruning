package tesi;

import java.util.ArrayList;
import java.util.Objects;

public class Action extends Function<Predicate, ObjectPDDL>{
    private ArrayList<String> parameters;

    Action(String name, ArrayList<Fact<Predicate, ObjectPDDL>> predicates, ArrayList<Fact<Predicate, ObjectPDDL>> effects, ArrayList<String> parameters){
        super(name, predicates, effects);
        this.parameters=parameters;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public String phrase(){
        return "(:action " +super.getName()+ "\n \t\t :parameters "+ getParametersString() +" \n \t\t" + " :precondition "+
                "(and \n \t \t \t" + getPredicatesString() + "\n \t\t)" + ":effect \n \t\t" + "(and \n \t \t \t" + getEffectString() + "\n \t \t) \n )";
    }

    public String getPredicatesString(){
        String s = "";
        for(Fact<Predicate, ObjectPDDL> tmp : super.getPredicates())
            s += tmp.phrase().trim()+"\n \t \t \t";
        return s;
    }

    public String getEffectString(){
        String s = "";
        for(Fact<Predicate, ObjectPDDL> tmp : super.getEffects())
            s += tmp.phrase().trim()+"\n \t \t \t";
        return s;
    }

    public String getParametersString(){
        String s = "";
        for(String tmp : parameters)
            s += tmp+"\n";
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Action action = (Action) o;
        return Objects.equals(parameters, action.parameters);
    }
}
