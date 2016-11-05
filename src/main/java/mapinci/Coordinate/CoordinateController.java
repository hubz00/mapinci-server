package mapinci.Coordinate;

import java.io.IOException;
import java.util.ArrayList;

import map.graph.graphElements.Node;
import mapinci.GraphMaker;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

@RestController
@RequestMapping("/coordinate")
public class CoordinateController {

    @RequestMapping(method = RequestMethod.GET)
    public ArrayList<Node> coordinate(@RequestParam(value="shapeId", defaultValue="1") int shapeId, @RequestParam(value="startingLat", defaultValue = "0") String startingLat,
                                      @RequestParam(value="startingLong", defaultValue = "0") String startingLong) throws IOException, OsmXmlParserException {

        GraphMaker gm = new GraphMaker();

        //TODO change from all nodes to important
        ArrayList<Node> result = gm.runApp();

        return result;
    }
}
