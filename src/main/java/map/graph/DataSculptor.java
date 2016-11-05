package map.graph;

import map.graph.graphElements.*;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.*;
import se.kodapan.osm.domain.Node;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;

import java.io.IOException;
import java.util.*;

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
            if(graph.ge)
            map.graph.graphElements.Node currentNode = nf.newNodeFromLibNode(tmp);
            List<Way> ways = tmp.getWaysMemberships();
            List<Segment> segments = new LinkedList<Segment>();

            List<map.graph.graphElements.Node> currentNeighbours = graph.getNeighbours(currentNode);

            int connections = ways.size();

            if(connections == currentNeighbours.size())
                continue;
            else {
                connections -= currentNeighbours.size();
            }

            for (int i = 0; i < connections; i++){
                Segment segment = segmentFactory.newHalfSegment(currentNode);
                segments.add(segment);
            }

            Iterator<Segment> it = segments.iterator();
            Segment tmpSegment;
            Node tmpNode;

            for(Way way: ways){
                if ((way.getNodes().indexOf(tmp)) + 1 <  way.getNodes().size()){
                    tmpNode = way.getNodes().get(way.getNodes().indexOf(tmp) + 1);
                    map.graph.graphElements.Node n = nf.newNodeFromLibNode(tmpNode);

                    if(currentNeighbours.contains(n)){
                        currentNeighbours.remove(n);
                    }
                    else {
                        if (it.hasNext()) {
                            tmpSegment = it.next();
                            tmpSegment.setNode2(n);
                            graph.addSegment(tmpSegment);
                        } else {
                            // todo: throw error
                            System.out.println("no more segments without pairs !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    }
                }
            }
            if(it.hasNext()){
                // todo: throw error
                System.out.println("Not all neighbours assigned for node: " + currentNode);
            }
        }

        return graph;
    }
}
