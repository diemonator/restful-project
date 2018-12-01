package service.model;

public class Product {
    private int studentNr;
    private int roomNr;
    private String location;

    public Product(int studentNr, int roomNr, String location) {
        this.studentNr = studentNr;
        this.roomNr = roomNr;
        this.location = location;
    }

    public Product() {}

    public void setTenant(int studentNr) {
        this.studentNr = studentNr;
    }

    public int getTenant() {
        return studentNr;
    }

    public void setRoomNumber(int roomNr) {
        this.roomNr = roomNr;
    }

    public int getRoomNumber() {
        return roomNr;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
