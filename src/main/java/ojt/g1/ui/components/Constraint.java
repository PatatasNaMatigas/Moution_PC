package ojt.g1.ui.components;

public class Constraint {

    private ConstraintType constraintType;
    private Component component;

    public Constraint(ConstraintType constraintType, Component component) {
        this.constraintType = constraintType;
        this.component = component;
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public Component getComponent() {
        return component;
    }
}
