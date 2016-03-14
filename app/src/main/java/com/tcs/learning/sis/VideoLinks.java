package com.tcs.learning.sis;

/**
 * Created by 883633 on 27-10-2015.
 */
public class VideoLinks {
    private String title;
    private String urlreceived;
    private String time;
    private String thumbnail;
    private String id;

    public VideoLinks(){

    }
    public VideoLinks(String id, String title, String urlreceived, String time,String thumbnail){
        this.title=title;
        this.urlreceived=urlreceived;
        this.time=time;
        this.thumbnail=thumbnail;
        this.id=id;
    }

    public void setId(String id) {
        this.title = id;
    }

    public String getId() {
        return id;
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
    public String getThumbnail(){
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString(){
        return "Video ID : "+id+
                "Title : "+title+
                "URL : "+urlreceived+
                "Time : "+time+
                "Thumbnail :"+thumbnail;
    }

}
