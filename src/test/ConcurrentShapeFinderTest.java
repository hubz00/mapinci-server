import computation.algorithm.ShapeFinderManager;
import computation.algorithm.conditions.ConditionFactory;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.NodeFactory;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import org.junit.Test;

import java.util.*;
import java.util.logging.Logger;

public class ConcurrentShapeFinderTest {

    private Graph graph;
    private Logger log;

    private void setup() {
        log = Logger.getLogger("ConcurrentShapeFinder");

        graph = new Graph();

        NodeFactory nodeFactory = new NodeFactory();
        SegmentFactory segmentFactory = new SegmentFactory();

        Node n0 = nodeFactory.newNode(0.0,0.0);
        Node n1 = nodeFactory.newNode(0.2,0.1);
        Node n2 = nodeFactory.newNode(0.4,0.1);
        Node n3 = nodeFactory.newNode(0.5,0.3);
        Node n4 = nodeFactory.newNode(0.3,0.4);
        Node n5 = nodeFactory.newNode(0.2,0.3);
        Node n6 = nodeFactory.newNode(0.3,0.1);
        Node n8 = nodeFactory.newNode(0.4001,0.1001);
        Node n9 = nodeFactory.newNode(0.399,0.1002);


        List<Segment> segments = new LinkedList<Segment>();
        segments.add(segmentFactory.newSegment(n1,n6));
        segments.add(segmentFactory.newSegment(n6,n2));
        segments.add(segmentFactory.newSegment(n2,n3));
        segments.add(segmentFactory.newSegment(n3,n4));
        segments.add(segmentFactory.newSegment(n4,n5));

        segments.add(segmentFactory.newSegment(n1,n4));
        segments.add(segmentFactory.newSegment(n4,n2));
        segments.add(segmentFactory.newSegment(n2,n6));
        segments.add(segmentFactory.newSegment(n6,n1));

        segments.add(segmentFactory.newSegment(n2,n8));
        segments.add(segmentFactory.newSegment(n8,n9));
        segments.add(segmentFactory.newSegment(n9,n5));
        segments.add(segmentFactory.newSegment(n5,n1));

        segments.add(segmentFactory.newSegment(n1,n3));

        graph.setSegments(segments);

        graph.getSegments().values().forEach(segment -> System.out.println(String.format("%s", segment)));
    }



    @Test
    public void test(){
        setup();

        Node startNode = graph.getNodeByCoordinates(0.2,0.1);
        assert startNode != null;
        log.info(String.format("Start node [id: %d] [long: %f] [lat: %f]", startNode.getId(), startNode.getLongitude(), startNode.getLatitude()));


        List<Integer> shapeNodes = new LinkedList<>();
        shapeNodes.add(0,1);
        shapeNodes.add(1,2);
        shapeNodes.add(2,2);
        shapeNodes.add(3,3);
        shapeNodes.add(4,3);
        shapeNodes.add(5,1);

        List<Double> percentLengthList = new LinkedList<>();
        percentLengthList.add(0,0.29325513196480938416422287390029);
        percentLengthList.add(1,0.41348973607038123167155425219941);
        percentLengthList.add(2,0.29325513196480938416422287390029);

        List<Segment> shape = createShapeSegments(0.0, shapeNodes, percentLengthList);

        ConditionManager cm = new ConditionManager();
        ConditionFactory conditionFactory = new ConditionFactory();
        cm.addPrimaryCondition(conditionFactory.newPrimaryCondition(150.0, Math.PI));
        cm.addCondition(conditionFactory.newCondition(0.1));
        cm.addCondition(conditionFactory.newCondition(0.05, 76120.0));

        ShapeFinderManager manager = new ShapeFinderManager(graph, 3);
        manager.findShapeConcurrent(shape,startNode,cm, 0.05).forEach(System.out::println);
    }


    private List<Segment> createShapeSegments(double noiseRange, List<Integer> shapeNodes, List<Double> percentLength){
        List<Segment> result = new LinkedList<>();
        List<Node> nodes = new ArrayList<>(7);
        NodeFactory nf = new NodeFactory();
        SegmentFactory sf = new SegmentFactory();
        Random random = new Random();

        nodes.add(0,nf.newNode(0.0,0.0));
        nodes.add(1,nf.newNode(0.2 + random.nextDouble()*noiseRange,0.1 + random.nextDouble()*noiseRange));
        nodes.add(2,nf.newNode(0.4 + random.nextDouble()*noiseRange,0.1 + random.nextDouble()*noiseRange));
        nodes.add(3,nf.newNode(0.5 + random.nextDouble()*noiseRange,0.3 + random.nextDouble()*noiseRange));
        nodes.add(4,nf.newNode(0.3 + random.nextDouble()*noiseRange,0.4 + random.nextDouble()*noiseRange));
        nodes.add(5,nf.newNode(0.2 + random.nextDouble()*noiseRange,0.3 + random.nextDouble()*noiseRange));
        nodes.add(6,nf.newNode(0.4 + random.nextDouble()*noiseRange,0.3 + random.nextDouble()*noiseRange));

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
