package computation.graphElements.segments;


import computation.graphElements.Node;
import computation.graphElements.VectorContainer;

public class JSONSegment {

    private long id;
    private Node n1;
    private Node n2;
    private Double length;
    private Double percentLength;
    private Double slope;
    private VectorContainer[] vectors;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Node getN1() {
        return n1;
    }

    public void setN1(Node n1) {
        this.n1 = n1;
    }

    public Node getN2() {
        return n2;
    }

    public void setN2(Node n2) {
        this.n2 = n2;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getPercentLength() {
        return percentLength;
    }

    public void setPercentLength(Double percentLength) {
        this.percentLength = percentLength;
    }

    public Double getSlope() {
        return slope;
    }

    public void setSlope(Double slope) {
        this.slope = slope;
    }

    public VectorContainer[] getVectors() {
        return vectors;
    }

    public void setVectors(VectorContainer[] vectors) {
        this.vectors = vectors;
    }
}
