package edu.upc.dsa.models;

public class UsersScoreDTO {
    private String username;
    private int score;

    public UsersScoreDTO() {}

    public UsersScoreDTO(String username, int score) {
        this.username = username;
        this.score = score;
    }

    // getters y setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
