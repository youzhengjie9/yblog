package com.boot.pojo;

import java.io.Serializable;

public class category implements Serializable {

    private String categoryName;
    private int categoryCount;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    @Override
    public String toString() {
        return "category{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryCount=" + categoryCount +
                '}';
    }
}
