package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the bill database table.
 * 
 */
@Entity
@NamedQuery(name="Bill.findAll", query="SELECT b FROM Bill b")
public class Bill implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int billId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date emiteDate;

	private byte isPayd;

	//bi-directional many-to-one association to Customer
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customerId")
	private Customer customer;

	//bi-directional many-to-one association to BillProviderProduct
	@OneToMany(mappedBy="bill")
	private List<BillProviderProduct> billProviderProducts;

	public Bill() {
	}
	
	public Bill(int id, Date date, byte isPayd) {
		this.billId = id;
		this.isPayd = isPayd;
		this.emiteDate = date;
	}

	public int getBillId() {
		return this.billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public Date getEmiteDate() {
		return this.emiteDate;
	}

	public void setEmiteDate(Date emiteDate) {
		this.emiteDate = emiteDate;
	}

	public byte getIsPayd() {
		return this.isPayd;
	}

	public void setIsPayd(byte isPayd) {
		this.isPayd = isPayd;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<BillProviderProduct> getBillProviderProducts() {
		return this.billProviderProducts;
	}

	public void setBillProviderProducts(List<BillProviderProduct> billProviderProducts) {
		this.billProviderProducts = billProviderProducts;
	}

	public BillProviderProduct addBillProviderProduct(BillProviderProduct billProviderProduct) {
		getBillProviderProducts().add(billProviderProduct);
		billProviderProduct.setBill(this);

		return billProviderProduct;
	}

	public BillProviderProduct removeBillProviderProduct(BillProviderProduct billProviderProduct) {
		getBillProviderProducts().remove(billProviderProduct);
		billProviderProduct.setBill(null);

		return billProviderProduct;
	}

	@Override
	public String toString() {
		return "EMITE DATE: " + emiteDate;
	}
	
	

}