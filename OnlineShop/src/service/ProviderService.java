package service;

import java.util.List;

import javax.persistence.Persistence;

import model.Provider;
import dao.ProviderDAO;

public class ProviderService {

	private ProviderDAO providerDAO;

	public ProviderService() {
		try {
			providerDAO = new ProviderDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addProvider(Provider c) {
		providerDAO.create(c);
	}

	public void updateProvider(Provider u) {
		providerDAO.update(u);
	}

	public List<Provider> getAllProvider() {
		return providerDAO.findAll();
	}
	
	public List<Provider> findProviderById(String id){
		return providerDAO.findById(id);
	}
}
