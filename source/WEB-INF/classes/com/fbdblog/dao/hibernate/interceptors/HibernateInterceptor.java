package com.fbdblog.dao.hibernate.interceptors;

import org.hibernate.EmptyInterceptor;

import com.fbdblog.dao.hibernate.RegerEntity;


/**
 * Intercepts for validation
 */
public class HibernateInterceptor extends EmptyInterceptor {
//    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types){
//        try{
//            logger.debug("Interceptor called for: " + entity.getClass().getName());
//            ValidateWorker.validate(entity);
//        } catch (HibValEx hex){
//            throw hex;
//        }
//        return false;
//    }

//      public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types){
//            try{
//                ((RegerEntity)entity).load();
//            } catch (Exception ex){
//                logger.error("HibernateInterceptor.java", ex);
//            }
//            return false;
//      }
}
