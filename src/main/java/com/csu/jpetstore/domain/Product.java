package com.csu.jpetstore.domain;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private static final long serialVersionUID = -7492639752670189553L;
    private String productId;
    private String categoryId;
    private String name;
    private String description;
    private List<Item> itemList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId.trim();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return getName();
    }

    public List<Item> getItemList() { return itemList; }
    public void setItemList(List<Item> itemList) { this.itemList = itemList; }

}
