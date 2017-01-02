package communication;

import computation.graphElements.Node;
import computation.graphElements.Shape;
import computation.graphElements.segments.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class CoordinatingService {

    Logger logger = LoggerFactory.getLogger(CoordinatingService.class);

    public List<Node> startAlgo(Shape shape) {
        //logger.info("Starting algo for shape: \n {}", shape.toString());
        //logger.info("Shape segment vectors: \n {}", shape.getSegments().stream().map(Segment::getVectors).collect(Collectors.toList()));
        GraphMaker graphMaker = new GraphMaker();
        return graphMaker.runApp(shape);
    }
}
