package controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Bill;
import model.BillProviderProduct;
import model.Customer;
import model.Product;
import model.Provider;
import model.ProviderProduct;
import service.BillProviderProductService;
import service.BillService;
import service.CustomerService;
import service.ProductService;
import service.ProviderProductService;

public class OwnBuyersController {

	@FXML
	private ListView<String> listBuyers;

	ObservableList<String> obList = FXCollections.observableArrayList();

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
	}

	private Provider logProvider = new Provider();

	private ProviderProductService providerProductService = new ProviderProductService();
	private BillProviderProductService billProviderProductService = new BillProviderProductService();
	private BillService billService = new BillService();
	private CustomerService customerService = new CustomerService();
	private ProductService productService = new ProductService();

	public void setProvider(Provider tempProvider) {
		logProvider = tempProvider;
	}

	public void showOwnBuyers() {
		List<ProviderProduct> listProviderProducts = providerProductService.getAllProviderProducts();
		List<BillProviderProduct> listBillProviderProducts = billProviderProductService.getAllBillProviderProducts();
		List<Bill> listBills = billService.getAllBills();
		List<Customer> listCustomers = customerService.getAllCustomers();
		List<Product> listProducts = productService.getAllProducts();

		for (int i = 0; i < listProviderProducts.size(); i++) {
			if (logProvider.getProviderId() == listProviderProducts.get(i).getProvider().getProviderId()) {
				for (int j = 0; j < listBillProviderProducts.size(); j++) {
					if (listProviderProducts.get(i).getProviderProductId() == listBillProviderProducts.get(j)
							.getProviderProduct().getProviderProductId()) {
						for (int k = 0; k < listBills.size(); k++) {
							if (listBills.get(k).getBillId() == listBillProviderProducts.get(j).getBill().getBillId()
									&& listBills.get(k).getIsPayd() == 1) {
								for (int l = 0; l < listCustomers.size(); l++) {
									if (listCustomers.get(l).getCustomerId() == listBills.get(k).getCustomer()
											.getCustomerId()) {
										for (int m = 0; m < listProducts.size(); m++) {
											if (listProducts.get(m).getProductId() == listProviderProducts.get(i)
													.getProduct().getProductId()) {
												obList.add("Name: " + listCustomers.get(l).getCustomerName()
														+ ", Product name: " + listProducts.get(m).getProductName()
														+ ", Pieces: "
														+ listBillProviderProducts.get(j).getQuntityRequested());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		listBuyers.setItems(obList);
	}

}
