package com.fbdblog.qtype.def;



import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.App;
import com.fbdblog.qtype.*;
import com.fbdblog.util.Num;
import com.fbdblog.chart.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 10, 2006
 * Time: 12:26:37 PM
 */
public class ComponentTypes {

    private LinkedHashMap typesaslinkedhashmap;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static Component getComponentByID(int ID, Question question, Post post, User user){
        if (ID== Textbox.ID){
            return new Textbox(user, post, question);
        }
        if (ID== Essay.ID){
            return new Essay(user, post, question);
        }
        if (ID== Dropdown.ID){
            return new Dropdown(user, post, question);
        }
        if (ID== DropdownComplex.ID){
            return new DropdownComplex(user, post, question);
        }
        if (ID== Checkboxes.ID){
            return new Checkboxes(user, post, question);
        }
        if (ID== Range.ID){
            return new Range(user, post, question);
        }
        if (ID== Matrix.ID){
            return new Matrix(user, post, question);
        }
        if (ID== Timeperiod.ID){
            return new Timeperiod(user, post, question);
        }
        return null;
    }

    public LinkedHashMap getTypesaslinkedhashmap(){
        if (typesaslinkedhashmap==null){
            typesaslinkedhashmap = new LinkedHashMap();
            typesaslinkedhashmap.put(Textbox.NAME, Textbox.ID);
            typesaslinkedhashmap.put(Essay.NAME, Essay.ID);
            typesaslinkedhashmap.put(Dropdown.NAME, Dropdown.ID);
            typesaslinkedhashmap.put(DropdownComplex.NAME, DropdownComplex.ID);
            typesaslinkedhashmap.put(Checkboxes.NAME, Checkboxes.ID);
            typesaslinkedhashmap.put(Range.NAME, Range.ID);
            typesaslinkedhashmap.put(Matrix.NAME, Matrix.ID);
            typesaslinkedhashmap.put(Timeperiod.NAME, Timeperiod.ID);
        }
        return typesaslinkedhashmap;
    }

    public void setTypesaslinkedhashmap(LinkedHashMap typesaslinkedhashmap){
        this.typesaslinkedhashmap = typesaslinkedhashmap;
    }

    public Component getByTagSyntax(String tag, App app, Post post, User user){
        //Get questionid from <$question_22334$>
        String qidStr = tag.substring(11, tag.length()-2);
        logger.debug("tag="+tag+" qidStr="+qidStr);
        if (Num.isinteger(qidStr)){
            Question question = Question.get(Integer.parseInt(qidStr));
            if (question.getAppid()==app.getAppid()){
                return getComponentByID(question.getComponenttype(), question, post, user);
            }
        }
        return null;
    }


}
