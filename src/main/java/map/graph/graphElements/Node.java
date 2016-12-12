package map.graph.graphElements;

import java.util.Comparator;

public class Node implements Comparable<Node>{

    private long id;
    private Double longitude;
    private Double latitude;

    protected Node (long id, de.westnordost.osmapi.map.data.Node n){
        this.id = id;
        initiateWithNode(n);
    }

    protected Node() {}

    protected Node(Long id, Double longitude, Double latitude){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private void initiateWithNode(de.westnordost.osmapi.map.data.Node n) {
        this.longitude = n.getPosition().getLongitude();
        this.latitude = n.getPosition().getLatitude();
    }

    public long getId() {
        return id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public int compareTo(Node n) {
        if(id == n.getId())
            return 0;
        else
            return id > n.getId() ? 1 : -1;
    }
}
