package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserPK id;

	private String email;

	private byte isCustomer;

	private byte isProvider;

	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;

	private short status;

	private String userName;

	private String userPassword;

	//bi-directional many-to-one association to Customer
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customerId")
	private Customer customer;

	//bi-directional many-to-one association to Provider
	//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="providerId")
	private Provider provider;

	public User() {
	}

	public UserPK getId() {
		return this.id;
	}

	public void setId(UserPK id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getIsCustomer() {
		return this.isCustomer;
	}

	public void setIsCustomer(byte isCustomer) {
		this.isCustomer = isCustomer;
	}

	public byte getIsProvider() {
		return this.isProvider;
	}

	public void setIsProvider(byte isProvider) {
		this.isProvider = isProvider;
	}

	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public short getStatus() {
		return this.status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

}