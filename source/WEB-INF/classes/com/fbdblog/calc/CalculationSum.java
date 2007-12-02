package com.fbdblog.calc;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.Postanswer;
import com.fbdblog.chart.DataType;
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
public class CalculationSum implements Calculation {

    public static int ID = 1;

    public int getId() {
        return ID;
    }

    public String getName() {
        return "Sum";
    }

    public double calculate(User user, Question question, List<Post> posts) {
        double out = 0;
        if (question.getDatatypeid()==DataTypeInteger.DATATYPEID || question.getDatatypeid()==DataTypeDecimal.DATATYPEID){
            for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
                Post post=iterator.next();
                for (Iterator<Postanswer> iterator1=post.getPostanswers().iterator(); iterator1.hasNext();) {
                    Postanswer postanswer=iterator1.next();
                    if (postanswer.getQuestionid()==question.getQuestionid()){
                        if (Num.isdouble(postanswer.getValue())){
                            out = out + Double.parseDouble(postanswer.getValue());
                        }
                    }
                }
            }
        }
        return out;
    }
}
