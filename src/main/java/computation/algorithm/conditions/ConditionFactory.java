package computation.algorithm.conditions;

public class ConditionFactory {

    public Condition newDirectionCondition(Double angleEpsilon){
        return new DirectionCondition(angleEpsilon);
    }

    public Condition newLengthCondition(Double lengthEpsilon){
        return new LengthCondition(lengthEpsilon);
    }

    public Condition copyCondition(Condition c){
        if(c instanceof DirectionCondition){
            return new DirectionCondition(((DirectionCondition) c).getEpsilon());
        }
        else if (c instanceof LengthCondition){
            return new LengthCondition(((LengthCondition) c).getEpsilon());
        }
        else return null;
    }

    public PrimaryCondition newPrimaryCondition(Double lengthEpsilon, Double angleEpsilon){
        return new PaddingCondition(lengthEpsilon, angleEpsilon);
    }

    public PrimaryCondition copyPrimaryCondition(PrimaryCondition p){
        if(p instanceof PaddingCondition){
            return new PaddingCondition(((PaddingCondition) p).getLengthEpsilon(), ((PaddingCondition) p).getSlopeEpsilon());
        }
        else return null;
    }

}
