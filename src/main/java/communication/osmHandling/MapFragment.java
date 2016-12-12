package communication.osmHandling;

import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Way;

import java.util.List;
import java.util.Map;

public class MapFragment {

    private Map<Long, Node> nodes;
    private List<Way> ways;

    public MapFragment(Map<Long, Node> nodes, List<Way> ways) {
        this.nodes = nodes;
        this.ways = ways;
    }

    public Map<Long, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Long, Node> nodes) {
        this.nodes = nodes;
    }

    public List<Way> getWays() {
        return ways;
    }

    public void setWays(List<Way> ways) {
        this.ways = ways;
    }
}
