package map.graph.graphElements;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Random;

public final class NodeFactory {

    private final HashMap<Long, String> ids;

    public NodeFactory(){
        this.ids = new HashMap<>();
    }


    public Node newNodeFromLibNode(se.kodapan.osm.domain.Node n){
        if(ids.containsKey(n.getId())){
            //todo: throw error
            System.out.println("Node with id: " + n.getId() + " already exists");
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
