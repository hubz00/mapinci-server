import map.graph.DataSculptor;
import map.graph.algorithm.ShapeFinderManager;
import map.graph.algorithm.conditions.*;
import map.graph.graphElements.*;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import org.junit.Test;

import java.util.*;
import java.util.logging.Logger;

public class ShapeFinderTest {

    private Graph graph;
    private Logger log;

    private void setup() {
        log = Logger.getLogger("ShapeFinderTest");

        graph = new Graph();

        NodeFactory nodeFactory = new NodeFactory();
        SegmentFactory segmentFactory = new SegmentFactory();

        Node n0 = nodeFactory.newNode(0.0,0.0);
        Node n1 = nodeFactory.newNode(2.0,1.0);
        Node n2 = nodeFactory.newNode(4.0,1.0);
        Node n3 = nodeFactory.newNode(5.0,3.0);
        Node n4 = nodeFactory.newNode(3.0,4.0);
        Node n5 = nodeFactory.newNode(2.0,3.0);
        Node n7 = nodeFactory.newNode(3.0,1.0);
        Node n8 = nodeFactory.newNode(4.0001,1.0001);
        Node n9 = nodeFactory.newNode(3.999,1.0002);


        List<Segment> segments = new LinkedList<Segment>();
        segments.add(segmentFactory.newSegment(n1,n7));
        segments.add(segmentFactory.newSegment(n7,n2));
        segments.add(segmentFactory.newSegment(n2,n3));
        segments.add(segmentFactory.newSegment(n3,n4));
        segments.add(segmentFactory.newSegment(n4,n5));

        segments.add(segmentFactory.newSegment(n1,n4));
        segments.add(segmentFactory.newSegment(n4,n2));
        segments.add(segmentFactory.newSegment(n2,n7));
        segments.add(segmentFactory.newSegment(n7,n1));

        segments.add(segmentFactory.newSegment(n2,n8));
        segments.add(segmentFactory.newSegment(n8,n9));
        segments.add(segmentFactory.newSegment(n9,n5));
        segments.add(segmentFactory.newSegment(n5,n1));

        segments.add(segmentFactory.newSegment(n1,n3));

        graph.setSegments(segments);

        graph.getSegments().values().forEach(segment -> System.out.println(String.format("%s \t\t\tLength: %s", segment,segment.getLength())));
    }

    /* creates shape
        5 -- 2
        2 -- 3
        3 -- 1
        1 -- 5
    */

    @Test
    public void findPathWithInfiniteSlope(){

        setup();

        Node startNode = graph.getNodeByCoordinates(2.0,3.0);
        assert startNode != null;
        log.info(String.format("[Start node] [id: %d] [long: %f] [lat: %f]", startNode.getId(), startNode.getLongitude(), startNode.getLatitude()));

        List<Integer> shapeNodes = new LinkedList<>();
        shapeNodes.add(0,5);
        shapeNodes.add(1,2);
        shapeNodes.add(2,2);
        shapeNodes.add(3,3);
        shapeNodes.add(4,3);
        shapeNodes.add(5,1);
        shapeNodes.add(6,1);
        shapeNodes.add(7,5);
        List<Segment> shape = createShapeSegments(0.0, shapeNodes, new LinkedList<>());

        ConditionManager cm = new ConditionManager();
        ConditionFactory conditionFactory = new ConditionFactory();
        cm.addCondition(conditionFactory.newCondition(0.0));

        ShapeFinderManager manager = new ShapeFinderManager(graph);
        manager.findShape(shape,startNode,cm, 0.05).forEach(System.out::println);
    }

    /* creates shape
        4 -- 2
        2 -- 1
        1 -- 3
        3 -- 4
    */
    @Test
    public void findPerfectlyFittedRoute() {

        setup();

        Node startNode = graph.getNodeByCoordinates(2.0,1.0);
        assert startNode != null;
        log.info(String.format("Start node [id: %d] [long: %f] [lat: %f]", startNode.getId(), startNode.getLongitude(), startNode.getLatitude()));


        List<Integer> shapeNodes = new LinkedList<>();
        shapeNodes.add(0,4);
        shapeNodes.add(1,2);
        shapeNodes.add(2,2);
        shapeNodes.add(3,1);
        shapeNodes.add(4,1);
        shapeNodes.add(5,3);
        shapeNodes.add(6,3);
        shapeNodes.add(7,4);
        List<Segment> shape = createShapeSegments(0.0, shapeNodes, new LinkedList<>());

        ConditionManager cm = new ConditionManager();
        ConditionFactory conditionFactory = new ConditionFactory();
        cm.addCondition(conditionFactory.newCondition(0.0));

        ShapeFinderManager manager = new ShapeFinderManager(graph);
        manager.findShape(shape,startNode,cm, 0.05).forEach(System.out::println);
    }


