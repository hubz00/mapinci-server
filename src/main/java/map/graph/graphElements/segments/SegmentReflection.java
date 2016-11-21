package map.graph.graphElements.segments;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;

import java.util.HashMap;

public class SegmentReflection {

    private long id;
    private long correspondingSegmentId;
    private HashMap<Long, Vector> vectors;

    protected SegmentReflection(long id, Node n1, Vector v1, Node n2, Vector v2){
        this.id = id;
        this.vectors = new HashMap<>();
        vectors.put(n1.getId(), v1);
        vectors.put(n2.getId(), v2);
    }

    public long getCorrespondingSegmentId(){
        return correspondingSegmentId;
    }

    public Vector getVectorForNode(Node n){
        return vectors.get(n.getId());
    }

    public long getId() {
        return id;
    }
}
