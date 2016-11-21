import map.graph.graphElements.Node;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.Vector;
import org.junit.Test;

public class VectorTest {

    @Test
    public void angleCounting(){
        NodeFactory nf = new NodeFactory();

        Node start = nf.newNode(0.0,0.0);
        Node end1 = nf.newNode(0.0, 10.0);
        Node end2 = nf.newNode(10.0, 0.0);

        Vector v1 = new Vector(start, end1);
        Vector v2 = new Vector(start, end2);

        System.out.println(v1.getAngleBetween(v2));

        assert (v2.getAngleBetween(v1) == Math.PI/2);
    }

}
