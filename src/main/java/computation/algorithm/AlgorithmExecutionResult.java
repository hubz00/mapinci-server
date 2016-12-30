package computation.algorithm;


import computation.graphElements.Node;
import computation.graphElements.segments.Segment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlgorithmExecutionResult {

    private Map<Node, List<Segment>> pathsToEndNodes;
    private Node startNode;
    private Map<Node, AlgorithmExecutionResult> nextSegmentPaths;

    public AlgorithmExecutionResult(Map<Node, List<Segment>> pathsToEndNodes, Node startNode) {
        this.pathsToEndNodes = pathsToEndNodes;
        this.startNode = startNode;
        this.nextSegmentPaths = new ConcurrentHashMap<>();
    }

    public Map<Node, List<Segment>> getPathsToEndNodes() {
        return pathsToEndNodes;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Map<Node, AlgorithmExecutionResult> getNextSegmentPaths() {
        return nextSegmentPaths;
    }

    public AlgorithmExecutionResult getResultsForEndNode(Node n){
        return nextSegmentPaths.get(n);
    }

    public void addResultForNode(Node n, AlgorithmExecutionResult result){
        this.nextSegmentPaths.put(n,result);
    }
    public void removeResultForNode(Node n){
        this.nextSegmentPaths.remove(n);
    }
}
