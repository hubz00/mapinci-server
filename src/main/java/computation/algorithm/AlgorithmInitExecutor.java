package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.*;
import computation.graphElements.Vector;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.ReferenceRotor;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class AlgorithmInitExecutor implements Callable<List<List<Segment>>>{

    private List<SegmentSoul> shape;
    private Node startNode;
    private ConditionManager conditionManager;
    private int graphKey;
    private Logger log;
    private AlgorithmExecutionResult algorithmResult;


    public AlgorithmInitExecutor(List<SegmentSoul> shape, Node startNode, ConditionManager conditionManager, int graphKey){
        this.shape = shape;
        this.startNode = startNode;
        this.conditionManager = conditionManager;
        this.graphKey = graphKey;
        this.log = Logger.getLogger(this.toString() + this.hashCode());
    }

    @Override
    public List<List<Segment>> call() throws Exception {
        //todo initialize new call for next segment
        ReferenceRotor referenceRotor = new ReferenceRotor();
        ExecutorService executorService = ComputationDispatcher.executorService;
        Map<Node,Future<List<List<Segment>>>> futures = new HashMap<>();
        Map<Node,List<Segment>> foundSegments = new HashMap<>();
        Graph graph = ComputationDispatcher.getGraph(graphKey);

        if(shape.size() == 0){
            log.info("Null will be thrown");
        }



        //if first call - >  rotate shape to fit segment

        List<Segment> initialSegmentsFromMap = graph.getSegmentsForNode(startNode);
        initialSegmentsFromMap.forEach(segment -> {
            Vector shapeVector = shape.get(0).getVector1();
            this.shape = referenceRotor.rotateShapeToFit(shape, segment.getVectorFromNode(startNode), shapeVector );
            EndNodesPredictor endNodesPredictor = new EndNodesPredictor(graph,conditionManager);
            Map<Node, List<Segment>> potentialNodes = endNodesPredictor.getNodes(startNode,segment,shape.get(0), shapeVector);

            potentialNodes.entrySet().forEach(entry -> {
                if(!foundSegments.containsKey(entry.getKey()) || foundSegments.containsKey(entry.getKey()) && foundSegments.get(entry.getKey()).size() > entry.getValue().size())
                    foundSegments.put(entry.getKey(), entry.getValue());
                });

            if(!shape.subList(1, shape.size()).isEmpty())
                potentialNodes.keySet().forEach(n -> {
                    List<SegmentSoul> tmp = referenceRotor.rotateShapeToFit(shape, new Vector(startNode,n), shapeVector);
                    futures.put(n,executorService.submit(new AlgorithmExecutor(new LinkedList<>(tmp.subList(1, shape.size())), n,conditionManager, graphKey, algorithmResult)));
                });
        });
        shape.remove(0);


        if(shape.isEmpty() && !foundSegments.isEmpty()){
            List<List<Segment>> result = new LinkedList<>();
            foundSegments.values().forEach(list -> result.add(new LinkedList<>(list)));
            return result;
        }else if(foundSegments.isEmpty()){
            futures.values().forEach(f -> f.cancel(true));
            return new LinkedList<>();
        }

        List<List<Segment>> potentialPaths = new LinkedList<>();
        while (!futures.entrySet().isEmpty()){
            try {
                Iterator<Map.Entry<Node, Future<List<List<Segment>>>>> iterator = futures.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Node, Future<List<List<Segment>>>> futureEntry = iterator.next();
                    if (futureEntry.getValue().isDone()) {
                        if (futureEntry.getValue().get().isEmpty()) {
                            iterator.remove();
                        } else {
                            List<Segment> result = foundSegments.get(futureEntry.getKey());
                            for (List<Segment> list : futureEntry.getValue().get()) {
                                List<Segment> tmp = new LinkedList<>(result);
                                tmp.addAll(list);
                                potentialPaths.add(tmp);
                            }
                            iterator.remove();
                            futures.entrySet().parallelStream().forEach(entry -> entry.getValue().cancel(true));
                        }
                    } else if (futureEntry.getValue().isCancelled()) {
                        iterator.remove();
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //todo cancel or wait for the result
        log.info(String.format("\tReturning path: %s", potentialPaths));
        return potentialPaths;
    }
}
