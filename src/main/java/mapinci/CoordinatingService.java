package mapinci;

import map.graph.graphElements.Node;
import map.graph.graphElements.Shape;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CoordinatingService {
    public List<Node> startAlgo(Shape shape) {
        GraphMaker graphMaker = new GraphMaker();
        return graphMaker.runApp(shape);
    }
}
