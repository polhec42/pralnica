package database;

public class User {

    private String cypher;
    private String name;
    private int id;

    public User(String cypher, String name, int id){
        this.cypher = cypher;
        this.name = name;
        this.id = id;
    }

    public String getCypher() {
        return cypher;
    }

    public void setCypher(String cypher) {
        this.cypher = cypher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
