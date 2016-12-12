package mapinci.Coordinate;

import java.util.ArrayList;

import map.graph.graphElements.Node;
import map.graph.graphElements.NodeFactory;
import mapinci.GraphMaker;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coordinate")
public class CoordinateController {

    @RequestMapping(method = RequestMethod.GET)
    public ArrayList<Node> coordinate(@RequestParam(value="shapeId", defaultValue="1") int shapeId, @RequestParam(value="startingLat", defaultValue = "0") String startingLat,
                                      @RequestParam(value="startingLong", defaultValue = "0") String startingLong)  {

        GraphMaker gm = new GraphMaker();
        NodeFactory nodeFactory = new NodeFactory();

        //TODO change from all nodes to important
        ArrayList<Node> result = gm.runApp(nodeFactory.newNode(Double.parseDouble(startingLong), Double.parseDouble(startingLong)),0.05);

        return result;
    }
}
