package com.coding.sales.ProductUtil;

import java.math.BigDecimal;

public class Product {

    private String productId;
    private String name;
    private String unit;
    private BigDecimal price;
    private String[] discounts;

    public Product(String productId, String name, String unit, BigDecimal price, String[] discounts) {
        this.productId = productId;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.discounts = discounts;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String[] getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String[] discounts) {
        this.discounts = discounts;
    }
}
