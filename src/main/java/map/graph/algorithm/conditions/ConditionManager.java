package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;


/**
 * If primary conditions are applied and met
 * normal condition doesn't have to be met
 * and the node in shape shouldn't be changed.
 */
public class ConditionManager {

    private List<Condition> conditions;
    private List<PrimaryCondition> primaryConditions;

    public ConditionManager(){
        conditions = new LinkedList<>();
        primaryConditions = new LinkedList<>();
    }

    public ConditionsResult checkConditions(SegmentSoul graphSegment, SegmentSoul mapSegment, boolean newSide){
        ConditionsResult result = new ConditionsResult();
//        if(primaryConditions.parallelStream().allMatch(c -> c.apply(graphSegment,mapSegment))){
//               return primaryConditions.parallelStream().allMatch(c -> c.meet(graphSegment,mapSegment));
//        }
        conditions.parallelStream().forEach(c -> c.meet(graphSegment,mapSegment, result, newSide));
        return result;
    }

    public void addCondition(Condition c){
        this.conditions.add(c);
    }
    public void addPrimaryCondition(PrimaryCondition c){
        this.primaryConditions.add(c);
    }
}
