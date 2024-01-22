package tesi;

import java.util.Objects;

public class Coppia<T,V>{
    private T c1;
    private V c2;

    public Coppia(T c1, V c2){
        this.c1 = c1;
        this.c2 = c2;
    }

    public T getC1() {
        return c1;
    }

    public void setC1(T c1) {
        this.c1 = c1;
    }

    public V getC2() {
        return c2;
    }

    public void setC2(V c2) {
        this.c2 = c2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coppia<?, ?> coppia = (Coppia<?, ?>) o;
        return Objects.equals(c1, coppia.c1) && Objects.equals(c2, coppia.c2);
    }
}
