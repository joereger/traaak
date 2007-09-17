package com.fbdblog.dao.hibernate.eventlisteners;

import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreInsertEvent;
import com.fbdblog.dao.extendedpropscache.ExtendedPropsFactory;

/**
 * Before insert
 */
public class RegerPreInsertEventListener implements PreInsertEventListener {

    public boolean onPreInsert(PreInsertEvent event){
        ExtendedPropsFactory.flushExtended(event.getEntity());
        return false;
    }

}
