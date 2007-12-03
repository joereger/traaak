package com.fbdblog.calc;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.Postanswer;
import com.fbdblog.chart.DataTypeInteger;
import com.fbdblog.chart.DataTypeDecimal;
import com.fbdblog.util.Num;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 10:55:31 AM
 */
public class CalculationDeltaPercent implements Calculation {

    public static int ID = 4;

    public int getId() {
        return ID;
    }

    public String getName() {
        return "Increase/Decrease Percent";
    }

    public double calculate(User user, Question question, List<Post> posts) {
        double first = 0;
        boolean havefirst = false;
        double last = 0;
        if (question.getDatatypeid()== DataTypeInteger.DATATYPEID || question.getDatatypeid()== DataTypeDecimal.DATATYPEID){
            for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
                Post post=iterator.next();
                for (Iterator<Postanswer> iterator1=post.getPostanswers().iterator(); iterator1.hasNext();) {
                    Postanswer postanswer=iterator1.next();
                    if (postanswer.getQuestionid()==question.getQuestionid()){
                        if (Num.isdouble(postanswer.getValue())){
                            if (!havefirst){
                                first = Double.parseDouble(postanswer.getValue());
                                havefirst = true;
                            }
                            last = Double.parseDouble(postanswer.getValue());
                        }
                    }
                }
            }
        }
        double abschange =  last - first;
        double out = 0;
        if (first>0){
            out = (abschange/first) * 100;
        }
        return out;
    }
}
