package mapinci.Coordinate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import map.graph.graphElements.Node;
import map.graph.graphElements.Shape;
import mapinci.CoordinatingService;
import mapinci.GraphMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

@RestController
@RequestMapping("/coordinate")
public class CoordinateController {

    @Autowired
    private CoordinatingService service;

    @RequestMapping(method = RequestMethod.GET)
    public ArrayList<Node> coordinate(@RequestParam(value="shapeId", defaultValue="1") int shapeId, @RequestParam(value="startingLat", defaultValue = "0") String startingLat,
                                      @RequestParam(value="startingLong", defaultValue = "0") String startingLong) throws IOException, OsmXmlParserException {

        GraphMaker gm = new GraphMaker();

        //TODO change from all nodes to important
        ArrayList<Node> result = gm.runApp();

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/json", produces = "application/json")
    public List<Node> postShape (@RequestBody Shape shape) {
        return service.startAlgo(shape);
    }
}
