package data;

import java.math.BigDecimal;
import java.util.Objects;


public class ProductCardModel {

    private final String imageUrl;
    private final String name;
    private final BigDecimal price;

    private ProductCardModel(String imageUrl, String name, BigDecimal price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductCardModel that)) return false;
        return Objects.equals(imageUrl, that.imageUrl) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl, name, price);
    }

    public static class Builder {
        private String imageUrl;
        private String name;
        private BigDecimal price;

        public Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductCardModel build() {
            return new ProductCardModel(name, imageUrl, price);
        }
    }
}
