package map.graph.graphElements;

import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.root.PojoRoot;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;
import se.kodapan.osm.parser.xml.instantiated.InstantiatedOsmXmlParser;

import java.io.IOException;

public class OsmFetcher {

    public IndexedRoot<Query> makeGraph(String mapsName) throws IOException, OsmXmlParserException {
        PojoRoot root = new PojoRoot();
        IndexedRoot<Query> index = IndexedRoot.newInstance(root);
        index.open();

        InstantiatedOsmXmlParser parser = InstantiatedOsmXmlParser.newInstance();
        parser.setRoot(index);
        parser.parse(getClass().getResourceAsStream("/" + mapsName));
        index.commit();

        System.out.println(root.getNodes().size());

        return index;
    }

}
