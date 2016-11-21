package map.graph.graphElements.segments;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;


public interface Segment {

    Node getNeighbour(Node n);
    Double getSlope();
    Double getLength();
    boolean contains(Node n);
    Vector getVectorFromNode(Node n);
    long getId();
    Node getNode1();
    Node getNode2();
    int compareTo(Segment s);
    void setNode2(Node n2);
}
