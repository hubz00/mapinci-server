import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SortingTest {

    @Test
    public void test(){
        List<Double> list = new LinkedList<>();
        list.add(0.3);
        list.add(0.4);
        list.add(0.1);
        list.add(4.5);

        Collections.sort(list);

        list.forEach(System.out::println);
    }
}
