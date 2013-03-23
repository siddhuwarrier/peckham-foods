package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Copyright (c) Cisco systems 2013. All rights reserved.
 * Date: 23/03/2013
 */
@Entity
@Table(name = "Product")
public class Product extends Model {
    @Id
    public String productId;

    @Constraints.Required
    public String productName;

    @Constraints.Required
    public String ean;

    @Constraints.Required
    public Double listPrice;

    @Constraints.Required
    public Double wholesalePrice;

    /**
     * Finder method
     */
    public static Finder<String, Product> find = new Finder<String, Product>(
            String.class, Product.class
    );

    public Product(String productId, String productName, String ean, Double listPrice, double wholesalePrice) {
        this.productId = productId;
        this.productName = productName;
        this.ean = ean;

        this.listPrice = listPrice;
        this.wholesalePrice = wholesalePrice;
    }
}
