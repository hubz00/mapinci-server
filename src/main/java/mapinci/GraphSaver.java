package mapinci;

import map.graph.graphElements.Node;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by m on 07.11.16.
 */
public class GraphSaver {

    public static void main(String[] args) throws IOException, OsmXmlParserException {

        GraphMaker gm = new GraphMaker();

        ArrayList<Node> result = gm.runApp();


    }

}
