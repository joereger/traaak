package com.fbdblog.chart;

import reger.cache.providers.jboss.Cacheable;

/**
 * Holds field location information
 */
@Cacheable
public class FieldOrder {

    private int questionid = 0;
    private int x = 0;
    private int y = 0;
    private int w = 400;
    private int h = 150;

    public FieldOrder(int questionid, int x, int y, int w, int h){
        this.questionid = questionid;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getMegafieldid() {
        return questionid;
    }

    public void setMegafieldid(int questionid) {
        this.questionid = questionid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

}
