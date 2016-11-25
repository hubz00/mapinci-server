package serialized;

import se.kodapan.osm.domain.Node;
import se.kodapan.osm.domain.Way;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 23.11.16.
 */
public class WaySerialized {
    private List<Long> nodesID = new ArrayList<>();

    public WaySerialized(Way way) {
        this.setNodesID(way);
    }

    private void setNodesID(Way way) {

        List<Node> nodes = way.getNodes();
        if (nodes == null) {
            System.out.println(way.getId() + " tu jest blad - nodes.size");
        }
        else {
            nodes.forEach((temp) -> {

                if(temp.getId() == null) {
                    System.out.println(way.getId() + " tu jest blad temp.getId");
                }
                else {
                    nodesID.add(temp.getId());
                }
            });
        }

    }

    public List<Long> getNodesID() {
        return nodesID;
    }
}
