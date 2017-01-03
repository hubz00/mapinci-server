package computation.algorithm.conditions;

import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;


/**
 * If primary conditions are applied and met
 * normal condition doesn't have to be met
 * and the node in shape shouldn't be changed.
 */
public class ConditionManager {

    private List<Condition> conditions;
    private List<Condition> baseConditions;
    private List<PrimaryCondition> basePrimaryConditions;
    private List<PrimaryCondition> primaryConditions;
    private ConditionFactory conditionFactory;
    private Double lengthEpsilon;

    public ConditionManager(){
        conditionFactory = new ConditionFactory();
        conditions = new LinkedList<>();
        baseConditions = new LinkedList<>();
        primaryConditions = new LinkedList<>();
        basePrimaryConditions = new LinkedList<>();
    }

    public ConditionManager(ConditionManager cm){
        conditionFactory = new ConditionFactory();
        conditions = cm.getBaseConditions();
        baseConditions = cm.getBaseConditions();
        primaryConditions = cm.getPrimaryConditions();
        basePrimaryConditions = cm.getPrimaryConditions();
    }

    public ConditionsResult checkConditions(SegmentSoul graphSegment, SegmentSoul mapSegment, Vector shapeVector, Vector mapVector){
        ConditionsResult result = new ConditionsResult();
        if(!primaryConditions.isEmpty() && primaryConditions.stream().allMatch(c -> c.applicable(graphSegment,mapSegment))){
            primaryConditions.parallelStream().forEach(c -> c.meet(graphSegment,mapSegment, result, shapeVector , mapVector));
            return result;
        }
        conditions.forEach(c -> c.meet(graphSegment,mapSegment, result, shapeVector, mapVector));
        return result;
    }

    public void reset(){
        this.conditions = this.baseConditions;
        this.primaryConditions = this.basePrimaryConditions;
    }

    public void addCondition(Condition c){
        this.conditions.add(c);
        this.baseConditions.add(conditionFactory.copyCondition(c));
    }

    public void addPrimaryCondition(PrimaryCondition c){
        this.primaryConditions.add(c);
        this.basePrimaryConditions.add(conditionFactory.copyPrimaryCondition(c));
    }

    public void simplifyConditions(){
        if(lengthEpsilon != null)
            lengthEpsilon *= 1.3;
        baseConditions.parallelStream().forEach(Condition::simplify);
        basePrimaryConditions.parallelStream().forEach(PrimaryCondition::simplify);
        this.conditions = this.baseConditions;
        this.primaryConditions = this.basePrimaryConditions;
    }

    public List<Condition> getBaseConditions() {
        return baseConditions;
    }

    public List<PrimaryCondition> getPrimaryConditions(){
        return basePrimaryConditions;
    }

    public Double getLengthEpsilon() {
        return lengthEpsilon;
    }

    public void setLengthEpsilon(Double lengthEpsilon) {
        this.lengthEpsilon = lengthEpsilon;
    }
}
