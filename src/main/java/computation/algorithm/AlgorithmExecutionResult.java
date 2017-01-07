package computation.algorithm;


import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlgorithmExecutionResult {

    private Map<Node, List<Segment>> pathsToEndNodes;
    private Node startNode;
    private boolean finished;
    private Map<Node, List<AlgorithmExecutionResult>> nextSegmentResults;
    private Logger logger = LoggerFactory.getLogger(AlgorithmExecutionResult.class);

    public AlgorithmExecutionResult(Node startNode) {
        this.pathsToEndNodes = new ConcurrentHashMap<>();
        this.startNode = startNode;
        this.finished = false;
        this.nextSegmentResults = new ConcurrentHashMap<>();
    }

    public Map<Node, List<Segment>> getPathsToEndNodes() {
        return pathsToEndNodes;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Map<Node, List<AlgorithmExecutionResult>> getResultsForNextSegments() {
        return nextSegmentResults;
    }

    public List<AlgorithmExecutionResult> getResultsForNextSegments(Node n){
        return nextSegmentResults.get(n);
    }

    public void addPathForNode(Node n, List<Segment> newPath){
        if(pathsToEndNodes.containsKey(n)){
            if(pathsToEndNodes.get(n).size() > newPath.size()){
                pathsToEndNodes.put(n,newPath);
            }
        }
        else {
            pathsToEndNodes.put(n, newPath);
        }
    }

    public void setPathsToEndNodes(Map<Node, List<Segment>> map){
        this.pathsToEndNodes = new ConcurrentHashMap<>(map);
    }
    public void addResultForNode(Node n, AlgorithmExecutionResult result){
        if(nextSegmentResults.containsKey(n)){
            synchronized (nextSegmentResults.get(n)) {
                this.nextSegmentResults.get(n).add(result);
            }
        }
        else {
            List<AlgorithmExecutionResult> tmp = Collections.synchronizedList(new LinkedList<>());
            synchronized (tmp) {
                tmp.add(result);
                this.nextSegmentResults.put(n, tmp);
            }
        }

    }

    public void removeResultForNode(Node n){
        this.nextSegmentResults.remove(n);
    }

    public List<Segment> getPathforEndNode(Node n){
        return pathsToEndNodes.get(n);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }
}
