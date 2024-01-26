package tesi;

import java.util.ArrayList;

public class Problem {
    private String name;
    private ArrayList<ObjectPDDL> objects;

    public ArrayList<ObjectPDDL> getObjects2() {
        return objects2;
    }

    public void setObjects2(ArrayList<ObjectPDDL> objects2) {
        this.objects2 = objects2;
    }

    private ArrayList<ObjectPDDL> objects2;
    private ArrayList<Fact<Predicate,ObjectPDDL>> initialState;
    private ArrayList<Fact<Predicate,ObjectPDDL>> goalState;

    public Problem() {
        this.objects = new ArrayList<>();
        this.initialState = new ArrayList<>();
        this.goalState = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ObjectPDDL> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<ObjectPDDL> objects) {
        this.objects = objects;
    }

    public ArrayList<Fact<Predicate,ObjectPDDL>> getInitialState() {
        return initialState;
    }

    public void setInitialState(ArrayList<Fact<Predicate,ObjectPDDL>> initialState) {
        this.initialState = initialState;
    }

    public ArrayList<Fact<Predicate,ObjectPDDL>> getGoalState() {
        return goalState;
    }

    public void setGoalState(ArrayList<Fact<Predicate,ObjectPDDL>> goalState) {
        this.goalState = goalState;
    }


}
