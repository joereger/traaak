package com.fbdblog.dao.hibernate.eventlisteners;

import org.hibernate.event.*;
import com.fbdblog.dao.extendedpropscache.ExtendedPropsFactory;

public class RegerPreDeleteEventListener implements PreDeleteEventListener {

    public boolean onPreDelete(PreDeleteEvent event){
        ExtendedPropsFactory.flushExtended(event.getEntity());
        return false;
    }



}
