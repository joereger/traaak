package com.fbdblog.qtype.util;

import com.fbdblog.dao.Question;
import com.fbdblog.dao.Questionconfig;
import com.fbdblog.util.UserInputSafe;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 25, 2007
 * Time: 12:46:35 PM
 */
public class QuestionconfigUtil {

    public static String getValue(Question question, String name){
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals(name)){
                return questionconfig.getValue();
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
