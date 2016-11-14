package mapinci;

import map.graph.DataSculptor;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.Segment;
import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.OsmObject;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by m on 07.11.16.
 */
public class GraphSaver {

    public static void main(String[] args) throws IOException, OsmXmlParserException {

        GraphMaker gm = new GraphMaker();

        ArrayList<Node> result = gm.runApp();


    }

}
