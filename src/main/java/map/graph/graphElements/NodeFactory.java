package map.graph.graphElements;

import com.sun.media.sound.InvalidDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One Node Factory instance creates Nodes for given Graph to stay thread-safe
 * if no Graph given behaves like normal factory
 */
public final class NodeFactory {

    private final Map<Long, String> ids;
    private final Graph graph;

    public NodeFactory(Graph graph){
        this.graph = graph;
        this.ids = new ConcurrentHashMap<>();
    }

    public NodeFactory(){
        this.graph = null;
        this.ids = new ConcurrentHashMap<>();
    }


    public synchronized Node newNodeFromLibNode(de.westnordost.osmapi.map.data.Node n) throws InvalidDataException {
        if(ids.containsKey(n.getId())){
            throw new InvalidDataException("Duplicated node");
        }
        ids.put(n.getId(), "");
        Node createdNode = new Node(n.getId(), n);
        if(graph != null)
            graph.addNode(createdNode);
        return createdNode;
    }

    public synchronized Node newNode(Double longitude, Double latitude){
        Random r = new Random();
        Long id = r.nextLong();
        while (ids.containsKey(id)){
            id = r.nextLong();
        }

        Node createdNode = new Node(id,longitude, latitude);
        ids.put(id,"");
        if(graph != null)
            graph.addNode(createdNode);
        return createdNode;
    }

}
