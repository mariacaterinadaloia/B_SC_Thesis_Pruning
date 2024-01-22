package tesi;

import java.util.Objects;

public class ObjectModel {
    private String name;
    private Type type;

    public ObjectModel(String name, Type type){
        this.name=name;
        this.type=type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String phrase(){
        if(type.getName().equals("object"))
            return "";
        else if(!type.getSuperclass().equals("object"))
            return name + " - " + type.getName();
        else
            return name;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectModel that = (ObjectModel) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }
}
