package com.bookhub.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    private Books books;

    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;

    @Column(name = "price_per_unit", nullable = false)
    private BigDecimal pricePerUnit;

    public OrderItems() {
    }

    public OrderItems(Integer orderItemId, Orders orders, Books books, Integer orderQuantity, BigDecimal pricePerUnit) {
        this.orderItemId = orderItemId;
        this.orders = orders;
        this.books = books;
        this.orderQuantity = orderQuantity;
        this.pricePerUnit = pricePerUnit;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItems that = (OrderItems) o;
        return Objects.equals(orderItemId, that.orderItemId) && Objects.equals(orders, that.orders) && Objects.equals(books, that.books) && Objects.equals(orderQuantity, that.orderQuantity) && Objects.equals(pricePerUnit, that.pricePerUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, orders, books, orderQuantity, pricePerUnit);
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderItemId=" + orderItemId +
                ", orders=" + orders +
                ", books=" + books +
                ", orderQuantity=" + orderQuantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