    /**
     * Searches for shape:
     *
     * 4 --- 3
     * 3 --- 6
     * 6 --- 4
     *
     */

    @Test
    public void findShapeInDifferentAngleWithPadding(){
        setup();

        Node startNode = graph.getNodeByCoordinates(2.0,1.0);
        assert startNode != null;
        log.info(String.format("Start node [id: %d] [long: %f] [lat: %f]", startNode.getId(), startNode.getLongitude(), startNode.getLatitude()));


        List<Integer> shapeNodes = new LinkedList<>();
        shapeNodes.add(0,6);
        shapeNodes.add(1,5);
        shapeNodes.add(2,5);
        shapeNodes.add(3,2);
        shapeNodes.add(4,2);
        shapeNodes.add(5,6);

        List<Double> percentLengthList = new LinkedList<>();
        percentLengthList.add(0,0.29325513196480938416422287390029);
        percentLengthList.add(1,0.41348973607038123167155425219941);
        percentLengthList.add(2,0.29325513196480938416422287390029);

        List<Segment> shape = createShapeSegments(0.0, shapeNodes, percentLengthList);

        ConditionManager cm = new ConditionManager();
        ConditionFactory conditionFactory = new ConditionFactory();
        cm.addPrimaryCondition(conditionFactory.newPrimaryCondition(150.0, Math.PI));
        cm.addCondition(conditionFactory.newCondition(0.1));
        cm.addCondition(conditionFactory.newCondition(0.1, 750000.0));

        ShapeFinderManager manager = new ShapeFinderManager(graph);
        manager.findShape(shape,startNode,cm, 0.05).forEach(System.out::println);
    }


    private List<Segment> createShapeSegments(double noiseRange, List<Integer> shapeNodes, List<Double> percentLength){
        List<Segment> result = new LinkedList<>();
        List<Node> nodes = new ArrayList<>(7);
        NodeFactory nf = new NodeFactory();
        SegmentFactory sf = new SegmentFactory();
        Random random = new Random();

        nodes.add(0,nf.newNode(0.0,0.0));
        nodes.add(1,nf.newNode(2.0 + random.nextDouble()*noiseRange,1.0 + random.nextDouble()*noiseRange));
        nodes.add(2,nf.newNode(4.0 + random.nextDouble()*noiseRange,1.0 + random.nextDouble()*noiseRange));
        nodes.add(3,nf.newNode(5.0 + random.nextDouble()*noiseRange,3.0 + random.nextDouble()*noiseRange));
        nodes.add(4,nf.newNode(3.0 + random.nextDouble()*noiseRange,4.0 + random.nextDouble()*noiseRange));
        nodes.add(5,nf.newNode(2.0 + random.nextDouble()*noiseRange,3.0 + random.nextDouble()*noiseRange));
        nodes.add(6,nf.newNode(4.0 + random.nextDouble()*noiseRange,3.0 + random.nextDouble()*noiseRange));

        Iterator<Integer> i = shapeNodes.iterator();
        Iterator<Double> iPercent = percentLength.iterator();

        int index = 0;
        if(!percentLength.isEmpty()) {
            while (i.hasNext()) {
                Double percent = iPercent.next();
                log.info(percent.toString());
                result.add(index, sf.newSegment(nodes.get(i.next()), nodes.get(i.next()), percent ));
                index++;
            }
        }
        else {
            while (i.hasNext()) {
                result.add(index, sf.newSegment(nodes.get(i.next()), nodes.get(i.next())));
                index++;
            }
        }

        result.forEach(seg -> log.info(seg.toString()));

        return result;
    }


}
