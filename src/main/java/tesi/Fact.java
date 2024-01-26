package tesi;

import java.util.ArrayList;
import java.util.Objects;

public class Fact<T extends tesi.Properties, V extends ObjectModel> {
    private String complete;
    private ArrayList<Coppia<Integer, V>>pos;
    private T properties;
    private boolean negate;
    private ArrayList<V> objects;

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public Fact(String complete, T properties, ArrayList<Coppia<Integer, V>>pos, boolean negate, ArrayList<V> objects){
        this.pos = pos;
        this.properties = properties;
        this.negate = negate;
        this.complete = complete;
        this.objects = objects;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }

    public ArrayList<Coppia<Integer, V>> getPos() {
        return pos;
    }

    public void setPos(ArrayList<Coppia<Integer, V>>pos) {
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fact<T,V> fact = (Fact<T,V>) o;
        return negate == fact.negate && Objects.equals(pos, fact.pos) && Objects.equals(properties, fact.properties);
    }

    public ArrayList<V> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<V> objects) {
        this.objects = objects;
    }

    public String phrase(){
        String s = "";
        if(negate)
            s += "(not";
        s += complete;
        if(negate)
            s +=")";
        return s;
    }
}
