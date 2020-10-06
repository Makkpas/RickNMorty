package com.example.rickmorty.models;

public class Episode {
    private int id;
    private String name;
    private String air_date;
    private String episode;

    public Episode() {
    }

    public Episode(int id, String name, String air_date, String episode) {
        this.id = id;
        this.name = name;
        this.air_date = air_date;
        this.episode = episode;
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

    public String getDate() {
        return air_date;
    }

    public void setDate(String date) {
        this.air_date = date;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", air_date='" + air_date + '\'' +
                ", episode='" + episode + '\'' +
                '}';
    }
}
