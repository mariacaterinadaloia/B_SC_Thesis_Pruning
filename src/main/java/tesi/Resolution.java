package tesi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

        for (T predicate : properties) {
            if(checkGoal(predicate)!=null)
                continue;
            hm = uhm.createMap(predicate, objects, init);
            Properties t;
            boolean cckd = false;

            if ((t = ruleOne(hm, predicate)) != null && !removable.contains(t)) {
                removable.add(t);
                cckd = true;
                System.out.println(t.getComplete()+" 1");
            }
            if (!cckd && (t = ruleTwo(hm, predicate)) != null && !removable.contains(t)) {
                removable.add(t);
                cckd = true;
                System.out.println(t.getComplete()+" 2");
            }
            if (!cckd && (t = ruleThree(hm, predicate)) != null && !removable.contains(t)){
                removable.add(t);
                cckd = true;
                System.out.println(t.getComplete()+" 3");
            }
        }

        ruleSix();
        ruleFour();
        ruleFive();
        properties.removeAll(removable);

        //in successione chiama le regole ed effettua il pruning
        //a seguito dell'istanziazione dell'hashmap, possiamo procedere agli algoritmi generici basati sulle regole di
        //pruning di ARBAC
    }


    public T ruleOne(HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> map, T predicate) {
        if(checkGoal(predicate)!=null)
            return null;
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
                if (predicate.equals(f.getProperties())) {
                    if (!f.isNegate()) {
                        return null;
                    }
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
                        removableFP.add(new Fact<T,V>(f.getComplete(), predicate, f.getPos(), false,f.getObjects()));
                    }

                //Se l'unico predicato in quella azione è quello rimosso, allora possiamo eliminare completamente l'azione
                a.getEffects().removeAll(removableFP);
                a.getPredicates().removeAll(removableF);
                if (a.getEffects().isEmpty() || a.getPredicates().isEmpty()) removableA.add(a);
            }
        }
        //Possiamo finalmente rimuovere il predicato
        functions.removeAll(removableA);
        init.removeIf(fact -> fact.getProperties()!=null&&fact.getProperties().equals(predicate));
        return predicate;
    }

    public T ruleThree(HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> map, T predicate){
        if(checkGoal(predicate)!=null)
            return null;
        ArrayList<Fact<T,V>> removableFP = new ArrayList<>();
        for(U a : functions){
                for(Fact<T,V> p1 : a.getPredicates()){
                    if(predicate.equals(p1.getProperties())){
                            for(Fact<T,V> i : checkInit(predicate)) {
                                if (i.isNegate() == p1.isNegate())
                                    return null;
                                else
                                    removableFP.add(p1);
                            }
                    }

                }
                if(!removableFP.isEmpty())
                    a.getPredicates().removeAll(removableFP);
            }
        return predicate;
    }

    private Fact<T,V> checkGoal(T predicate) {
        for (Fact<T,V> a : goal) {
            if (a.getProperties()!=null&&a.getProperties().equals(predicate)) {
                return a;
            }
        }
        return null;
    }
    private ArrayList<Fact<T,V>> checkInit(T predicate) {
        ArrayList<Fact<T,V>> al = new ArrayList<>();
        for (Fact<T,V> a : init) {
            if (a.getProperties().equals(predicate)) {
                al.add(a);
            }
        }
        return al;
    }

    public T ruleTwo(HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> map, T predicate) {
        if(checkGoal(predicate)!=null)
            return null;
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
                        removableFP.add(new Fact<T,V>(f.getComplete(), predicate, f.getPos(), false, f.getObjects()));
                    }
            }
                //Se l'unico predicato in quella azione è quello rimosso, allora possiamo eliminare completamente l'azione
                a.getEffects().removeAll(removableFP);
                a.getPredicates().removeAll(removableF);
                if (a.getEffects().isEmpty() || a.getPredicates().isEmpty()) removableA.add(a);
        }
        //Possiamo finalmente rimuovere il predicato
        functions.removeAll(removableA);
        init.removeIf(fact -> fact.getProperties()!=null&&fact.getProperties().equals(predicate));
        return predicate;
    }
        public void ruleFour() {
            ArrayList<Coppia<U,U>> ctrl = new ArrayList<>();
            ArrayList<Comparator<U,T,V>> cmp = new ArrayList<>();
            ArrayList<Fact<T,V>> subsetP = new ArrayList<>();
            ArrayList<Fact<T,V>> oppositeP = new ArrayList<>();
            ArrayList<Fact<T,V>> subsetE = new ArrayList<>();
            ArrayList<Fact<T,V>> oppositeE = new ArrayList<>();
            for (U a : functions) {
                if(a.getPredicates().size()==1&&a.getEffects().size()==1)
                    continue;
                for (U b : functions) {
                    if(b.getPredicates().size()==1&&b.getEffects().size()==1)
                        continue;
                    if (a.equals(b)) continue;
                    if((a.getPredicates().size()!=b.getPredicates().size())||(a.getEffects().size()!=b.getEffects().size()))
                        continue;
                    if(ctrl.contains(new Coppia<U,U>(b,a)))
                        continue;
                    else
                        ctrl.add(new Coppia<>(a,b));
                    for (Fact<T,V> f1 : a.getPredicates()) {
                        for (Fact<T,V> f2 : b.getPredicates()) {
                            if (controlNull(f1)&&controlNull(f2)&&checkFact2(f1,f2)) {
                                subsetP.add(f2);
                            } else {
                                if (controlNull(f1)&&controlNull(f2)&&checkFact(f1, f2))
                                    oppositeP.add(f1);
                            }
                        }
                    }
                    for (Fact<T,V> f1 : a.getEffects()) {
                        for (Fact<T,V> f2 : b.getEffects()) {
                            if (controlNull(f1)&&controlNull(f2)&&checkFact2(f1,f2)) {
                                subsetE.add(f2);
                            } else {
                                if (controlNull(f1)&&controlNull(f2)&&checkFact(f1, f2))
                                    oppositeE.add(f1);
                            }
                        }
                    }
                    cmp.add(new Comparator<U, T, V>(a, b, new ArrayList<>(oppositeP), new ArrayList<>(subsetP), new ArrayList<>(oppositeE), new ArrayList<>(subsetE)));
                    subsetP.clear();
                    oppositeP.clear();
                    subsetE.clear();
                    oppositeE.clear();
                }
            }
            ArrayList<U> already = new ArrayList<>();
            for (Comparator<U,T,V> comparator : cmp) {
                if(already.contains(comparator.getA())||already.contains(comparator.getB()))
                    continue;
                if(comparator.getA().getName().contains("_combined")||comparator.getB().getName().contains("_combined"))
                    continue;
                if (checkSubset(comparator)){
                    System.out.println("Coppie: ");
                    System.out.println(comparator.getA().getName());
                    System.out.println(comparator.getB().getName());

                    for (Fact<T,V> f : comparator.getOppositeP()) {
                        if(checkGoal(f.getProperties())!=null)
                            comparator.getA().getPredicates().removeIf(f::equals);
                    }
                    for (Fact<T,V> f : comparator.getOppositeE()) {
                        if(checkGoal(f.getProperties())!=null)
                            comparator.getA().getEffects().removeIf(f::equals);
                    }
                    if(!comparator.getA().getEffects().isEmpty()||!comparator.getA().getPredicates().isEmpty()) {
                        functions.remove(comparator.getB());
                        functions.remove(comparator.getA());
                        comparator.getA().setName(comparator.getA().getName() + "_combined");
                        already.add(comparator.getB());
                        functions.add(comparator.getA());
                    }else{
                        comparator.getA().getEffects().addAll(comparator.getOppositeE());
                        comparator.getA().getPredicates().addAll(comparator.getOppositeP());
                        functions.remove(comparator.getB());
                        already.add(comparator.getB());
                    }
                    }
                }
        }
    private boolean controlNull(Fact<T,V> fact){
        return fact.getProperties()!=null;
    }
    private boolean checkSubset(Comparator<U,T,V> comparator) {
        ArrayList<Fact<T, V>> cpy1 = new ArrayList<>(comparator.getB().getPredicates());
        ArrayList<Fact<T, V>> cpy2= new ArrayList<>(comparator.getB().getEffects());
        cpy1.removeAll(comparator.getSubsetP());
        for (Fact<T, V> f : comparator.getOppositeP()){
            for (Fact<T, V> d : comparator.getB().getPredicates()) {
                if(checkFact(f,d))
                    cpy1.remove(d);
            }
        }
        cpy2.removeAll(comparator.getSubsetE());
        for (Fact<T, V> f : comparator.getOppositeE()){
            for (Fact<T, V> d : comparator.getB().getEffects()) {
                if(checkFact(f,d))
                    cpy2.remove(d);
            }
        }
        return cpy1.isEmpty()&&cpy2.isEmpty();
    }

    public void ruleFive(){
            ArrayList<U> removable = new ArrayList<>();
            ArrayList<Coppia<U,U>> implicated = new ArrayList<>();

            for(U a : functions){
                if(a.getName().contains("_implied")) continue;
                for(U b : functions){
                    if(a.equals(b)) continue;
                    if(a.getName().contains("_implied")||b.getName().contains("_implied")) continue;
                    if((a.getPredicates().size()<b.getPredicates().size())||(a.getEffects().size()<b.getEffects().size()))
                        continue;
                    if(a.getEffects().containsAll(b.getEffects())){
                        if(a.getPredicates().containsAll(b.getPredicates())){
                                implicated.add(new Coppia<>(a, b));
                        }
                    }
                }
            }

            for(Coppia<U,U> a : implicated){
                boolean not = false;
                if(a.getC1().getName().contains("_implied")) continue;
                if(a.getC2().getName().contains("_implied")) continue;
                for(Fact<T,V> f : a.getC2().getEffects()){
                    if(checkGoal(f.getProperties())!=null) {
                        functions.remove(a.getC2());
                        not = true;
                    }
                }
                if(not) {
                    functions.removeIf(a.getC1()::equals);
                    a.getC2().setName(a.getC2().getName() + "_implied");
                }
            }
        }

        public void ruleSix(){
            ArrayList<U> removableA = new ArrayList<>();
            for(U a : functions){
                for(Fact<T,V> f : a.getPredicates()){
                    if(!checkInit(f)) {
                        if (!removableA.contains(a))
                            removableA.add(a);
                    }//else if(a.getPredicates().size()!=1&&!checkInitForObjects(a.getPredicates(), a.getParameters().get(0)))
                            //removableA.add(a);

                }
            }

            ArrayList<U> notRem = new ArrayList<>();
            ArrayList<Fact<T,V>>  fact = new ArrayList<>();
            for(U a : removableA){
                for(Fact<T,V> f : goal){
                    for(Fact<T,V> d : a.getEffects()){
                        if(controlNull(f)&&controlNull(d)&&d.getProperties().equals(f.getProperties())&&d.isNegate()==f.isNegate()){
                            fact.add(d);
                            notRem.add(a);
                        }
                    }
                }
            }
            removableA.removeAll(notRem);
            functions.removeAll(removableA);
            ArrayList<T> p = new ArrayList<>();
            ArrayList<Fact<T,V>> rem = new ArrayList<>();
            ArrayList<Fact<T,V>> rem2 = new ArrayList<>();
            for(Fact<T,V> f : fact){
                if(!p.contains(f.getProperties()))
                    p.add(f.getProperties());
            }
                for(U a : notRem){
                    for(Fact<T,V> f :a.getEffects()){
                        if(!p.contains(f.getProperties()))
                            rem.add(f);
                    }
                    a.getEffects().removeAll(rem);
                    for(Fact<T,V> f :a.getPredicates()){
                        if(!p.contains(f.getProperties()))
                            rem2.add(f);
                    }
                    a.getPredicates().removeAll(rem2);
                    rem.clear();
                    rem2.clear();
                }
            System.out.println("rimosse dalla 6");
                for(Function<T,V> v : removableA){
                    System.out.println(v.getName());
                }
        }
        //Questa funzione controlla se nell'init è presente un predicato che può rendere vera la funzione
        private boolean checkInit(Fact<T,V> fact){
            for(Fact<T,V> f : init){
                if(f.getProperties()==fact.getProperties() && f.isNegate()==fact.isNegate()) return true;
            }
            return false;
        }

    private boolean checkInitForObjects(ArrayList<Fact<T,V>> precondition, String parameter){
        long occ = countOccurrences(parameter);
        long size = precondition.size();
        ArrayList<Fact<T,V>> canBe = new ArrayList<>();
        for(Fact<T,V> f : init){
            for(Fact<T,V> fact :precondition) {
                if (f.getProperties() == fact.getProperties() && f.isNegate() == fact.isNegate())
                    canBe.add(f);
            }
        }
        long min = 100;
        ArrayList<V> c = new ArrayList<>();
        ArrayList<Fact<T,V>> cmp = new ArrayList<>();
        for(Fact<T,V> f : canBe){
            for(Fact<T,V> f1 : canBe){
                if(f.getProperties().equals(f1.getProperties())) continue;
                for(V v1 : f1.getObjects()){
                    if(f.getObjects().contains(v1)&&!c.contains(v1)){
                        c.add(v1);
                        if(!cmp.contains(f1))
                            cmp.add(f1);
                    }
                }
            }
            if(cmp.size()==occ-1)
                return true;
        }
        return false;
    }

    public long countOccurrences(String str) {
        return str.chars().filter(ch -> ch == '?').count();
    }

        private boolean checkFact(Fact f1, Fact f2){
                if(!f1.getProperties().equals(f2.getProperties())) return false;
                return f1.isNegate() != f2.isNegate();
        }

    private boolean checkFact2(Fact f1, Fact f2){
        if(!f1.getProperties().equals(f2.getProperties())) return false;
        return f1.isNegate() == f2.isNegate();
    }

        private static final class Comparator<U extends Function<T,V>, T extends tesi.Properties, V extends ObjectModel>{
            private U a;
            private U b;
            private ArrayList<Fact<T,V>> oppositeP;
            private ArrayList<Fact<T,V>> subsetP;
            private ArrayList<Fact<T,V>> oppositeE;
            private ArrayList<Fact<T,V>> subsetE;
            Comparator(U a, U b, ArrayList<Fact<T,V>> oppositeP, ArrayList<Fact<T,V>> subsetP, ArrayList<Fact<T,V>> oppositeE, ArrayList<Fact<T,V>> subsetE){
                this.a = a;
                this.b = b;
                this.oppositeP = oppositeP;
                this.subsetP = subsetP;
                this.oppositeE = oppositeE;
                this.subsetE = subsetE;
            }

            public ArrayList<Fact<T, V>> getOppositeP() {
                return oppositeP;
            }

            public ArrayList<Fact<T, V>> getSubsetP() {
                return subsetP;
            }

            public ArrayList<Fact<T, V>> getSubsetE() {
                return subsetE;
            }

            public ArrayList<Fact<T, V>> getOppositeE() {
                return oppositeE;
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