package mapinci;

import map.graph.DataSculptor;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import mapinci.osmHandling.MapFetcher;
import mapinci.osmHandling.MapFragment;

import java.util.ArrayList;

public class GraphMaker {


    public ArrayList<Node> runApp (Node n, Double searchNodeRadius) {

        MapFetcher mapFetcher = new MapFetcher();
        DataSculptor dataSculptor = new DataSculptor();

        MapFragment mapFragment = mapFetcher.fetch(n,searchNodeRadius);

        Graph mapGraph = dataSculptor.rebuildGraph(mapFragment);


        return new ArrayList<Node>();
    }

}
