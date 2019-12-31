package Model;

import android.graphics.Bitmap;

import java.util.Date;

public class PostData {
    private String idUser;
    private String title;
    private String description;
    private String chronology;
    private Bitmap photo;
    private String dateLost;
    private String typePost;
    private String datePost;

    public PostData(String idUser, String title, String chronology,String description, Bitmap photo, String dateLost, String typePost, String datePost) {
        this.idUser = idUser;
        this.description=description;
        this.title = title;
        this.chronology = chronology;
        this.photo = photo;
        this.dateLost = dateLost;
        this.typePost = typePost;
        this.datePost = datePost;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setDateLost(String dateLost) {
        this.dateLost = dateLost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTypePost(String typePost) {
        this.typePost = typePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
