package communication.osmHandling;

import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MapStreamHandler implements MapDataHandler {

    private Map<Long, Node> nodes;
    private LinkedList<Way> ways;
    private Long nodeHandleCalls;
    private List<String> wayTypes;

    MapStreamHandler(){
        this.nodes = new HashMap<>();
        this.ways = new LinkedList<>();
        this.nodeHandleCalls = 0L;
        this.wayTypes = new ArrayList<>();
        Collections.addAll(wayTypes,"pedestrian", "living_street", "cycleway", "footpath", "motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential", "motorway_link", "trunk_link", "primary_link", "secondary_link", "tertiary_link");
    }

    public Long getNodeHandleCalls() {
        return nodeHandleCalls;
    }

    @Override
    public void handle(BoundingBox boundingBox) {

    }

    @Override
    public void handle(Node node) {
        this.nodeHandleCalls++;
        if(!nodes.containsKey(node.getId())) {
            this.nodes.put(node.getId(), node);
        }
        else {
            System.out.println(String.format("Duplicated: %s", node.getId()));
        }
    }

    @Override
    public void handle(Way way) {
        if(way.getTags() != null && way.getTags().containsKey("highway") && wayTypes.contains(way.getTags().get("highway"))) {
            this.ways.add(way);
        }
    }

    @Override
    public void handle(Relation relation) {

    }

    public Map<Long, Node> getNodes() {
        return nodes;
    }

    public LinkedList<Way> getWays() {
        return ways;
    }
}
