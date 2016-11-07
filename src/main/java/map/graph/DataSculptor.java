package map.graph;

import map.graph.graphElements.*;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherFactory;
import se.kodapan.osm.domain.*;
import se.kodapan.osm.domain.Node;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataSculptor {

    private SegmentFactory segmentFactory;

    public DataSculptor(){
        this.segmentFactory = new SegmentFactory();
    }

    public Map<OsmObject, Float> narrowDown(double south, double north, double east, double west, IndexedRoot<Query> index){
        Map<OsmObject, Float> result = new HashMap<OsmObject, Float>();

        BooleanQuery bq = new BooleanQuery();

        //       bq.add(index.getQueryFactories().containsTagKeyQueryFactory().setKey("highway").build(), BooleanClause.Occur.MUST);
        bq.add(index.getQueryFactories().nodeEnvelopeQueryFactory()
                .setSouthLatitude(south).setWestLongitude(west)
                .setNorthLatitude(north).setEastLongitude(east)
                .build(), BooleanClause.Occur.MUST);

        try {
            result = index.search(bq);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    public Graph rebuildGraph(IndexedRoot<Query> index, Map<OsmObject, Float> map){
        Graph graph = new Graph();
        NodeFactory nf = new NodeFactory();

        for (OsmObject entry : map.keySet()){
            se.kodapan.osm.domain.Node tmp = index.getNode(entry.getId());
            map.graph.graphElements.Node currentNode = getNodeOrCreate(graph, nf, tmp);
            List<Way> ways = tmp.getWaysMemberships();

            if(ways != null) {
                HashMap<Long, map.graph.graphElements.Node> addedNodes = new HashMap<>();

                ways.stream()
                        .filter(w -> w.getNodes().size() > 1)
                        .filter(way -> (way.getNodes().indexOf(tmp)) + 1 < way.getNodes().size())
                        .forEach(way -> {

                    map.graph.graphElements.Node n = getNodeOrCreate(graph, nf, way.getNodes().get(way.getNodes().indexOf(tmp) + 1));

                    if (!addedNodes.containsKey(n.getId())) {
                        Segment tmpSegment = segmentFactory.newFullSegment(currentNode, n);
                        if (!graph.hasSegment(tmpSegment)) {
                            graph.addSegment(tmpSegment);
                            addedNodes.put(n.getId(), n);
                        }
                    }
                });
            }
        }
        return graph;
    }

    private map.graph.graphElements.Node getNodeOrCreate(Graph graph, NodeFactory nf, Node tmpNode) {
        map.graph.graphElements.Node n;
        map.graph.graphElements.Node nn = graph.getNodeById(tmpNode.getId());
        if (nn != null)
            n = nn;
        else
            n = nf.newNodeFromLibNode(tmpNode);
        return n;
    }
}
