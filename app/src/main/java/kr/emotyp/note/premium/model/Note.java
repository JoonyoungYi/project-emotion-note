package kr.emotyp.note.premium.model;

public class Note {

    private int id;
    private String ts;

    private int font_code = 0; //default
    private String text = "";
    private int color = 0xFF000000;

    /**
     * Init
     */

    public Note() {

    }

    /**
     * Getter and Setter
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public int getFont_code() {
        return font_code;
    }

    public void setFont_code(int font_code) {
        this.font_code = font_code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
