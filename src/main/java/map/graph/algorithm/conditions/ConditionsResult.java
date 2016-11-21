package map.graph.algorithm.conditions;

public class ConditionsResult {

    private boolean boolResult;
    private double angleToRotate;

    public ConditionsResult(){
        angleToRotate = 0;
    }

    public double getAngleToRotate() {
        return angleToRotate;
    }

    public void setAngleToRotate(double angleToRotate) {
        this.angleToRotate = angleToRotate;
    }

    public boolean areMet() {
        return boolResult;
    }

    public void setBoolResult(boolean boolResult) {
        this.boolResult = boolResult;
    }
}
