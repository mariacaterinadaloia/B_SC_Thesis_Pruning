package tesi;

public class Type {
    private String name;
    private String superclass;

    Type(String name, String supeerclass){
        this.name = name;
        this.superclass = supeerclass;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String phrase(){
        if(name.equals("object"))
            return "";
        String s = "";
        s += name;
        if(!superclass.equals("object"))
            s += " - " + superclass;
        return s;
    }
}
