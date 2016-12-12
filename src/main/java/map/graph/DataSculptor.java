package map.graph;

import de.westnordost.osmapi.map.data.Node;
import map.graph.graphElements.Graph;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import mapinci.osmHandling.MapFragment;

import javax.sound.midi.SysexMessage;
import java.util.Map;

public class DataSculptor {

    private final SegmentFactory segmentFactory;
    private final NodeFactory nodeFactory;
    private Graph graph;


    public DataSculptor(){
        this.segmentFactory = new SegmentFactory();
        this.nodeFactory = new NodeFactory();
    }

    public Graph rebuildGraph(MapFragment mapFragment){
        this.graph = new Graph();
        Map<Long, Node> nodeMap = mapFragment.getNodes();

        mapFragment.getWays().stream()
                .filter(way -> way.getNodeIds().size() > 1)
                .forEach(way ->
                        way.getNodeIds().stream()
                                .filter(id -> way.getNodeIds().indexOf(id) < way.getNodeIds().size() - 1)
                                .forEach(nodeId -> {
                                    map.graph.graphElements.Node currentNode = getNodeOrCreate(nodeMap.get(nodeId));
                                    Segment checkedSegment = segmentFactory.newSegment(currentNode, getNodeOrCreate(nodeMap.get(way.getNodeIds().get((way.getNodeIds().indexOf(nodeId) + 1)))));
                                    if (!graph.hasSegment(checkedSegment)) {
                                        graph.addSegment(checkedSegment);
                                    }
            })
        );
        return graph;
    }

    private map.graph.graphElements.Node getNodeOrCreate(Node tmpNode) {
        map.graph.graphElements.Node nn = this.graph.getNodeById(tmpNode.getId());
        if (nn != null)
            return nn;
        else
            return nodeFactory.newNodeFromLibNode(tmpNode);
    }
}
