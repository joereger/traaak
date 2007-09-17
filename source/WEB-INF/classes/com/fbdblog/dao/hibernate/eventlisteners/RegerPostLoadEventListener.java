package com.fbdblog.dao.hibernate.eventlisteners;

import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.apache.log4j.Logger;
import com.fbdblog.dao.hibernate.RegerEntity;

public class RegerPostLoadEventListener implements PostLoadEventListener {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void onPostLoad(PostLoadEvent event){
        try{
            RegerEntity entity = (RegerEntity)event.getEntity();
            entity.load();
        } catch (Exception ex){
            logger.error("Error PostLoadEventListener", ex);
        }
    }



}
