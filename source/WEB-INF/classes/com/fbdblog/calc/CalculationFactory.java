package com.fbdblog.calc;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 11:36:15 AM
 */
public class CalculationFactory {

    public static Calculation getCalculationByType(int calculationtype){
        if (calculationtype==CalculationSum.ID){
            return new CalculationSum();
        }
        return null;
    }


}
