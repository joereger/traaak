package com.fbdblog.calc;

import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;

import java.util.List;
import java.util.HashMap;

/**
 * Caches lists of posts for request scope
 */
public class CalctimeperiodFactory {

    private User user;
    private App app;
    private HashMap<Integer, Calctimeperiod> calctimeperiods = new HashMap<Integer, Calctimeperiod>();

    public CalctimeperiodFactory(User user, App app){
        this.user = user;
        this.app = app;
    }

    public Calctimeperiod getCalctimeperiod(int calctimeperiodid){
        if (!calctimeperiods.containsKey(calctimeperiodid)){
            calctimeperiods.put(calctimeperiodid, getCalctimeperiodById(calctimeperiodid));
        }
        return calctimeperiods.get(calctimeperiodid);
    }

    private Calctimeperiod getCalctimeperiodById(int calctimeperiodid){
        if (calctimeperiodid==CalctimeperiodAlltime.ID){
            return new CalctimeperiodAlltime(user, app);
        }
        if (calctimeperiodid==CalctimeperiodMonth.ID){
            return new CalctimeperiodMonth(user, app);
        }
        if (calctimeperiodid==CalctimeperiodYear.ID){
            return new CalctimeperiodYear(user, app);
        }
        if (calctimeperiodid==CalctimeperiodDay.ID){
            return new CalctimeperiodDay(user, app);
        }
        if (calctimeperiodid==CalctimeperiodWeek.ID){
            return new CalctimeperiodWeek(user, app);
        }
        return null;
    }

    public static Calctimeperiod getCalctimeperiodByIdStatic(int calctimeperiodid){
        if (calctimeperiodid==CalctimeperiodAlltime.ID){
            return new CalctimeperiodAlltime();
        }
        if (calctimeperiodid==CalctimeperiodMonth.ID){
            return new CalctimeperiodMonth();
        }
        if (calctimeperiodid==CalctimeperiodYear.ID){
            return new CalctimeperiodYear();
        }
        if (calctimeperiodid==CalctimeperiodDay.ID){
            return new CalctimeperiodDay();
        }
        if (calctimeperiodid==CalctimeperiodWeek.ID){
            return new CalctimeperiodWeek();
        }
        return null;
    }


}
