package com.fbdblog.calc;

import com.fbdblog.dao.*;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Num;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Iterator;
import java.util.Date;


/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 10:54:32 AM
 */
public class DoCalculationsAfterPost {

    public static void doCalculations(Post post){
        App app = App.get(post.getAppid());
        User user = User.get(post.getUserid());
        doCalculations(user, app);
    }

    public static void doCalculations(User user, App app){
        Logger logger = Logger.getLogger(DoCalculationsAfterPost.class);
        CalctimeperiodFactory calctimeperiodFactory = new CalctimeperiodFactory(user, app);

        for (Iterator<Question> iterator=app.getQuestions().iterator(); iterator.hasNext();) {
            Question question=iterator.next();

            List<Questioncalc> questioncalcs = HibernateUtil.getSession().createCriteria(Questioncalc.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .addOrder(Order.asc("questioncalcid"))
                                           .setCacheable(true)
                                           .list();
            for (Iterator<Questioncalc> iterator1=questioncalcs.iterator(); iterator1.hasNext();) {
                Questioncalc questioncalc=iterator1.next();
                Calctimeperiod calctimeperiod = calctimeperiodFactory.getCalctimeperiod(questioncalc.getCalctimeperiodid());
                Calculation calculation = CalculationFactory.getCalculationByType(questioncalc.getCalculationtype());
                if (calctimeperiod!=null && calculation!=null){
                    double value = calculation.calculate(user, question, calctimeperiod.getPosts());
                    com.fbdblog.dao.Calculation calcDao = new com.fbdblog.dao.Calculation();
                    calcDao.setRecordeddate(new Date());
                    calcDao.setCalctimeperiodid(calctimeperiod.getId());
                    calcDao.setCalctimeperiodkey(calctimeperiod.getKey());
                    calcDao.setCalculationtype(calculation.getId());
                    calcDao.setQuestionid(question.getQuestionid());
                    calcDao.setUserid(user.getUserid());
                    calcDao.setValue(value);
                    try{calcDao.save();}catch (Exception ex){logger.error("", ex);}
                }
            }                                                                 
        }
    }


}
