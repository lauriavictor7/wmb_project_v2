package br.lauriavictor.wmb.model;

public class Beer {

    private int id;
    private String name;
    private String type;
    private String note;
    private byte[] image;

    public Beer(int id, String name, String type, String note, byte[] image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.note = note;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
