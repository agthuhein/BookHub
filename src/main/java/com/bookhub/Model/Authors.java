package com.bookhub.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Authors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer authorId;

    @NotBlank(message = "Author name cannot be empty")
    @Column(name = "author_name", length = 255, nullable = false)
    private String authorName;

//    @ManyToMany(mappedBy = "authors")
//    private Set<Books> books = new HashSet<>();

    public Authors() {
    }

    public Authors(Integer authorId, String authorName, Set<Books> books) {
        this.authorId = authorId;
        this.authorName = authorName;
        //this.books = books;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Authors authors = (Authors) o;
        return Objects.equals(authorId, authors.authorId) && Objects.equals(authorName, authors.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, authorName);
    }

    @Override
    public String toString() {
        return "Authors{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
