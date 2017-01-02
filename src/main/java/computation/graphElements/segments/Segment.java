package computation.graphElements.segments;

import computation.graphElements.Node;
import computation.graphElements.Vector;
import computation.graphElements.VectorContainer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Segment implements SegmentSoul{

    private final long id;
    private final Node n1;
    private final Node n2;
    private final Double length;
    private Double percentLength;
    private Double slope;
    private HashMap<Long, Vector> vectors;


    public Segment(Long id, Node n1, Node n2, Double percentLength){
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;
        this.vectors = new HashMap<>();
        this.percentLength = percentLength;
        this.length = calculateLength();
        setVectorsAndSlope();
    }

    protected Segment(Long id, Node n1, Node n2) {
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;
        this.vectors = new HashMap<>();
        this.length = calculateLength();
        setVectorsAndSlope();
    }

    protected Segment(long id, Node n1, Node n2, Double length, Double percentLength, Double slope) {
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;
        this.length = length;
        this.percentLength = percentLength;
        this.slope = slope;
    }

    private void setVectorsAndSlope() {
        vectors.put(n1.getId(), new Vector(n1,n2));
        vectors.put(n2.getId(), new Vector(n2,n1));
        slope = vectors.get(n1.getId()).getY()/vectors.get(n1.getId()).getX();
    }

    public boolean containsNode(Node n){
        return n1.equals(n) || n2.equals(n);
    }

    public Long getId() {
        return id;
    }

    @Override
    public Vector getVector1() {
        return getVectorFromNode(n1);
    }

    @Override
    public Vector getVector2() {
        return getVectorFromNode(n2);
    }

    public Node getNode1() {
        return n1;
    }

    public Node getNode2() {
        return n2;
    }

    public Double getSlope() {
        return slope;
    }

    public Double getLength() {
        return length;
    }

    public boolean contains(Node n){
        return (n1.getId() == n.getId() || n2.getId() == n.getId());
    }

    public Node getNeighbour(Node n){
        if(n.getId() == n1.getId())
            return n2;
        else
            return n1;
    }

    public Vector getVectorFromNode(Node n){
        return vectors.get(n.getId());
    }

    public void setVectors(HashMap<Long, Vector> vectors) {
        this.vectors = vectors;
    }

    @Override public String toString() {
        return String.format("[%s - %s]\t[Slope: %s]\t[Length: %s]",n1 ,n2, slope, Math.round(length));
    }

    public Double getPercentLength() {
        return percentLength;
    }

    public void setPercentLength(Double percentLength) {
        this.percentLength = percentLength;
    }

    Double calculateLength() {
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(this.n2.getLatitude() - this.n1.getLatitude());
        Double lonDistance = Math.toRadians(this.n2.getLongitude() - this.n1.getLongitude());
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.n1.getLatitude())) * Math.cos(Math.toRadians(this.n1.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // convert to meters

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

    public Double getLengthToFind(){
        return 0.0;
    }

    public void changeLengthToFind(Double addedValue){
    }

    public List<Vector> getVectors(){
        return new LinkedList<>(vectors.values());
    }

    public Map<Long, Vector> getMapVectors(){
        return vectors;
    }


    public static Segment fromJson(JSONSegment jsonSegment) {

        Segment segment = new Segment(jsonSegment.getId(), jsonSegment.getN1(), jsonSegment.getN2(),
                jsonSegment.getLength(), jsonSegment.getPercentLength(), jsonSegment.getSlope());

        HashMap<Long, Vector> map = new HashMap<>();
        for (VectorContainer vectorContainer : jsonSegment.getVectors() ) {
            map.put(vectorContainer.getId(), vectorContainer.getVector());
        }

        segment.setVectors(map);
        return segment;
    }
}
