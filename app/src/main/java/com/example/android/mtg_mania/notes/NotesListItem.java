package com.example.android.mtg_mania.notes;

public class NotesListItem {

    private Integer id;
    private String title;
    private String noteText;
    private String date;
    private String time;

    public NotesListItem(Integer id, String title, String noteText, String date, String time) {
        this.id = id;
        this.title = title;
        this.noteText = noteText;
        this.date = date;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
