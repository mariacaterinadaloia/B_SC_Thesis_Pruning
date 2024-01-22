package tesi;

import java.util.ArrayList;

public class Domain {
        private String name;
        private ArrayList<String> requirements;
        private ArrayList<Action> actions = new ArrayList<Action>();
        private ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        private ArrayList<Type> types = new ArrayList<Type>();
        private ArrayList<String> costants = new ArrayList<String>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public ArrayList<String> getCostants() {
                return costants;
        }

        public void setCostants(ArrayList<String> costants) {
                this.costants = costants;
        }

        public ArrayList<Type> getTypes() {
                return types;
        }

        public void setTypes(ArrayList<Type> types) {
                this.types = types;
        }

        public ArrayList<Predicate> getPredicates() {
                return predicates;
        }

        public void setPredicates(ArrayList<Predicate> predicates) {
                this.predicates = predicates;
        }

        public ArrayList<Action> getActions() {
                return actions;
        }

        public void setActions(ArrayList<Action> actions) {
                this.actions = actions;
        }

        public ArrayList<String> getRequirements() {
                return requirements;
        }

        public void setRequirements(ArrayList<String> requirements) {
                this.requirements = requirements;
        }
}

