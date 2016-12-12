package map.graph;

import com.sun.media.sound.InvalidDataException;
import de.westnordost.osmapi.map.data.Node;
import map.graph.graphElements.Graph;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import mapinci.osmHandling.MapFragment;

import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DataSculptor {

    private final SegmentFactory segmentFactory;
    private final NodeFactory nodeFactory;
    private Map<Long, List<Long>> addedSegments;
    private Graph graph;
    private Logger log;
    private int index;


    public DataSculptor(){
        this.segmentFactory = new SegmentFactory();
        this.nodeFactory = new NodeFactory();
        this.addedSegments = new HashMap<>();
        this.log = Logger.getLogger("DataSculptor");
        index = 0;
    }

    public Graph rebuildGraph(MapFragment mapFragment){
        this.graph = new Graph();
        log.info("Rebuild Started");
        Map<Long, Node> nodeMap = mapFragment.getNodes();
        mapFragment.getWays().parallelStream()
                .forEach(way ->{
                        iteration();
                        way.getNodeIds().stream()
                                .filter(id ->  way.getNodeIds().indexOf(id) < way.getNodeIds().size() - 1)
                                .forEach(nodeId -> {
                                    map.graph.graphElements.Node currentNode = getNodeOrCreate(nodeMap.get(nodeId));
                                    map.graph.graphElements.Node nextNode = getNodeOrCreate(nodeMap.get(way.getNodeIds().get((way.getNodeIds().indexOf(nodeId) + 1))));
                                    if (!checkIfSegmentAdded(currentNode.getId(), nextNode.getId())) {
                                        Segment checkedSegment = segmentFactory.newSegment(currentNode, nextNode);
                                        graph.addSegment(checkedSegment);
                                    }
            });}        );
        log.info("Rebuild Finished");
        return graph;
    }


    private synchronized void iteration(){
        if(index%500 == 0)
            log.info("" + index);
        index++;
    }

    private synchronized boolean checkIfSegmentAdded(Long nodeId1, Long nodeId2){
        boolean result = false;
        if(addedSegments.containsKey(nodeId1)){
            if(addedSegments.get(nodeId1).contains(nodeId2)){
                result = true;
            }
            else {
                addedSegments.get(nodeId1).add(nodeId2);
            }
        }
        else {
            addedSegments.put(nodeId1, new LinkedList<>());
            addedSegments.get(nodeId1).add(nodeId2);
        }

        if(addedSegments.containsKey(nodeId2)){
            if(addedSegments.get(nodeId2).contains(nodeId1)){
                result = true;
            }
            else {
                addedSegments.get(nodeId2).add(nodeId1);
            }
        }
        else {
            addedSegments.put(nodeId2, new LinkedList<>());
            addedSegments.get(nodeId2).add(nodeId1);
        }


        return result;
    }

    private map.graph.graphElements.Node getNodeOrCreate(Node tmpNode) {
        map.graph.graphElements.Node nn = this.graph.getNodeById(tmpNode.getId());
        if (nn != null)
            return nn;
        else
            try {
                return nodeFactory.newNodeFromLibNode(tmpNode);
            } catch (InvalidDataException e) {
            //todo
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            }
            return null;
    }
}
