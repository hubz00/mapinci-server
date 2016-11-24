package serialized;

import se.kodapan.osm.domain.Node;

/**
 * Created by m on 23.11.16.
 */
public class NodeSerialized {
    private Long id;
    private double latitude;
    private double longitude;


    public NodeSerialized(Node node) {
        this.latitude = node.getLatitude();
        this.longitude = node.getLongitude();
        this.id = node.getId();
    }

    public Long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
