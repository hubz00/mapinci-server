import computation.graphElements.LatLon;
import computation.graphElements.NodeFactory;
import computation.utils.PositionApproximator;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class PositionApproximatorTest {

    @Test
    public void offsetTest(){
        PositionApproximator approximator = new PositionApproximator();
        NodeFactory factory = new NodeFactory();

        LatLon latLon = approximator.offset(factory.newNode(0.0,50.0), 100.0,100.0);
        assertTrue(latLon.getLat() == 50.00089831528412);
        assertTrue(latLon.getLon() == 0.0013975304915376204);
    }
}
