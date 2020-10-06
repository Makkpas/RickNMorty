package com.example.rickmorty.models;

public class Location {

    private int id;
    private String name;
    private String type;
    private String dimension;
    private  String url;

    public Location() {
    }

    public Location(int id, String name, String type, String dimension, String url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.dimension = dimension;
        this.url = url;
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

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", dimension='" + dimension + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
