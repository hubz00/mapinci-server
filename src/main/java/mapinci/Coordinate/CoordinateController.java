package mapinci.Coordinate;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordinateController {

    @RequestMapping("/coordinate")
    public Coordinate coordinate(@RequestParam(value="shapeId", defaultValue="1") int shapeId, @RequestParam(value="startingLat", defaultValue = "0") String startingLat,
                               @RequestParam(value="startingLong", defaultValue = "0") String startingLong) {
        return new Coordinate(shapeId, startingLong, startingLat);
    }
}
