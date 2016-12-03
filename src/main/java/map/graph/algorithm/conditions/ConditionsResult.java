package map.graph.algorithm.conditions;

public class ConditionsResult {

    private boolean boolResult;
    private Double angleToRotate;
    private boolean enoughSpaceForAnotherSegment;

    public ConditionsResult(){
        this.angleToRotate = 0.0;
        this.boolResult = true;
        this.enoughSpaceForAnotherSegment = false;
    }

    public double getAngleToRotate() {
        return angleToRotate;
    }

    public synchronized void setAngleToRotate(double angleToRotate) {
        this.angleToRotate = angleToRotate;
    }

    public boolean areMet() {
        return boolResult;
    }

    public synchronized void setBoolResult(boolean boolResult) {
        this.boolResult = boolResult;
    }

    public boolean isEnoughSpaceForAnotherSegment() {
        return enoughSpaceForAnotherSegment;
    }

    public synchronized void setEnoughSpaceForAnotherSegment(boolean enoughSpaceForAnotherSegment) {
        this.enoughSpaceForAnotherSegment = enoughSpaceForAnotherSegment;
    }

}
