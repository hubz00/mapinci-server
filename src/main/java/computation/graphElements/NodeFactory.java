package computation.graphElements;

import com.sun.media.sound.InvalidDataException;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One Node Factory instance creates Nodes for given Graph to stay thread-safe
 * if no Graph given behaves like normal factory
 */
public final class NodeFactory {

    private final Map<Long, String> IDS;
    private final Graph GRAPH;

    public NodeFactory(Graph graph){
        this.GRAPH = graph;
        this.IDS = new ConcurrentHashMap<>();
    }

    public NodeFactory(){
        this.GRAPH = null;
        this.IDS = new ConcurrentHashMap<>();
    }


    public synchronized Node newNodeFromLibNode(de.westnordost.osmapi.map.data.Node n) throws InvalidDataException {
        if(IDS.containsKey(n.getId())){
            throw new InvalidDataException("Duplicated node");
        }
        IDS.put(n.getId(), "");
        Node createdNode = new Node(n.getId(), n);
        if(GRAPH != null)
            GRAPH.addNode(createdNode);
        return createdNode;
    }

    public synchronized Node newNode(Double longitude, Double latitude){
        Random r = new Random();
        Long id = r.nextLong();
        while (IDS.containsKey(id)){
            id = r.nextLong();
        }

        Node createdNode = new Node(id,longitude, latitude);
        IDS.put(id,"");
        if(GRAPH != null)
            GRAPH.addNode(createdNode);
        return createdNode;
    }

}
