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

    private static void createCalcOfTypeOnAllTimePeriods(Question question, int calctypeid, String typename){
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodAlltime.ID, typename, "All Time");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodYear.ID, typename, "Yearly");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodMonth.ID, typename, "Monthly");
        createCalcOfTypeAndOfTimePeriod(question, calctypeid, CalctimeperiodWeek.ID, typename, "Weekly");
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
