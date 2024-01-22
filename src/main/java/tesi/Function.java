package tesi;

import java.util.ArrayList;
import java.util.Objects;

public class Function<T extends tesi.Properties, V extends ObjectModel> {
    private String name;
    private ArrayList<Fact<T,V>> predicates;
    private ArrayList<Fact<T,V>> effects;

    public Function(String name, ArrayList<Fact<T,V>> predicates, ArrayList<Fact<T,V>> effects){
        this.name = name;
        this.predicates = predicates;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Fact<T,V>> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<Fact<T,V>> effects) {
        this.effects = effects;
    }

    public ArrayList<Fact<T,V>> getPredicates() {
        return predicates;
    }

    public void setPredicates(ArrayList<Fact<T,V>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function<T,V> function = (Function<T,V>) o;
        return Objects.equals(name, function.name) && Objects.equals(predicates, function.predicates) && Objects.equals(effects, function.effects);
    }
}
