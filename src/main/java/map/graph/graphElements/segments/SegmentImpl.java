package map.graph.graphElements.segments;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;

import java.util.HashMap;

public class SegmentImpl implements Segment {

    private long id;
    private Node n1;
    private Node n2;
    private Double length;
    private Double slope;
    private HashMap<Long, Vector> vectors;

    protected SegmentImpl(Long id, Node n1){
        this.id = id;
        this.n1 = n1;
        this.n2 = null;
        this.length = 0.0;
        this.vectors = new HashMap<>();
    }

    protected SegmentImpl(Long id, Node n1, Node n2) {
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;
        this.vectors = new HashMap<>();
        calculateLength();
        setVectorsAndSlope();
    }

    private void setVectorsAndSlope() {
        vectors.put(n1.getId(), new Vector(n1,n2));
        vectors.put(n2.getId(), new Vector(n2,n1));
        slope = vectors.get(n1.getId()).getY()/vectors.get(n1.getId()).getX();
    }

    protected SegmentImpl() {}


    public boolean containsNode(Node n){
        return n1.equals(n) || n2.equals(n);
    }

    public long getId() {
        return id;
    }

    public Node getNode1() {
        return n1;
    }

    public void setNode1(Node n1) {
        this.n1 = n1;
            if (this.n2 != null)
                calculateLength();
    }

    public Node getNode2() {
        return n2;
    }

    public void setNode2(Node n2) {
        this.n2 = n2;
        if (this.n1 != null && this.n2 != null) {
            calculateLength();
            setVectorsAndSlope();
        }
    }

    public Double getSlope() {
        return slope;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public boolean contains(Node n){
        return (n1.equals(n) || n2.equals(n));
    }

    public Node getNeighbour(Node n){
        return n.equals(n1)? n2 : n1;
    }

    public HashMap<Long, Vector> getVectors() {
        return vectors;
    }

    public Vector getVectorFromNode(Node n){
        return vectors.get(n.getId());
    }

    @Override public String toString() {
        return "-- " + n1 + " ---- " + n2 + " -- " + "[Slope: " + slope + "]";
    }

    protected void calculateLength() {
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(this.n2.getLatitude() - this.n1.getLatitude());
        Double lonDistance = Math.toRadians(this.n2.getLongitude() - this.n1.getLongitude());
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.n1.getLatitude())) * Math.cos(Math.toRadians(this.n1.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        this.length = R * c * 1000; // convert to meters

    }

    public int compareTo(Segment o) {
        if((this.n1.getId() == o.getNode1().getId() && this.n2.getId() == o.getNode2().getId())
                || (this.n2.getId() == o.getNode1().getId() && this.n1.getId() == o.getNode2().getId()))
            return 0;
        else if (this.length >= o.getLength())
            return 1;
        else
            return -1;
    }
}
