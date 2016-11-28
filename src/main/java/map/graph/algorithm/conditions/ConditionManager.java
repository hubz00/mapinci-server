package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;


/**
 * if primary conditions are applied and met
 * normal condition doesn't have to be met
 * and the node in shape shouldn't be changed
 */
public class ConditionManager {

    private List<Condition> conditions;
    private List<PrimaryCondition> primaryConditions;

    public ConditionManager(){
        conditions = new LinkedList<>();
        primaryConditions = new LinkedList<>();
    }

    public boolean checkConditions(SegmentSoul graphSegment, SegmentSoul mapSegment){
//        if(primaryConditions.parallelStream().allMatch(c -> c.apply(graphSegment,mapSegment))){
//               return primaryConditions.parallelStream().allMatch(c -> c.meet(graphSegment,mapSegment));
//        }

       return conditions.parallelStream()
                .allMatch(c -> c.meet(graphSegment,mapSegment));
    }

    public void addCondition(Condition c){
        this.conditions.add(c);
    }
    public void addPrimaryCondition(PrimaryCondition c){
        this.primaryConditions.add(c);
    }
}
