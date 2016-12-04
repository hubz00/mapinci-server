package map.graph.algorithm.conditions;

public class ConditionFactory {

    public Condition newCondition(Double epsilon){
        return new DirectionCondition(epsilon);
    }

    public Condition newCondition(Double epsilon, Double overallLength){
        return new LengthCondition(epsilon, overallLength);
    }

    public PrimaryCondition newPrimaryCondition(Double lengthEpsilon, Double angleEpsilon){
        return new PaddingCondition(lengthEpsilon, angleEpsilon);
    }

}
