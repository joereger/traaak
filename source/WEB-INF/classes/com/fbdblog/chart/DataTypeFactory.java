package com.fbdblog.chart;

/**
 * Returns a DataType
 */
public class DataTypeFactory {

    public static DataType get(int DATATYPEID){

        if (DATATYPEID==DataTypeDatetime.DATATYPEID){
            return new DataTypeDatetime();
        } else if (DATATYPEID==DataTypeString.DATATYPEID){
            return new DataTypeString();
        } else if (DATATYPEID==DataTypeDecimal.DATATYPEID){
            return new DataTypeDecimal();
        } else if (DATATYPEID==DataTypeInteger.DATATYPEID){
            return new DataTypeInteger();
        }
        return null;
    }


}
