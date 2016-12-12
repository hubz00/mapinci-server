package map.graph.graphElements;

import com.sun.media.sound.InvalidDataException;

import java.util.HashMap;
import java.util.Random;

public final class NodeFactory {

    private final HashMap<Long, String> ids;

    public NodeFactory(){
        this.ids = new HashMap<>();
    }


    public synchronized Node newNodeFromLibNode(de.westnordost.osmapi.map.data.Node n) throws InvalidDataException {
        if(ids.containsKey(n.getId())){
            throw new InvalidDataException("Duplicated node");
        }
        ids.put(n.getId(), "");
        return new Node(n.getId(), n);
    }

    public Node newNode(Double longitude, Double latitude){
        Random r = new Random();
        Long id = r.nextLong();
        while (ids.containsKey(id)){
            id = r.nextLong();
        }
        ids.put(id,"");
        return new Node(id,longitude, latitude);
    }

}
