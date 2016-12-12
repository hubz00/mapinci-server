package communication;

import computation.graphElements.Node;
import computation.graphElements.Shape;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoordinatingService {
    public List<Node> startAlgo(Shape shape) {
        GraphMaker graphMaker = new GraphMaker();
        return graphMaker.runApp(shape);
    }
}
