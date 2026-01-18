package com.example.fjnuserviceapp.ui.nav.model;

/**
 * 兴趣点（POI）信息模型
 */
public class PoiInfo {
    private String id;
    private String name;
    private String address;
    private LatLngPoint coordinate;
    private String type;
    private String description;

    public PoiInfo() {
    }

    public PoiInfo(String id, String name, String address, LatLngPoint coordinate, String type, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.coordinate = coordinate;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLngPoint getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLngPoint coordinate) {
        this.coordinate = coordinate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PoiInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", coordinate=" + coordinate +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}