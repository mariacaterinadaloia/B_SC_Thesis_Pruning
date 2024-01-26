package tesi;

import java.util.ArrayList;

public class Predicate extends Properties{
    public Predicate(String complete, ArrayList<String> suppose, int num){
        super(complete, suppose, num);
    }

    public String phrase(){
        String s = "";
        s += getComplete();
        return s;
    }

}
