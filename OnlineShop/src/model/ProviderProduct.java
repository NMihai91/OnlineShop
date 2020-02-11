package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the provider_product database table.
 * 
 */
@Entity
@Table(name="provider_product")
@NamedQuery(name="ProviderProduct.findAll", query="SELECT p FROM ProviderProduct p")
public class ProviderProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int providerProductId;

	private int quantityAvailable;

	//bi-directional many-to-one association to BillProviderProduct
	@OneToMany(mappedBy="providerProduct")
	private List<BillProviderProduct> billProviderProducts;

	//bi-directional many-to-one association to Product
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="productId")
	private Product product;

	//bi-directional many-to-one association to Provider
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="providerId")
	private Provider provider;

	public ProviderProduct() {
	}
	
	public ProviderProduct(int quantity) {
		this.quantityAvailable = quantity;
	}
	
	public int getProviderProductId() {
		return this.providerProductId;
	}

	public void setProviderProductId(int providerProductId) {
		this.providerProductId = providerProductId;
	}

	public int getQuantityAvailable() {
		return this.quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public List<BillProviderProduct> getBillProviderProducts() {
		return this.billProviderProducts;
	}

	public void setBillProviderProducts(List<BillProviderProduct> billProviderProducts) {
		this.billProviderProducts = billProviderProducts;
	}

	public BillProviderProduct addBillProviderProduct(BillProviderProduct billProviderProduct) {
		getBillProviderProducts().add(billProviderProduct);
		billProviderProduct.setProviderProduct(this);

		return billProviderProduct;
	}

	public BillProviderProduct removeBillProviderProduct(BillProviderProduct billProviderProduct) {
		getBillProviderProducts().remove(billProviderProduct);
		billProviderProduct.setProviderProduct(null);

		return billProviderProduct;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public String toString() {
		return "ProviderProduct [providerProductId=" + providerProductId + ", quantityAvailable=" + quantityAvailable
				+ ", product=" + product + "]";
	}

}