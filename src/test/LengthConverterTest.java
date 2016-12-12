import computation.utils.LengthConverter;
import org.junit.Test;

public class LengthConverterTest {

    @Test
    public void meterToCoordinatesTest(){

        LengthConverter converter = new LengthConverter();

        Double result = converter.metersToCoordinatesDifference(1500.0);

        System.out.println(result);

    }
}
