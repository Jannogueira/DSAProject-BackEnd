package edu.upc.dsa.models;

public class InsigniaDTO {
    private String name;
    private String avatar;

    public InsigniaDTO(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public InsigniaDTO() {
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
