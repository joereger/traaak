package com.fbdblog.dao.hibernate;

import com.fbdblog.util.GeneralException;

/**
 * Simple validator interface
 */
public interface RegerEntity {

    public void load();
    public void validateRegerEntity() throws GeneralException;

}
