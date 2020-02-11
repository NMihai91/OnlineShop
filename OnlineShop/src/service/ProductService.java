package service;

import java.util.List;

import javax.persistence.Persistence;

import dao.ProductDAO;
import model.Product;

public class ProductService {

	private ProductDAO productDAO;

	public ProductService() {
		try {
			productDAO = new ProductDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addProduct(Product c) {
		productDAO.create(c);
	}

	public void updateProduct(Product u) {
		productDAO.update(u);
	}
	
	public void removeProduct(Product u) {
		productDAO.remove(u, u.getProductId());
	}

	public List<Product> getAllProducts() {
		return productDAO.findAll();
	}
	
	public List<Product> findById(String id){
		return productDAO.findById(id);
	}
}
