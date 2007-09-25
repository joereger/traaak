package com.fbdblog.util;

/**
 * User: Joe Reger Jr
 * Date: Sep 25, 2007
 * Time: 5:42:17 PM
 */
/**
 * A utility class good at converting from a large number of seconds to hh:mm:ss
 * and vice versa.
 */
public class HoursMinutesSeconds {

    private static final int SECONDSINHOUR = 3600;
    private static final int SECONDSINMINUTE = 60;

    private int hours;
    private int minutes;
    private int seconds;
    private int baseSeconds;

    private boolean calculationIsDone = false;

    /**
     * Constructor: pass number of seconds
     */
    public HoursMinutesSeconds(int baseSeconds){
        //Set baseSeconds
        this.baseSeconds = baseSeconds;

        //Setup a working variable
        int bSec = baseSeconds;

        //Go get number of hours
        this.hours = getHours(bSec);

        //Subtract that number of hours from the working bSec
        bSec = bSec - (this.hours * SECONDSINHOUR);

        //Go get number of minutes left
        this.minutes = getMinutes(bSec);

        //Subtract that number of minutes from the working bSec
        bSec = bSec - (this.minutes * SECONDSINMINUTE);

        //So the number of seconds is the remaining bSec
        this.seconds = bSec;

        //Conclude the calculation
        calculationIsDone=true;
    }

    /**
     * Constructor: pass hour, min, sec
     */
    public HoursMinutesSeconds(int hour, int min, int sec){
        //Set the vars correctly
        this.hours = hour;
        this.minutes = min;
        this.seconds = sec;
        //Calculate baseSeconds
        baseSeconds = (SECONDSINHOUR * hours) + (SECONDSINMINUTE * minutes) + (seconds);
        //Conclude the calculation
        calculationIsDone=true;
    }

    /**
     * returns the number of hours in the baseseconds number of seconds
     */
    private int getHours(int baseSeconds){
        int counter=0;
        while (baseSeconds>=SECONDSINHOUR) {
            baseSeconds=baseSeconds-SECONDSINHOUR;
            counter=counter+1;
        }
        return counter;
    }

    /**
     * returns the number of minutes in the baseseconds number of seconds
     */
    private int getMinutes(int baseSeconds){
        int counter=0;
        while (baseSeconds>=SECONDSINMINUTE) {
            baseSeconds=baseSeconds-SECONDSINMINUTE;
            counter=counter+1;
        }
        return counter;
    }


    //Some getters and setters.  Make sure calculation is done before getting value.

    public int getHours(){
        if (calculationIsDone){
            return this.hours;
        } else {
            return -1;
        }
    }

    public int getMinutes(){
        if (calculationIsDone){
            return this.minutes;
        } else {
            return -1;
        }
    }

    public int getSeconds(){
        if (calculationIsDone){
            return this.seconds;
        } else {
            return -1;
        }
    }

    public int getbaseSeconds(){
        if (calculationIsDone){
            return this.baseSeconds;
        } else {
            return -1;
        }
    }


}

