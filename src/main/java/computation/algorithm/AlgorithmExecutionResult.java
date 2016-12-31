package computation.algorithm;


import computation.graphElements.Node;
import computation.graphElements.segments.Segment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlgorithmExecutionResult {

    private Map<Node, List<Segment>> pathsToEndNodes;
    private Node startNode;
    private Map<Node, AlgorithmExecutionResult> nextSegmentResults;

    public AlgorithmExecutionResult(Node startNode) {
        this.pathsToEndNodes = new ConcurrentHashMap<>();
        this.startNode = startNode;
        this.nextSegmentResults = new ConcurrentHashMap<>();
    }

    public Map<Node, List<Segment>> getPathsToEndNodes() {
        return pathsToEndNodes;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Map<Node, AlgorithmExecutionResult> getResultsForNextSegments() {
        return nextSegmentResults;
    }

    public AlgorithmExecutionResult getResultsForNextSegments(Node n){
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
        this.nextSegmentResults.put(n,result);
    }

    public void removeResultForNode(Node n){
        this.nextSegmentResults.remove(n);
    }

    public List<Segment> getPathforEndNode(Node n){
        return pathsToEndNodes.get(n);
    }
}
