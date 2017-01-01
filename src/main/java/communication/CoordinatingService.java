package communication;

import computation.graphElements.Node;
import computation.graphElements.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoordinatingService {

    Logger logger = LoggerFactory.getLogger(CoordinatingService.class);

    public List<Node> startAlgo(Shape shape) {
        logger.info("Starting algo for shape: \n {}", shape.toString());
        GraphMaker graphMaker = new GraphMaker();
        return graphMaker.runApp(shape);
    }
}
