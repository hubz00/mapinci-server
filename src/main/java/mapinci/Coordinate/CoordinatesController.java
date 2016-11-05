package mapinci.Coordinate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import map.graph.App;
import map.graph.graphElements.Segment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.kodapan.osm.parser.xml.OsmXmlParserException;
import se.kodapan.osm.util.Coordinate;

@RestController
public class CoordinatesController {

    @RequestMapping("/coordinate")
    public ArrayList<Coordinate> coordinate(@RequestParam(value="shapeId", defaultValue="1") int shapeId, @RequestParam(value="startingLat", defaultValue = "0") String startingLat,
                                            @RequestParam(value="startingLong", defaultValue = "0") String startingLong) throws IOException, OsmXmlParserException {

        Coordinates c = new Coordinates(App.runApp());
        return c.getCoordinates();
    }
}
