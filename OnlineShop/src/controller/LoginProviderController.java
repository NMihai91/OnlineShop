package controller;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import model.Product;
import model.Provider;
import model.ProviderProduct;
import model.User;
import service.ProductService;
import service.ProviderProductService;

public class LoginProviderController {
	@FXML
	private TableColumn<Product, String> colName;

	@FXML
	private Label loggedAs;

	@FXML
	private Label lblSuccessMessage;

	@FXML
	private Label lblErrorMessage;

	@FXML
	private TableColumn<Product, Double> colPrice;

	@FXML
	private TableColumn<Product, String> colDescription;

	@FXML
	private TableView<Product> txtProvOwnProducts;

	@FXML
	private TextField editPrice;

	@FXML
	private TextArea editDescription;

	@FXML
	private TextField editName;

	@FXML
	private TextField editQuantity;

	ObservableList<Product> obList = FXCollections.observableArrayList();

	private boolean verifyUpdateInfo(String name, String price, String quantity) {
		if (name.equals("") || price.equals("") || quantity.equals("")) {
			return false;
		}
		return true;
	}

	private boolean verifyUpdatePrice(String price) {
		String decimalPattern = "[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
		boolean match = Pattern.matches(decimalPattern, price);

		return match;
	}

	private boolean verifyUpdateQuantity(String quantity) {
		String decimalPattern = "([0-9]*)";
		boolean match = Pattern.matches(decimalPattern, quantity);

		return match;
	}

	private User logUser = new User();
	private Provider logProvider = new Provider();

	public void currentUser(User user) {
		logUser.setUserName(user.getUserName());
	}

	public void showUser() {
		loggedAs.setText(logUser.getUserName());
	}

	public void getProvider(Provider provider) {
		logProvider = provider;
	}

	public void buildTable() {
		ProductService productService = new ProductService();
		List<Product> products = productService.getAllProducts();

		ProviderProductService providerProductService = new ProviderProductService();

		for (int i = 0; i < products.size(); i++) {
			if (providerProductService.getAllProviderProducts().get(i).getProvider().getProviderId() == logProvider
					.getProviderId()) {
				obList.add(
						new Product(providerProductService.getAllProviderProducts().get(i).getProduct().getProductId(),
								providerProductService.getAllProviderProducts().get(i).getProduct().getProductName(),
								providerProductService.getAllProviderProducts().get(i).getProduct().getPrice(),
								providerProductService.getAllProviderProducts().get(i).getProduct().getDescription()));
			}
		}
	}

	// In this object retain the informations from editable cells
	private Product updateProduct = new Product();

	public void showOwnProducts() {
		// clear when I want to refresh the table, delete a product or update
		editName.clear();
		editPrice.clear();
		editDescription.clear();
		editQuantity.clear();

		// we assure the list is clear and after that upload the products
		obList.clear();
		buildTable();

		colName.setCellValueFactory(new PropertyValueFactory<>("productId"));
		colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

		txtProvOwnProducts.setItems(obList);

		txtProvOwnProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				// find quantity in ProviderProduct to show
				ProviderProductService providerProductService = new ProviderProductService();
				ProviderProduct providerProduct = new ProviderProduct();
				List<ProviderProduct> providerProducts = providerProductService.getAllProviderProducts();

				for (int i = 0; i < providerProducts.size(); i++) {
					if (providerProducts.get(i).getProduct().getProductId() == newSelection.getProductId()) {
						providerProduct = providerProducts.get(i);
					}
				}

				updateProduct.setProductId(newSelection.getProductId());

				editName.setText(newSelection.getProductName());
				editPrice.setText(String.valueOf(newSelection.getPrice()));
				editDescription.setText(newSelection.getDescription());

				editQuantity.setText(Integer.toString(providerProduct.getQuantityAvailable()));

			} else {
				txtProvOwnProducts.getSelectionModel().select(0);
			}
		});
	}

	public void deleteRowFromTable() {
		// ProductService productService = new ProductService();

		ProviderProduct deleteProviderProduct = new ProviderProduct();
		ProviderProductService providerProductService = new ProviderProductService();

		List<ProviderProduct> listProviderProducts = providerProductService.getAllProviderProducts();
		for (int i = 0; i < listProviderProducts.size(); i++) {
			if (listProviderProducts.get(i).getProduct().getProductId() == updateProduct.getProductId()) {
				deleteProviderProduct = listProviderProducts.get(i);
				deleteProviderProduct.setQuantityAvailable(0);
			}
		}
		// removeProviderProduct called from providerProductService to delete
		// deleteProviderProduct
		// providerProductService.removeProviderProduct(deleteProviderProduct);
		providerProductService.updateProviderProduct(deleteProviderProduct);
		// productService.removeProduct(updateProduct);

		showOwnProducts();
	}

	@FXML
	public void updateProductFromTable(ActionEvent event) {

		if (!verifyUpdateInfo(editName.getText(), editPrice.getText(), editQuantity.getText())) {
			lblErrorMessage.setText("Error: please provide the details!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (!verifyUpdatePrice(editPrice.getText())) {
			lblErrorMessage.setText("Error: Incorrect price!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (!verifyUpdateQuantity(editQuantity.getText())) {
			lblErrorMessage.setText("Error: Incorrect quantity!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else {
			lblErrorMessage.setVisible(false);

			ProductService productService = new ProductService();

			// retreive all ProviderProductService to extract ProviderProductServiceId
			// because I want to change the quantity with that ID
			ProviderProduct updateProviderProduct = new ProviderProduct();
			ProviderProductService providerProductService = new ProviderProductService();
			List<ProviderProduct> listProviderProducts = providerProductService.getAllProviderProducts();

			for (int i = 0; i < listProviderProducts.size(); i++) {
				if (listProviderProducts.get(i).getProduct().getProductId() == updateProduct.getProductId()) {
					updateProviderProduct = listProviderProducts.get(i);
				}
			}
			updateProviderProduct.setQuantityAvailable(Integer.parseInt(editQuantity.getText()));
			providerProductService.updateProviderProduct(updateProviderProduct);

			updateProduct.setProductName(editName.getText());
			updateProduct.setPrice(Double.parseDouble(editPrice.getText()));
			updateProduct.setDescription(editDescription.getText());
			productService.updateProduct(updateProduct);

			lblSuccessMessage.setText("The product was updated");
			lblSuccessMessage.setVisible(true);

			showOwnProducts();
		}
	}

	public void showAddProductStage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AddProductView.fxml"));

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// send provider to AddProductController
		AddProductController addProductController = loader.getController();
		addProductController.getProvider(logProvider);

		Parent p = loader.getRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(p));
		stage.show();
	}

	public void btnOwnBuyers() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/OwnBuyersView.fxml"));

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// send data to AddProductController
		OwnBuyersController ownBuyersController = loader.getController();
		ownBuyersController.setProvider(logProvider);

		Parent p = loader.getRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(p));
		stage.show();
	}

	public void btnBadBuyers() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/BadBuyersView.fxml"));

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// send data to BadBuyersController
		BadBuyersController badBuyersController = loader.getController();
		badBuyersController.setProvider(logProvider);

		Parent p = loader.getRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(p));
		stage.show();
	}

	public void logOff(ActionEvent event) throws IOException {
		Main main = new Main();
		main.logOff(event);
	}

}
