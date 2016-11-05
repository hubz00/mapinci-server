package map.graph.graphElements;

public final class NodeFactory {


    public Node newNodeFromLibNode(se.kodapan.osm.domain.Node n){
        return new Node(n.getId(), n);
    }

}
