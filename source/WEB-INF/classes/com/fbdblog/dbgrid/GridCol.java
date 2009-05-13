package com.fbdblog.dbgrid;

/**
 * User: Joe Reger Jr
 * Date: Nov 7, 2007
 * Time: 12:13:37 PM
 */
public class GridCol {

    private String header;
    private String headerStyleClass;
    private String headerStyle;
    private String content;
    private String contentStyleClass;
    private String contentStyle;
    private boolean isnowrap = false;

    //Format of content is <$propertyname$>
    // or
    // <$propertyname|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>

    public GridCol (String header, String content){
        this.header = header;
        this.content = content;
    }

    public GridCol (String header, String content, boolean isnowrap){
        this.header = header;
        this.content = content;
        this.isnowrap = isnowrap;
    }

    public GridCol (String header, String content, boolean isnowrap, String headerStyleClass, String contentStyleClass){
        this.header = header;
        this.headerStyleClass = headerStyleClass;
        this.content = content;
        this.contentStyleClass = contentStyleClass;
        this.isnowrap = isnowrap;
    }

    public GridCol (String header, String content, String headerStyleClass, String contentStyleClass){
        this.header = header;
        this.headerStyleClass = headerStyleClass;
        this.content = content;
        this.contentStyleClass = contentStyleClass;
    }

    public GridCol (String header, String content, boolean isnowrap, String headerStyleClass, String contentStyleClass, String headerStyle, String contentStyle){
        this.header = header;
        this.headerStyle = headerStyle;
        this.headerStyleClass = headerStyleClass;
        this.content = content;
        this.contentStyle = contentStyle;
        this.contentStyleClass = contentStyleClass;
        this.isnowrap = isnowrap;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header=header;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content=content;
    }

    public String getHeaderStyleClass() {
        return headerStyleClass;
    }

    public void setHeaderStyleClass(String headerStyleClass) {
        this.headerStyleClass=headerStyleClass;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle=headerStyle;
    }

    public String getContentStyleClass() {
        return contentStyleClass;
    }

    public void setContentStyleClass(String contentStyleClass) {
        this.contentStyleClass=contentStyleClass;
    }

    public String getContentStyle() {
        return contentStyle;
    }

    public void setContentStyle(String contentStyle) {
        this.contentStyle=contentStyle;
    }

    public boolean getIsnowrap() {
        return isnowrap;
    }

    public void setIsnowrap(boolean isnowrap) {
        this.isnowrap=isnowrap;
    }
}
