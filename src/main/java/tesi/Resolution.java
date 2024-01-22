package tesi;

import java.util.ArrayList;
import java.util.HashMap;


public class Resolution<T extends tesi.Properties, V extends ObjectModel, U extends Function<T,V>> {
    //In questa classe è presente la logica di pruning che è il cuore della tesi
    private UtilityHashMap<T,V> uhm;
    private ArrayList<T> properties;
    private ArrayList<V> objects;
    private ArrayList<Fact<T,V>> init;
    private HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> hm;
    private ArrayList<Fact<T,V>> goal;
    private Properties p;
    private ArrayList<U> functions;

    public Resolution(IntermediateModel<U, V, T> model){
        this.properties = model.getProperties();
        this.objects = model.getObjects();
        this.init = model.getInitialState();
        this.goal = model.getGoalState();
        this.functions = model.getFunctions();
    }

    //Questa funzione inizializza le mappe che corrispondono ai valori di verità dei predicati, andandosi a basare su una classe
    //Coppia che lega il predicato alla propria mappa,andando a correllare
    public void init() {
        uhm = new UtilityHashMap<T, V>();
        ArrayList<Properties> removable = new ArrayList<>();
        ruleFour();
        ruleFive();
        ruleSix();
        for (T predicate : properties) {
            hm = uhm.createMap(predicate, objects, init);
            removable.add(ruleOne(hm, predicate));
            removable.add(ruleTwo(hm, predicate));
            removable.add(ruleThree(hm, predicate));
        }


        finalCheck();

        properties.removeAll(removable);

        //in successione chiama le regole ed effettua il pruning
        //a seguito dell'istanziazione dell'hashmap, possiamo procedere agli algoritmi generici basati sulle regole di
        //pruning di ARBAC
        finalCheck();
    }


    public T ruleOne(HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> map, T predicate) {
        ArrayList<T> notRemovable = new ArrayList<>();
        ArrayList<T> removable = new ArrayList<>();
        ArrayList<U> removableA = new ArrayList<>();
        ArrayList<Fact<T,V>> removableF = new ArrayList<>();
        ArrayList<Fact<T,V>> removableFP = new ArrayList<>();
        if (!map.isEmpty()) {
            for (Coppia<T,V> c : map.keySet()) {
                if (map.get(c).getC2()) return null; //se almeno uno dei valori è vero, la regola non è applicabbile
            }
        }
        //se il ciclo non è stato interrotto, si passa alla seconda parte della regola
        //controlla se esistono funzioni che portano a true lo stato
        for (U a : functions) {
            for (Fact<T,V> f : a.getEffects()) {
                if (predicate.equals(f.getProperties()))
                    if (!f.isNegate()) {
                        return null;
                    }
            }
        }
        //Se siamo arrivati qui significa che non c'é nessuna regola che porta lo stato a true, si può rimuovere.
       for (U a : functions) {
            for (Fact<T,V> f : a.getEffects()) {
                if (predicate.equals(f.getProperties()))
                    if (f.isNegate()) {
                        removableF.add(f); //rimuovere da effetto
                        //dobbiamo rimuoverlo anche dalla precondizione
                        removableFP.add(new Fact<T,V>(f.getComplete(), predicate, f.getPos(), false));
                    }

                //Se l'unico predicato in quella azione è quello rimosso, allora possiamo eliminare completamente l'azione
                a.getEffects().removeAll(removableFP);
                a.getPredicates().removeAll(removableF);
                if (a.getEffects().isEmpty() || a.getPredicates().isEmpty()) removableA.add(a);
            }
        }
        //Possiamo finalmente rimuovere il predicato
        functions.removeAll(removableA);
        init.removeIf(fact -> fact.getProperties().equals(predicate));
        return predicate;
    }

    public T ruleThree(HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> map, T predicate){
        ArrayList<Fact<T,V>> removableF = new ArrayList<>();
        ArrayList<Fact<T,V>> removableFP = new ArrayList<>();
        T pred = predicate;
        for(U a : functions){
                for(Fact<T,V> p1 : a.getEffects()){
                    if(predicate.equals(p1.getProperties())){
                        Fact f, i;
                        if((f = checkGoal(predicate)) != null) {
                            if((i = checkInit(predicate))!= null) {
                                if (f.isNegate() == p1.isNegate()&&(!i.isNegate())== p1.isNegate())
                                    return null;
                            }
                        }
                    }
                    removableFP.add(p1);
                }
            a.getPredicates().removeAll(removableFP);
            }

        return pred;
    }

    private Fact<T,V> checkGoal(T predicate) {
        for (Fact<T,V> a : goal) {
            if (a.getProperties().equals(predicate)) {
                return a;
            }
        }
        return null;
    }
    private Fact<T,V> checkInit(T predicate) {
        for (Fact<T,V> a : init) {
            if (a.getProperties().equals(predicate)) {
                return a;
            }
        }
        return null;
    }

