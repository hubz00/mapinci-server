package map.graph.graphElements.segments;


import map.graph.graphElements.Node;

public class Nodes {

    private Node[] nodes;

    public Nodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public Nodes() {
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }
}
