package main.java.model;

public class Author {
    private Integer id;
    private String name;
    private Integer birthYear;
    private String nationality;

    public Author() {

    }

    public Author(Integer id, String name, Integer birthYear, String nationality) {
        this.name = name;
        this.id = id;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", name: " + name + ", birthYear: " + birthYear + ", nationality: " + nationality + "}" ;
    }
}
