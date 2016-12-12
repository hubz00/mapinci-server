package mapinci.osmHandling;

import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapStreamHandler implements MapDataHandler {

    private Map<Long, Node> nodes;
    private LinkedList<Way> ways;

    public MapStreamHandler(){
        this.nodes = new HashMap<>();
        this.ways = new LinkedList<>();
    }

    @Override
    public void handle(BoundingBox boundingBox) {

    }

    @Override
    public void handle(Node node) {
        this.nodes.put(node.getId(), node);
    }

    @Override
    public void handle(Way way) {
        this.ways.add(way);
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
