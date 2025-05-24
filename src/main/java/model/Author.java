package main.java.model;

public class Author {

    private int id;
    private String name;
    private Integer birthYear;
    private String nationality;

    public Author(int id, String name, Integer birthYear, String nationality) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    public Author() {
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

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
