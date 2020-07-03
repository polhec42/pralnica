package database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Order {

    private String cypher;
    private String volunteer;

    private int numberOfClothes;
    private LocalDate date;
    private int done;
    private int id;
    private int archived;

    public Order(String cypher, String volunteer, int numberOfClothes, LocalDate date, int done) {
        this.cypher = cypher;
        this.volunteer = volunteer;
        this.numberOfClothes = numberOfClothes;
        this.date = date;
        this.done = done;
    }

    public String getCypher() {
        return cypher;
    }

    public void setCypher(String cypher) {
        this.cypher = cypher;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
    }

    public int getNumberOfClothes() {
        return numberOfClothes;
    }

    public void setNumberOfClothes(int numberOfClothes) {
        this.numberOfClothes = numberOfClothes;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("dd. MMMM yyyy"));
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }
}
