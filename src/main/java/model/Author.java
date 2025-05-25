package main.java.model;

import java.util.Objects;

public class Author {
    private int id;
    private String name;
    private Integer birthYear;
    private String nationality;

    // Constructors (default və parametrli)

    public Author() {
    }

    public Author(int id, String name, Integer birthYear, String nationality) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    // Getter və Setter methodları

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

    // toString() methodu

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", nationality='" + nationality + '\'' +
                '}';
    }

    // equals() və hashCode() methodları

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Author author)) return false;
        return id == author.id && Objects.equals(name, author.name) && Objects.equals(birthYear, author.birthYear) && Objects.equals(nationality, author.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthYear, nationality);
    }

}