package map.graph;

import map.graph.graphElements.*;
import map.graph.graphElements.segments.Segment;
import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.OsmObject;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class App {

    public static void main(String[] args) throws IOException, OsmXmlParserException {
        map.graph.graphElements.OsmFetcher gf = new map.graph.graphElements.OsmFetcher();
        DataSculptor ds = new DataSculptor();
        IndexedRoot<Query> index = gf.makeGraph("andorra-latest.osm");

        Map<OsmObject, Float> hits = ds.narrowDown(42.5110129,42.5209083,1.544432,1.527749, index);

        Graph g = ds.rebuildGraph(index,hits);


        Collection<Segment> segments = g.getSegments().values();

        segments.forEach(System.out::println);
    }


}
