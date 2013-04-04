package models;

import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Currency;

@Entity
@Table(name = "Product")
public class Product extends Model {
    public static final String DEFAULT_CURRENCY_CODE = "GBP";

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

    @Constraints.Required
    public Currency currency;

    private static Double EPSILON = 0.0001;

    /**
     * Finder method
     */
    public static Model.Finder<String, Product> find = new Model.Finder<String, Product>(
            String.class, Product.class
    );

    public Product(String productId, String productName, String ean, Double listPrice, double wholesalePrice) {
        this.productId = productId;
        this.productName = productName;
        this.ean = ean;

        this.listPrice = listPrice;
        this.wholesalePrice = wholesalePrice;
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    @Override
    public boolean equals(Object another) {
        boolean isEqual = false;
        if (another instanceof Product) {
            Product anotherProduct = (Product) another;

            if (anotherProduct.productId.equals(productId) &&
                    anotherProduct.productName.equals(productName) &&
                    anotherProduct.ean.equals(ean) &&
                    (anotherProduct.listPrice - listPrice) < EPSILON &&
                    (anotherProduct.wholesalePrice - wholesalePrice) < EPSILON ) {
                isEqual = true;
            }
        }

        return isEqual;
    }
}
