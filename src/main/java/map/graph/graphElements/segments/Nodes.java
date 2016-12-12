package map.graph.graphElements.segments;


import map.graph.graphElements.Node;

import java.util.List;

public class Nodes {

    private List<Node> nodes;

    public Nodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public Nodes() {
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
