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
    private int dateDayPost;
    private int dateMonthPost;
    private int dateYearPost;
    private int timeHourPost;
    private int timeMinutePost;
    private String typePost;
    private String datePost;


    public PostData() {}

    public void setDatePost(int day,int month, int year){
        this.dateDayPost=day;
        this.dateMonthPost=month;
        this.dateYearPost=year;
    }

    public void setDateLost(int day,int month, int year){
        this.dateDayLost=day;
        this.dateMonthLost=month;
        this.dateYearLost=year;
    }

    public void setTimePost(int hour, int minute){
        this.timeHourPost = hour;
        this.timeMinutePost = minute;
    }

    public void setTimeLost(int hour, int minute){
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

    public int getDateDayPost() {
        return dateDayPost;
    }

    public int getDateMonthPost() {
        return dateMonthPost;
    }

    public int getDateYearPost() {
        return dateYearPost;
    }

    public int getTimeHourPost() {
        return timeHourPost;
    }

    public int getTimeMinutePost() {
        return timeMinutePost;
    }
}
