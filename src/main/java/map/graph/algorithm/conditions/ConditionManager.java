package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.Segment;

import java.util.LinkedList;
import java.util.List;

public class ConditionManager {

    List<Condition> conditions;

    public ConditionManager(){
        conditions = new LinkedList<>();
    }

    public boolean checkConditions(Segment graphSegment, Segment mapSegment){
       return conditions.parallelStream()
                .allMatch(c -> c.meet(graphSegment,mapSegment));
        //todo fix me
    }

    public void addCondition(Condition c){
        this.conditions.add(c);
    }
}
