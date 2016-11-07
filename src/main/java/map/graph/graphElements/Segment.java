package map.graph.graphElements;

public class Segment implements Comparable<Segment> {

    private final long id;
    private Node n1;
    private Node n2;
    private Double length;

    protected Segment(Long id, Node n1){
        this.id = id;
        this.n1 = n1;
        this.n2 = null;
        this.length = 0.0;
    }

    protected Segment(Long id, Node n1, Node n2) {
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;
        calculateLength();
    }

    private void calculateLength() {
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(n2.getLatitude() - n1.getLatitude());
        Double lonDistance = Math.toRadians(n2.getLongitude() - n1.getLongitude());
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(n1.getLatitude())) * Math.cos(Math.toRadians(n1.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        this.length = R * c * 1000; // convert to meters

    }

    public int compareTo(Segment o) {
        if((n1.getId() == o.getNode1().getId() && n2.getId() == o.getNode2().getId())
                || (n2.getId() == o.getNode1().getId() && n1.getId() == o.getNode2().getId()))
            return 0;
        else if (length >= o.getLength())
            return 1;
        else
            return -1;
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
        if (this.n1 != null)
            calculateLength();
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

    @Override public String toString() {
        return "-- " + n1 + " ---- " + n2 + " --" + " length: " + length + "m";
    }
}