    public T ruleTwo(HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> map, T predicate) {
        ArrayList<T> notRemovable = new ArrayList<>();
        ArrayList<T> removable = new ArrayList<>();
        ArrayList<U> removableA = new ArrayList<>();
        ArrayList<Fact<T,V>> removableF = new ArrayList<>();
        ArrayList<Fact<T,V>> removableFP = new ArrayList<>();
        if (!map.isEmpty()) {
            for (Coppia<T,V> c : map.keySet()) {
                if (!map.get(c).getC2()) return null; //se almeno uno dei valori è vero, la regola non è applicabbile
            }
        }
        //se il ciclo non è stato interrotto, si passa alla seconda parte della regola
        //controlla se esistono funzioni che portano a false lo stato
        for (U a : functions) {
            for (Fact<T,V> f : a.getEffects()) {
                if (predicate.equals(f.getProperties()))
                    if (f.isNegate()) {
                        return null;
                    }
            }
        }
        //Se siamo arrivati qui significa che non c'é nessuna regola che porta lo stato a true, si può rimuovere.
        for (U a : functions) {
            for (Fact<T,V> f : a.getEffects()) {
                if (predicate.equals(f.getProperties()))
                    if (!f.isNegate()) {
                        removableF.add(f); //rimuovere da effetto
                        //dobbiamo rimuoverlo anche dalla precondizione
                        removableFP.add(new Fact<T,V>(f.getComplete(), predicate, f.getPos(), false));
                    }

                //Se l'unico predicato in quella azione è quello rimosso, allora possiamo eliminare completamente l'azione
                a.getEffects().removeAll(removableFP);
                a.getPredicates().removeAll(removableF);
                if (a.getEffects().isEmpty() || a.getPredicates().isEmpty()) removableA.add(a);
            }
        }
        //Possiamo finalmente rimuovere il predicato
        functions.removeAll(removableA);
        init.removeIf(fact -> fact.getProperties().equals(predicate));
        return predicate;
    }
        public void ruleFour() {
            ArrayList<Comparator<U,T,V>> cmp = new ArrayList<>();
            ArrayList<Fact<T,V>> subset = new ArrayList<>();
            ArrayList<Fact<T,V>> opposite = new ArrayList<>();
            for (U a : functions) {
                for (U b : functions) {
                    if (a.equals(b)) continue;
                    for (Fact<T,V> f1 : a.getPredicates()) {
                        for (Fact<T,V> f2 : b.getPredicates()) {
                            if (f1.getProperties().equals(f2.getProperties())) {
                                subset.add(f2);
                            } else {
                                if (checkFact(f1, f2))
                                    opposite.add(f1);
                            }
                        }
                    }
                    cmp.add(new Comparator<U,T,V>(a, b, subset, opposite));
                    subset.clear();
                    opposite.clear();
                }

                for (Comparator<U,T,V> comparator : cmp) {
                    if (checkSubset(comparator.getOpposite(), comparator.getSubset(), comparator.getA().getPredicates())) {
                        for (Fact<T,V> f : opposite) {
                            comparator.getA().getPredicates().removeIf(f::equals);
                        }
                        functions.remove(comparator.getA());
                    }
                }
            }
        }

    private boolean checkSubset(ArrayList<Fact<T,V>> opposite, ArrayList<Fact<T,V>>subset, ArrayList<Fact<T,V>> predicates) {
        ArrayList<Fact<T, V>> cpy = new ArrayList<>(predicates);
        cpy.removeAll(subset);
        for(Fact<T,V> f : opposite)
            cpy.removeIf(d -> checkFact(f, d));

        return cpy.isEmpty();
    }

    public void ruleFive(){
            ArrayList<U> removable = new ArrayList<>();
            ArrayList<Coppia<U,U>> implicated = new ArrayList<>();

            for(U a : functions){
                for(U b : functions){
                    if(a.equals(b)) continue;
                    if(a.getEffects().containsAll(b.getEffects())){
                        if(a.getPredicates().containsAll(b.getPredicates())){
                            implicated.add(new Coppia<>(a, b));
                        }
                    }
                }
            }

            for(Coppia<U,U> a : implicated){
                functions.removeIf(a.getC2()::equals);
            }
        }

        public void ruleSix(){
            ArrayList<U> removableA = new ArrayList<>();
            for(U a : functions){
                for(Fact<T,V> f : a.getPredicates()){
                    if(!checkInit(f))
                        removableA.add(a);
                }
            }

            for(U a : removableA){
                functions.removeIf(a::equals);
            }
        }
        //Questa funzione controlla se nell'init è presente un predicato che può rendere vera la funzione
        private boolean checkInit(Fact<T,V> fact){
            for(Fact<T,V> f : init){
                if(f.getProperties()==fact.getProperties() && f.isNegate()==fact.isNegate()) return true;
            }
            return false;
        }

        private boolean checkFact(Fact f1, Fact f2){
                if(!f1.getProperties().equals(f2.getProperties())) return false;
                return f1.isNegate() != f2.isNegate();
        }

        private static class Comparator<U extends Function, T extends tesi.Properties, V extends ObjectModel>{
            private U a;
            private U b;
            private ArrayList<Fact<T,V>> opposite;
            private ArrayList<Fact<T,V>> subset;
            Comparator(U a, U b, ArrayList<Fact<T,V>> opposite, ArrayList<Fact<T,V>> subset){
                this.a = a;
                this.b = b;
                this.opposite = opposite;
                this.subset = subset;
            }

            public ArrayList<Fact<T,V>> getSubset() {
                return subset;
            }

            public ArrayList<Fact<T,V>> getOpposite() {
                return opposite;
            }

            public U getB() {
                return b;
            }

            public U getA() {
                return a;
            }
        }

        private void finalCheck() {
            for(U a : functions){
                a.getPredicates().removeIf(f -> !properties.contains(f.getProperties()));
            }
        }

}