package com.bookhub.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

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
                "bookId=" + authorId +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
