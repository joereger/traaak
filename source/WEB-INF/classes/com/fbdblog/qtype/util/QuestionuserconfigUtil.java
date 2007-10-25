package com.fbdblog.qtype.util;

import com.fbdblog.dao.Question;
import com.fbdblog.dao.Questionconfig;
import com.fbdblog.dao.Questionuserconfig;
import com.fbdblog.dao.hibernate.HibernateUtil;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Oct 25, 2007
 * Time: 12:46:35 PM
 */
public class QuestionuserconfigUtil {

    public static String getValue(int questionid, int userid, String name){
        List<Questionuserconfig> questionuserconfigs = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                                           .add(Restrictions.eq("questionid", questionid))
                                           .add(Restrictions.eq("userid", userid))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Questionuserconfig> iterator = questionuserconfigs.iterator(); iterator.hasNext();) {
            Questionuserconfig questionuserconfig = iterator.next();
            if (questionuserconfig.getName().equals(name)){
                return questionuserconfig.getValue();
            }
        }
        return "";
    }

//    public static void setValue(Question question, String name, String value){
//        Logger logger = Logger.getLogger(QuestionconfigUtil.class);
//        //Delete any existing values for this name
//        for (Iterator<Questionconfig> iterator=question.getQuestionconfigs().iterator(); iterator.hasNext();) {
//            Questionconfig questionconfig=iterator.next();
//            if (questionconfig.getName().equals(name)){
//                iterator.remove();
//            }
//        }
//        //Insert the new value
//        if (value!=null && !value.trim().equals("")) {
//            Questionconfig qc=new Questionconfig();
//            qc.setQuestionid(question.getQuestionid());
//            qc.setName(name);
//            qc.setValue(UserInputSafe.clean(value));
//            try {qc.save();} catch (Exception ex) {logger.error("", ex);}
//            //Also add it to the question, no idea if this works
//            question.getQuestionconfigs().add(qc);
//        }
//
//    }



}
