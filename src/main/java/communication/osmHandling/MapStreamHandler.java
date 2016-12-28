package communication.osmHandling;

import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapStreamHandler implements MapDataHandler {

    private Map<Long, Node> nodes;
    private LinkedList<Way> ways;
    private Long nodeHandleCalls;

    public MapStreamHandler(){
        this.nodes = new HashMap<>();
        this.ways = new LinkedList<>();
        this.nodeHandleCalls = 0L;
    }

    public Long getNodeHandleCalls() {
        return nodeHandleCalls;
    }

    @Override
    public void handle(BoundingBox boundingBox) {

    }

    @Override
    public void handle(Node node) {
        this.nodeHandleCalls++;
        if(!nodes.containsKey(node.getId())) {
            this.nodes.put(node.getId(), node);
        }
        else {
            System.out.println(String.format("Duplicated: %s", node.getId()));
        }
    }

    @Override
    public void handle(Way way) {
        if(way.getId() == 178067341L){
            System.out.println("Urzednicza");
            way.getNodeIds().forEach(System.out::println);
        }
        else if(way.getId() == 440915828L){
            System.out.println("Czarnowiejska lewa");
            way.getNodeIds().forEach(System.out::println);
        }
        else if(way.getId() == 155221402L){
            System.out.println("Czarnowiejska prawa");
            way.getNodeIds().forEach(System.out::println);
        }
        else if(way.getId() == 251533289L){
            System.out.println("agh");
            way.getNodeIds().forEach(System.out::println);
        }


        this.ways.add(way);
    }

    @Override
    public void handle(Relation relation) {

    }

    public Map<Long, Node> getNodes() {
        return nodes;
    }

    public LinkedList<Way> getWays() {
        return ways;
    }
}
