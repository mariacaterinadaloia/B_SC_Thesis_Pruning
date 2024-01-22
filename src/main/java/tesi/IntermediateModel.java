package tesi;

import java.util.ArrayList;

public class IntermediateModel <T extends Function<G,V>, V extends ObjectModel, G extends Properties> {
    private ArrayList<T> functions;
    private ArrayList<V> objects;
    private ArrayList<G> properties;
    private ArrayList<Fact<G,V>> initialState;
    private ArrayList<Fact<G,V>> goalState;
    public IntermediateModel(ArrayList<T> functions, ArrayList<V> objects, ArrayList<G> properties, ArrayList<Fact<G,V>> initialState, ArrayList<Fact<G,V>> goalState){
        this.functions = functions;
        this.objects = objects;
        this.properties = properties;
        this.initialState = initialState;
        this.goalState = goalState;
    }

    public ArrayList<Fact<G,V>> getGoalState() {
        return goalState;
    }

    public void setGoalState(ArrayList<Fact<G,V>> goalState) {
        this.goalState = goalState;
    }

    public ArrayList<Fact<G,V>> getInitialState() {
        return initialState;
    }

    public void setInitialState(ArrayList<Fact<G,V>> initialState) {
        this.initialState = initialState;
    }

    public ArrayList<G> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<G> properties) {
        this.properties = properties;
    }

    public ArrayList<V> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<V> objects) {
        this.objects = objects;
    }

    public ArrayList<T> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<T> functions) {
        this.functions = functions;
    }
}
