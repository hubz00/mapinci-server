package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.Segment;

import java.util.LinkedList;
import java.util.List;

public class ConditionManager {

    List<Condition> conditions;

    public ConditionManager(){
        conditions = new LinkedList<>();
    }

    public ConditionsResult checkConditions(Segment graphSegment, Segment mapSegment){
        ConditionsResult cR = new ConditionsResult();
        cR.setBoolResult(conditions.parallelStream()
                .allMatch(c -> c.meet(graphSegment,mapSegment)));

        //todo fix me

        return cR;
    }

    public void addCondition(Condition c){
        this.conditions.add(c);
    }
}
