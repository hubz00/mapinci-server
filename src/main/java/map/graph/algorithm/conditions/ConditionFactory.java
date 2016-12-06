package map.graph.algorithm.conditions;

public class ConditionFactory {

    public Condition newCondition(Double angleEpsilon){
        return new DirectionCondition(angleEpsilon);
    }

    public Condition newCondition(Double lengthEpsilon, Double overallLength){
        return new LengthCondition(lengthEpsilon, overallLength);
    }

    public PrimaryCondition newPrimaryCondition(Double lengthEpsilon, Double angleEpsilon){
        return new PaddingCondition(lengthEpsilon, angleEpsilon);
    }

}
