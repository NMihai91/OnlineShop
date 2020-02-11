package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int productId;

	private String description;

	private double price;

	private String productName;

	//bi-directional many-to-one association to ProviderProduct
	@OneToMany(mappedBy="product")
	private List<ProviderProduct> providerProducts;

	public Product() {
	}
	
	//my constructor
	public Product(int productId, String productName, double price, String description) {
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.description = description;
	}
	
	public Product(String name) {
		this.productName = name;
	}

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<ProviderProduct> getProviderProducts() {
		return this.providerProducts;
	}

	public void setProviderProducts(List<ProviderProduct> providerProducts) {
		this.providerProducts = providerProducts;
	}

	public ProviderProduct addProviderProduct(ProviderProduct providerProduct) {
		getProviderProducts().add(providerProduct);
		providerProduct.setProduct(this);

		return providerProduct;
	}

	public ProviderProduct removeProviderProduct(ProviderProduct providerProduct) {
		getProviderProducts().remove(providerProduct);
		providerProduct.setProduct(null);

		return providerProduct;
	}

	@Override
	public String toString() {
		return productName;
	}
	

}