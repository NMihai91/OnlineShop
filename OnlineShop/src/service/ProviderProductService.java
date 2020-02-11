package service;

import java.util.List;

import javax.persistence.Persistence;

import dao.ProviderProductDAO;
import model.ProviderProduct;;

public class ProviderProductService {

	private ProviderProductDAO providerProductDAO;

	public ProviderProductService() {
		try {
			providerProductDAO = new ProviderProductDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addProviderProduct(ProviderProduct c) {
		providerProductDAO.create(c);
	}

	public void updateProviderProduct(ProviderProduct u) {
		providerProductDAO.update(u);
	}
	
	public void removeProviderProduct(ProviderProduct u) {
		providerProductDAO.remove(u, u.getProviderProductId());
	}

	public List<ProviderProduct> getAllProviderProducts() {
		return providerProductDAO.findAll();
	}	
	
	public List<ProviderProduct> findById(String id){
		return providerProductDAO.findById(id);
	}
}
