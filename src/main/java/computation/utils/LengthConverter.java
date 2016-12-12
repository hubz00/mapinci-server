package computation.utils;

public class LengthConverter {

    private final Double approxOneDegreeForCracow;

    public LengthConverter(){
        approxOneDegreeForCracow = 104585.0;
    }

    /**not accurate, only for reference
     *
     * @param meters
     * @return
     */
    public Double metersToCoordinatesDifference(Double meters) {
        return meters/approxOneDegreeForCracow;
    }
}
