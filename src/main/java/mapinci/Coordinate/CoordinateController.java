package mapinci.Coordinate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import map.graph.graphElements.Node;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.Shape;
import map.graph.graphElements.segments.Nodes;
import mapinci.CoordinatingService;
import mapinci.GraphMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

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
        try {
            List<Node> nodes = service.startAlgo(shape);
            logger.info("Returning nodes: {}", nodes.size());
            return new Nodes(nodes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (OsmXmlParserException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
