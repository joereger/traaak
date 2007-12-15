package com.fbdblog.calc;

import com.fbdblog.dao.*;
import com.fbdblog.dao.Calculation;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Num;
import com.fbdblog.qtype.def.ComponentTypes;
import com.fbdblog.qtype.def.Component;

import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * User: Joe Reger Jr
 * Date: Dec 5, 2007
 * Time: 6:30:58 PM
 */
public class CalcUtil {

    public static double getValueForCalc(Questioncalc questioncalc, User user, App app){
        double out = 0;
        CalctimeperiodFactory ctpFactory = new CalctimeperiodFactory(user, app);
        Calctimeperiod calctimeperiod=ctpFactory.getCalctimeperiodUnpopulated(questioncalc.getCalctimeperiodid());
        com.fbdblog.calc.Calculation calculation=CalculationFactory.getCalculationByType(questioncalc.getCalculationtype());
        if (calctimeperiod != null && calculation != null) {
            //See if something's already recorded for this key
            boolean foundValue=false;
            double value = 0;
            List<com.fbdblog.dao.Calculation> calcs=HibernateUtil.getSession().createCriteria(com.fbdblog.dao.Calculation.class)
                    .add(Restrictions.eq("calctimeperiodid", calctimeperiod.getId()))
                    .add(Restrictions.eq("calctimeperiodkey", calctimeperiod.getKey()))
                    .add(Restrictions.eq("calculationtype", calculation.getId()))
                    .add(Restrictions.eq("questionid", questioncalc.getQuestionid()))
                    .add(Restrictions.eq("userid", user.getUserid()))
                    .setCacheable(true)
                    .list();
            for (Iterator<Calculation> iterator2=calcs.iterator(); iterator2.hasNext();) {
                com.fbdblog.dao.Calculation calcTmp = iterator2.next();
                value = calcTmp.getValue();
                out = value;
            }
        }
        return out;
    }

    public static double getValueForQuestion(Question question, User user, App app){

        //Find the latest post with an answer for this question so that I can pass it to the component
        Post post = null;
        List<Postanswer> postanswers = HibernateUtil.getSession().createCriteria(Postanswer.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .addOrder(Order.desc("postanswerid"))
                                           .setCacheable(true)
                                           .setMaxResults(1)
                                           .list();
        for (Iterator<Postanswer> iterator=postanswers.iterator(); iterator.hasNext();) {
            Postanswer postanswer = iterator.next();
            post = Post.get(postanswer.getPostid());
        }

        if (post!=null && post.getPostid()>0){
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, post, user);
            String val = component.getValue();
            if (Num.isdouble(val)){
                return Double.parseDouble(val);
            }
        }

        return 0;
    }


}
