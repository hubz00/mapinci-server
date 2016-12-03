package mapinci;

import map.graph.DataSculptor;
import map.graph.graphElements.Node;
import map.graph.graphElements.OsmFetcher;
import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.Way;
import se.kodapan.osm.domain.root.PojoRoot;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;
import serialized.NodeSerialized;
import serialized.WaySerialized;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by m on 05.11.16.
 */
public class GraphMaker {

    public GraphMaker(){};

    public ArrayList<Node> runApp() throws IOException, OsmXmlParserException {
        OsmFetcher osmFetcher = new OsmFetcher();
        DataSculptor ds = new DataSculptor();
        IndexedRoot<Query> index = osmFetcher.makeGraph("andorra-latest.osm");
        PojoRoot root = (PojoRoot) index.getDecorated();
//        serializeNodes(root);
//        serializeWays(root);


        //dlaczemu niektore ways sa nullami? zle parsowanie? co z tym zrobic?
        Map<Long, Way> ways = root.getWays();
        Way error = ways.get(45382981);

        System.out.println(error);
        System.out.println("cos");
        ArrayList<Node> nodes = new ArrayList<>();


        return nodes;
    }

//    public void serializeNodes(PojoRoot root) throws IOException {
//
//        Map<Long, se.kodapan.osm.domain.Node> nodes = root.getNodes();
//
//        Map<Long, NodeSerialized> serialized = new HashMap<>();
//
//        nodes.forEach((key, value) ->
//            serialized.put(key, new NodeSerialized(value))
//        );
//
//        Kryo kryo = new Kryo();
//        Output output = new Output(new FileOutputStream("nodes.bin"));
//        kryo.writeObject(output, serialized);
//        output.close();
//
//    }
//
//    public void serializeWays(PojoRoot root) throws IOException {
//
//        Map<Long, Way> ways = root.getWays();
//
//        Map<Long, WaySerialized> serialized = new HashMap<>();
//        long c = 0;
//
//        ways.values().forEach((value) -> {
//                    serialized.put(value.getId(), new WaySerialized(value));
//                }
//        );
//
//        Kryo kryo = new Kryo();
//        Output output = new Output(new FileOutputStream("ways.bin"));
//        kryo.writeObject(output, serialized);
//        output.close();
//
//    }

}
