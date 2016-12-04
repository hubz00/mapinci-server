package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

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
    private List<PrimaryCondition> primaryConditions;
    private Logger log;

    public ConditionManager(){
        conditions = new LinkedList<>();
        primaryConditions = new LinkedList<>();
        this.log = Logger.getLogger("Condition Manager");
    }

    public ConditionsResult checkConditions(SegmentSoul graphSegment, SegmentSoul mapSegment, boolean newSide){
        ConditionsResult result = new ConditionsResult();
        if(!primaryConditions.isEmpty() && primaryConditions.parallelStream().allMatch(c -> c.applicable(graphSegment,mapSegment))){
            log.info("Applicable");
               primaryConditions.parallelStream().forEach(c -> c.meet(graphSegment,mapSegment, result, newSide));
               return result;
        }
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
