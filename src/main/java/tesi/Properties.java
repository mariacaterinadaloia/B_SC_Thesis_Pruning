package tesi;

import java.util.ArrayList;
import java.util.Objects;

public class Properties {
    private String complete;
    private ArrayList<String> suppose;
    private int num;

    Properties(String complete, ArrayList<String> suppose, int num){
        this.complete = complete;
        this.num = num;
        this.suppose = suppose;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<String> getSuppose() {
        return suppose;
    }

    public void setSuppose(ArrayList<String> suppose) {
        this.suppose = suppose;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Properties predicate = (Properties) o;
        return num == predicate.num && Objects.equals(complete, predicate.complete) && Objects.equals(suppose, predicate.suppose);
    }
}
