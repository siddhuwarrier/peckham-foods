package models;

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
}
