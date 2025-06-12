package edu.upc.dsa.models;

public class Media {
    String video;

    public Media(String url_video) {
        this.video = url_video;
    }
    public Media() {}
    public String getVideo() {
        return video;
    }
    public void setVideo(String video) {
        this.video = video;
    }
}
