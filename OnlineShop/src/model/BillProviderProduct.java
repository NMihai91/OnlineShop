package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the bill_provider_product database table.
 * 
 */
@Entity
@Table(name="bill_provider_product")
@NamedQuery(name="BillProviderProduct.findAll", query="SELECT b FROM BillProviderProduct b")
public class BillProviderProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int billProviderProductId;

	private int quntityRequested;

	//bi-directional many-to-one association to Bill
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="billId")
	private Bill bill;

	//bi-directional many-to-one association to ProviderProduct
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="providerProductId")
	private ProviderProduct providerProduct;

	public BillProviderProduct() {
	}

	public int getBillProviderProductId() {
		return this.billProviderProductId;
	}

	public void setBillProviderProductId(int billProviderProductId) {
		this.billProviderProductId = billProviderProductId;
	}

	public int getQuntityRequested() {
		return this.quntityRequested;
	}

	public void setQuntityRequested(int quntityRequested) {
		this.quntityRequested = quntityRequested;
	}

	public Bill getBill() {
		return this.bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public ProviderProduct getProviderProduct() {
		return this.providerProduct;
	}

	public void setProviderProduct(ProviderProduct providerProduct) {
		this.providerProduct = providerProduct;
	}

}