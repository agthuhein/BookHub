package com.bookhub.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "publishers")
public class Publishers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Integer publisherId;

    @NotBlank(message = "Publisher name cannot be empty")
    @Column(name = "publisher_name", length = 255, nullable = false)
    private String publisherName;

//    @OneToMany(mappedBy = "publishers")
//    private Set<Books> books = new HashSet<>();

    public Publishers() {
    }

    public Publishers(Integer publisherId, String publisherName, Set<Books> books) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        //this.books = books;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Publishers that = (Publishers) o;
        return Objects.equals(publisherId, that.publisherId) && Objects.equals(publisherName, that.publisherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherId, publisherName);
    }

    @Override
    public String toString() {
        return "Publishers{" +
                "publisherId=" + publisherId +
                ", publisherName='" + publisherName + '\'' +
                '}';
    }
}
