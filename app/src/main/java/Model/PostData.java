package Model;

import android.graphics.Bitmap;

import java.util.Date;

public class PostData {
    private String idUser;
    private String title;
    private String description;
    private String chronology;
    private String photo;
    private int dateDayLost;
    private int dateMonthLost;
    private int dateYearLost;
    private int timeHourLost;
    private int timeMinuteLost;
    private String typePost;
    private String datePost;

    public PostData() {}

    public void setDate(int day,int month, int year){
        this.dateDayLost=day;
        this.dateMonthLost=month;
        this.dateYearLost=year;
    }

    public void setTime(int hour, int minute){
        this.timeHourLost = hour;
        this.timeMinuteLost = minute;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getDateDayLost() {
        return dateDayLost;
    }

    public int getDateMonthLost() {
        return dateMonthLost;
    }

    public int getDateYearLost() {
        return dateYearLost;
    }

    public int getTimeHourLost() {
        return timeHourLost;
    }

    public int getTimeMinuteLost() {
        return timeMinuteLost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTypePost() {
        return typePost;
    }

    public void setTypePost(String typePost) {
        this.typePost = typePost;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
