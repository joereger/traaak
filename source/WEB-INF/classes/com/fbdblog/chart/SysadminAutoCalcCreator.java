package com.fbdblog.chart;

import com.fbdblog.dao.Question;
import com.fbdblog.dao.Calculation;
import com.fbdblog.dao.Questioncalc;
import com.fbdblog.calc.*;
import org.apache.log4j.Logger;


/**
 * User: Joe Reger Jr
 * Date: Dec 11, 2007
 * Time: 1:59:19 AM
 */
public class SysadminAutoCalcCreator {

    public static void createAllPossibleCalcs(int questionid){
        Question question = Question.get(questionid);
        createAllPossibleCalcs(question);    
    }

    public static void createAllPossibleCalcs(Question question){
        createCalcOfTypeOnAllTimePeriods(question, CalculationSum.ID, "Total");
        createCalcOfTypeOnAllTimePeriods(question, CalculationAvg.ID, "Average");
        createCalcOfTypeOnAllTimePeriods(question, CalculationDeltaAbs.ID, "Change in");
        createCalcOfTypeOnAllTimePeriods(question, CalculationDeltaPercent.ID, "Percent Change in");
    }

    public static void createAllTotalCalcs(Question question){
        createCalcOfTypeOnAllTimePeriods(question, CalculationSum.ID, "Total");
    }

    public static void createAllAverageCalcs(Question question){
        createCalcOfTypeOnAllTimePeriods(question, CalculationAvg.ID, "Average");
    }

    public static void createAllDeltaAbsCalcs(Question question){
        createCalcOfTypeOnAllTimePeriods(question, CalculationDeltaAbs.ID, "Change in");
    }

    public static void createAllDeltaPctCalcs(Question question){
        createCalcOfTypeOnAllTimePeriods(question, CalculationDeltaPercent.ID, "Percent Change in");
    }

    private static void createCalcOfTypeOnAllTimePeriods(Question question, int calctypeid, String typename){
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodAlltime.ID, typename, "All Time");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodYear.ID, typename, "Yearly");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodMonth.ID, typename, "Monthly");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodWeek.ID, typename, "Weekly");
    }

    public static void createCommonCalcs(Question question){
        createCalcOfTypeOnCommonPeriods(question, CalculationSum.ID, "Total");
        createCalcOfTypeOnCommonPeriods(question, CalculationAvg.ID, "Average");
        createCalcOfTypeOnCommonPeriods(question, CalculationDeltaPercent.ID, "Percent Change in");
    }

    private static void createCalcOfTypeOnCommonPeriods(Question question, int calctypeid, String typename){
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodAlltime.ID, typename, "All Time");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodMonth.ID, typename, "Monthly");
    }

    private static void createCalcOfTypeAndOfTimePeriod(Question question, int calctypeid, int timeperiodid, String typename, String timeperiodname){
        Logger logger = Logger.getLogger(SysadminAutoCalcCreator.class);

        String name = "";
        if (calctypeid==CalculationSum.ID || calctypeid==CalculationAvg.ID){
            name = typename+" "+timeperiodname+" "+question.getQuestion();
        } else if (calctypeid==CalculationDeltaAbs.ID || calctypeid==CalculationDeltaPercent.ID){
            name = timeperiodname+" "+typename+" "+question.getQuestion();
        } else {
            logger.error("passed an unknown calttypeid="+calctypeid);
            return;
        }

        Questioncalc qc = new Questioncalc();
        qc.setCalctimeperiodid(timeperiodid);
        qc.setCalculationtype(calctypeid);
        qc.setName(name);
        qc.setQuestionid(question.getQuestionid());
        try{qc.save();}catch(Exception ex){logger.error("", ex);}
    }





}
