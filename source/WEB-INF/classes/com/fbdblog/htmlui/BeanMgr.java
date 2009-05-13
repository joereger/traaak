package com.fbdblog.htmlui;

import java.util.TreeMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:48:34 AM
 */
public class BeanMgr {

    private TreeMap<String, Object> beans = new TreeMap<String, Object>();

    public BeanMgr(){
    }

    public Object get(String beanname){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (beans!=null && beans.containsKey(beanname)){
            //logger.debug("returning previously initialized bean for beanname="+ beanname);
            return beans.get(beanname);
        } else {
            try{
                //Need to instantiate the bean
                Class clazz = Class.forName("com.fbdblog.htmluibeans."+ beanname);
                Constructor constructor = clazz.getDeclaredConstructor(new Class[]{});
                constructor.setAccessible(true);
                Object bean = constructor.newInstance(new Object[]{});
                Method meth = clazz.getMethod("initBean", null);
                Object retobj = meth.invoke(bean, null);
                //Add bean to TreeMap
                beans.put(beanname, bean);
                //Return bean to caller
                return bean;
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        return null;
    }

    public Object getProp(String beandotprop){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beandotprop="+beandotprop);
        if (beandotprop!=null && beandotprop.indexOf(".")>-1){
            String[] split = beandotprop.split("\\.");
            if (split!=null && split.length>=2){
                String beanname = split[0];
                String propname = split[1];
                Object bean = get(beanname);
                if (bean!=null){
                    logger.debug("got a bean");
                    //Find the property
                    try{
                        return PropertyUtils.getSimpleProperty(bean, propname);
                    } catch (Exception ex){
                        logger.error("", ex);
                    }
                } else {
                    logger.debug("bean iz null");
                }
            } else {
                if (split==null){
                    logger.debug("split is null");
                } else {
                    logger.debug("split.length="+split.length);
                }
            }
        } else {
            logger.debug("no . found in beandotprop");
        }
        return null;
    }



}
