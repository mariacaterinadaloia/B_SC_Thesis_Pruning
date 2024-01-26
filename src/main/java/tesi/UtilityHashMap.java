package tesi;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilityHashMap<T extends tesi.Properties, V extends ObjectModel> {
    public HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> hm;

    public HashMap<Coppia<T,V>, Coppia<Integer, Boolean>> createMap(T properties, ArrayList<V> objs, ArrayList<Fact<T,V>> facts){
        hm = new HashMap<Coppia<T,V>, Coppia<Integer, Boolean>>();
        boolean is = true;
        Coppia<Integer, Boolean> a;

        for(Fact<T,V> fact : facts) {
            if(!properties.equals(fact.getProperties())) continue;
            if(fact.isNegate())
                is = false;
            for(Coppia<Integer, V> c : fact.getPos()){
                for (V obj : objs) {
                    if(c.getC2().equals(obj)) {
                        if ((a = hm.get(new Coppia<T, V>(properties, obj)))!=null){
                            if(a.equals(new Coppia<Integer, Boolean>(c.getC1(), is)))
                                continue;
                            else{
                                hm.put(new Coppia<T, V>(properties, obj), new Coppia<Integer, Boolean>(c.getC1(), is));
                            }
                        }else {
                                hm.put(new Coppia<T, V>(properties, obj), new Coppia<Integer, Boolean>(c.getC1(), is));
                        }
                    }
                }
            }
        }
        return hm;
    }
}
