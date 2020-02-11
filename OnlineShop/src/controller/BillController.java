package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Account;
import model.Bill;
import model.BillProviderProduct;
import model.Customer;
import model.Product;
import model.ProviderProduct;
import service.AccountService;
import service.BillProviderProductService;
import service.BillService;
import service.ProductService;
import service.ProviderProductService;

public class BillController {

	@FXML
	private Label txtErrorPaid;

	@FXML
	private Label txtSuccesPaid;

	@FXML
	private Button btnPaid;

	@FXML
	private Label txtTotalPrice;

	@FXML
	private ListView<Bill> listUnpaidBills;

	@FXML
	private ListView<String> txtInfoBill;

	@FXML
	private Label txtError;

	@FXML
	private Label txtSucces;

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
	}

	// need when we want to update bill with isPaid 1
	Customer logCustomer = new Customer();

	public void getCustomer(Customer tempCustomer) {
		logCustomer = tempCustomer;
	}

	ObservableList<Bill> obsUnpaidList = FXCollections.observableArrayList();
	ObservableList<String> obsInfoList = FXCollections.observableArrayList();

	BillService billService = new BillService();
	BillProviderProductService billProviderProductService = new BillProviderProductService();
	ProviderProductService providerProductService = new ProviderProductService();
	ProductService productService = new ProductService();
	AccountService accountService = new AccountService();

	// needs Bill to update when I want to pay a restance, save info from ListView
	// when I select a row
	Bill currentBill = new Bill();

	// here save the products from list to pay from DB
	List<Product> productPaidNow = new ArrayList<Product>();
	List<ProviderProduct> providerProductPaidNow = new ArrayList<ProviderProduct>();
	List<BillProviderProduct> billProviderProductPaidNow = new ArrayList<BillProviderProduct>();

	// total price for every bill
	private double price = 0;

	public void btnShowUnpaidBills() {

		obsUnpaidList.clear();
		List<Bill> tempBills = billService.getAllBills();
		List<BillProviderProduct> tempListBillProviderProducts = billProviderProductService
				.getAllBillProviderProducts();
		List<ProviderProduct> tempListProviderProducts = providerProductService.getAllProviderProducts();
		List<Product> tempListProducts = productService.getAllProducts();

		// add in ListView just bill with isPaid = 0
		for (int i = 0; i < tempBills.size(); i++) {
			if (tempBills.get(i).getIsPayd() == 0
					&& tempBills.get(i).getCustomer().getCustomerId() == logCustomer.getCustomerId()) {
				obsUnpaidList.add(new Bill(tempBills.get(i).getBillId(), tempBills.get(i).getEmiteDate(),
						tempBills.get(i).getIsPayd()));
			}
		}

		listUnpaidBills.setItems(obsUnpaidList);

		listUnpaidBills.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			// it's necessary to be clear and populate with info when I'll select something
			productPaidNow.clear();
			providerProductPaidNow.clear();
			billProviderProductPaidNow.clear();
			price = 0;

			if (newSelection != null) {
				obsInfoList.clear();

				// needs Bill to update when I want to pay a restance
				currentBill = newSelection;

				// tree in which i find all inform step by step after id
				for (int i = 0; i < tempListBillProviderProducts.size(); i++) {
					if (newSelection.getBillId() == tempListBillProviderProducts.get(i).getBill().getBillId()) {
						for (int j = 0; j < tempListProviderProducts.size(); j++) {
							if (tempListBillProviderProducts.get(i).getProviderProduct()
									.getProviderProductId() == tempListProviderProducts.get(j).getProviderProductId()) {
								// here can find quantity available
								// System.out.println(tempListProviderProducts.get(j).getQuantityAvailable());
								for (int k = 0; k < tempListProducts.size(); k++) {
									if (tempListProviderProducts.get(j).getProduct().getProductId() == tempListProducts
											.get(k).getProductId()) {
										obsInfoList
												.add("NAME: " + tempListProducts.get(k).getProductName() + ", PIECES: "
														+ tempListBillProviderProducts.get(i).getQuntityRequested()
														+ ", INFO: " + tempListProducts.get(k).getDescription()
														+ ", PRICE/PIECE: " + tempListProducts.get(k).getPrice());
										price += tempListBillProviderProducts.get(i).getQuntityRequested()
												* tempListProducts.get(k).getPrice();

										productPaidNow.add(tempListProducts.get(k));
										providerProductPaidNow.add(tempListProviderProducts.get(j));
										billProviderProductPaidNow.add(tempListBillProviderProducts.get(i));
									}
								}
							}
						}
					}
				}

				txtTotalPrice.setText(String.valueOf(price));
				txtTotalPrice.setVisible(true);
				txtInfoBill.setItems(obsInfoList);
			}
		});
	}

	public void btnPay() {
		byte isPaid = 1;

		Account myAccount = new Account();
		myAccount.setSum(logCustomer.getAccounts().get(0).getSum() - price);
		myAccount.setCustomer(logCustomer);
		myAccount.setAccountId(logCustomer.getAccounts().get(0).getAccountId());

		if (price <= logCustomer.getAccounts().get(0).getSum()) {
			if ((isAvailableInDb() == true)) {
				accountService.updateAccount(myAccount);
				// update isPaid
				currentBill.setIsPayd(isPaid);
				currentBill.setCustomer(logCustomer);
				billService.updateBill(currentBill);

				updateNumberOfProductsFromDb();

				txtSucces.setText("Successful payment!");
				txtSucces.setVisible(true);
				txtError.setVisible(false);
			} else {
				txtError.setText("Unevailable products!");
				txtSucces.setVisible(false);
				txtError.setVisible(true);
			}
		} else {
			txtError.setText("Insufficient amount!");
			txtError.setVisible(true);
			txtSucces.setVisible(false);
		}

	}

	public boolean isAvailableInDb() {
		for (int i = 0; i < providerProductPaidNow.size(); i++) {
			if (providerProductPaidNow.get(i).getQuantityAvailable() >= billProviderProductPaidNow.get(i)
					.getQuntityRequested()) {
				return true;
			}
		}
		return false;
	}

	public void updateNumberOfProductsFromDb() {
		List<ProviderProduct> tempProviderProducts = providerProductService.getAllProviderProducts();
		ProviderProduct tempProviderProduct = new ProviderProduct();

		for (int i = 0; i < tempProviderProducts.size(); i++) {
			for (int j = 0; j < providerProductPaidNow.size(); j++) {
				if (tempProviderProducts.get(i).getProviderProductId() == providerProductPaidNow.get(j)
						.getProviderProductId()) {
					tempProviderProduct.setProduct(providerProductPaidNow.get(j).getProduct());
					tempProviderProduct.setProvider(providerProductPaidNow.get(j).getProvider());
					tempProviderProduct.setProviderProductId(tempProviderProducts.get(i).getProviderProductId());
					tempProviderProduct.setQuantityAvailable(providerProductPaidNow.get(j).getQuantityAvailable()
							- billProviderProductPaidNow.get(j).getQuntityRequested());

					providerProductService.updateProviderProduct(tempProviderProduct);
				}
			}
		}
	}
}
