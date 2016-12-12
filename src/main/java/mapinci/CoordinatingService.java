package mapinci;

import map.graph.graphElements.Node;
import map.graph.graphElements.Shape;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CoordinatingService {
    public List<Node> startAlgo(Shape shape) {

//        Node node = new Node(1L, 10.0, 10.0);
        List<Node> nodes = new LinkedList<>();
//        nodes.add(node);
        return nodes;
    }
}
