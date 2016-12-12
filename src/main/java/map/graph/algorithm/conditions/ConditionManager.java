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
    private List<Condition> baseConditions;
    private List<PrimaryCondition> basePrimaryConditions;
    private List<PrimaryCondition> primaryConditions;
    private ConditionFactory conditionFactory;
    private Logger log;

    public ConditionManager(){
        conditionFactory = new ConditionFactory();
        conditions = new LinkedList<>();
        baseConditions = new LinkedList<>();
        primaryConditions = new LinkedList<>();
        basePrimaryConditions = new LinkedList<>();
        this.log = Logger.getLogger("Condition Manager");
    }

    public ConditionsResult checkConditions(SegmentSoul graphSegment, SegmentSoul mapSegment, boolean newSide){
        ConditionsResult result = new ConditionsResult();
        if(!primaryConditions.isEmpty() && primaryConditions.parallelStream().allMatch(c -> c.applicable(graphSegment,mapSegment))){
            log.info("\t[Primary conditions apply]");
               primaryConditions.parallelStream().forEach(c -> c.meet(graphSegment,mapSegment, result, newSide));
            if(!result.areMet()){
                log.info("\tReverting changes in conditions");
                primaryConditions.parallelStream().forEach(Condition::revertLastCheck);
            }
               return result;
        }
        conditions.parallelStream().forEach(c -> c.meet(graphSegment,mapSegment, result, newSide));
        log.info(String.format("\t[Conditions: %s]\n\t\t[Enough space for next one: %s]", result.areMet(), result.isEnoughSpaceForAnotherSegment()));
        if(!result.areMet()){
            log.info("\tReverting changes in conditions");
            conditions.parallelStream().forEach(Condition::revertLastCheck);
        }
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
}
