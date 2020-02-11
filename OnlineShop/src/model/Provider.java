package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the provider database table.
 * 
 */
@Entity
@NamedQuery(name="Provider.findAll", query="SELECT p FROM Provider p")
public class Provider implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int providerId;

	private String providerName;

	//bi-directional many-to-one association to ProviderProduct
	@OneToMany(mappedBy="provider")
	private List<ProviderProduct> providerProducts;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="provider")
	private List<User> users;

	public Provider() {
	}
	
	public Provider(int id, String name) {
		this.providerId = id;
		this.providerName = name;
	}

	public int getProviderId() {
		return this.providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return this.providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public List<ProviderProduct> getProviderProducts() {
		return this.providerProducts;
	}

	public void setProviderProducts(List<ProviderProduct> providerProducts) {
		this.providerProducts = providerProducts;
	}

	public ProviderProduct addProviderProduct(ProviderProduct providerProduct) {
		getProviderProducts().add(providerProduct);
		providerProduct.setProvider(this);

		return providerProduct;
	}

	public ProviderProduct removeProviderProduct(ProviderProduct providerProduct) {
		getProviderProducts().remove(providerProduct);
		providerProduct.setProvider(null);

		return providerProduct;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setProvider(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setProvider(null);

		return user;
	}

	@Override
	public String toString() {
		return providerName;
	}



}