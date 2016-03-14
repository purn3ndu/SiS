package com.tcs.learning.sis;

/**
 * Created by 883633 on 26-10-2015.
 */
public class TextLinks {

    private String title;
    private String urlreceived;
    private String time;

    public TextLinks(){

    }

    public TextLinks(String title, String urlreceived, String time){
        this.title=title;
        this.urlreceived=urlreceived;
        this.time=time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public String getUrlreceived() {
        return urlreceived;
    }

    public void setUrlreceived(String urlreceived) {
        this.urlreceived = urlreceived;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return "Title : "+title+
                "URL : "+urlreceived+
                "Time : "+time;
    }



}
