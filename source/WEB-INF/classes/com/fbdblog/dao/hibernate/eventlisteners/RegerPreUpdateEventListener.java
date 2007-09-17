package com.fbdblog.dao.hibernate.eventlisteners;

import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.event.PreUpdateEvent;
import com.fbdblog.dao.extendedpropscache.ExtendedPropsFactory;

/**
 * Before insert
 */
public class RegerPreUpdateEventListener implements PreUpdateEventListener {

    public boolean onPreUpdate(PreUpdateEvent event){
        ExtendedPropsFactory.flushExtended(event.getEntity());
        return false;
    }
}
