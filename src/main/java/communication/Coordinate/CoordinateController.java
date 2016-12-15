package communication.Coordinate;

import computation.graphElements.Node;
import computation.graphElements.Shape;
import computation.graphElements.segments.Nodes;
import communication.CoordinatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coordinate")
public class CoordinateController {

    private static Logger logger = LoggerFactory.getLogger(CoordinateController.class);


    private CoordinatingService service;

    @Autowired
    public CoordinateController(CoordinatingService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Nodes postShape (@RequestBody Shape shape) {
        logger.info("Request for some fresh nodes");
            List<Node> nodes = service.startAlgo(shape);
            logger.info("Returning nodes: {}", nodes.size());
            return new Nodes(nodes);

    }
    @RequestMapping(method = RequestMethod.GET)
    public String snoop() {
        return "Hello there ;>";
    }
}
