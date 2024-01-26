package tesi;

public class ObjectPDDL extends ObjectModel{
    public ObjectPDDL(String name, Type type){
        super(name, type);
    }
    public String phrase(){
        if(super.getType().getName().equals("object"))
            return super.getName();
        else if(!super.getType().getSuperclass().equals("object"))
            return getName() + " - " + super.getType().getName();
        else
            return super.getName();

    }
}
