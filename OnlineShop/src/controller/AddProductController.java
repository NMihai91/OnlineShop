package controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Product;
import model.Provider;
import model.ProviderProduct;
import service.ProductService;
import service.ProviderProductService;

public class AddProductController {

	@FXML
	private TextField txtAddProvProdName;

	@FXML
	private TextField txtAddProvProdPrice;

	@FXML
	private TextField txtAddProvProdQuantity;

	@FXML
	private TextArea txtAddProvProdDescription;

	@FXML
	private Label lblErrorMessage;

	@FXML
	private Label lblSuccessMessage;

	@FXML
	private Button closeButton;

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
	}

	private boolean verifyInfo(String name, String price, String quantity) {
		if (name.equals("") || price.equals("") || quantity.equals("")) {
			return false;
		}
		return true;
	}

	private boolean verifyPrice(String price) {
		String decimalPattern = "[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
		boolean match = Pattern.matches(decimalPattern, price);

		return match;
	}

	private boolean verifyQuantity(String quantity) {
		String decimalPattern = "([0-9]*)";
		boolean match = Pattern.matches(decimalPattern, quantity);

		return match;
	}

	private Provider logProvider = new Provider();

	public void getProvider(Provider provider) {
		logProvider = provider;
	}

	public void btnProvAddProduct(ActionEvent event) throws Exception {
		if (!verifyInfo(txtAddProvProdName.getText(), txtAddProvProdPrice.getText(),
				txtAddProvProdQuantity.getText())) {
			lblErrorMessage.setText("Error: please provide the details!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (!verifyPrice(txtAddProvProdPrice.getText())) {
			lblErrorMessage.setText("Error: Incorrect price!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (!verifyQuantity(txtAddProvProdQuantity.getText())) {
			lblErrorMessage.setText("Error: Incorrect quantity!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else {
			lblErrorMessage.setVisible(false);

			ProductService productService = new ProductService();
			Product logProduct = new Product();

			// parse String to double for price in top of the function
			double price = DecimalFormat.getNumberInstance().parse(txtAddProvProdPrice.getText()).doubleValue();

			logProduct.setProductName(txtAddProvProdName.getText());
			logProduct.setDescription(txtAddProvProdDescription.getText());
			logProduct.setPrice(price);
			productService.addProduct(logProduct);

			ProviderProduct logProviderProduct = new ProviderProduct();
			ProviderProductService providerProductService = new ProviderProductService();

			// retreive the last Product to add to the ProviderProduct table in Product
			// column
			List<Product> products = productService.getAllProducts();
			Product lastProduct = products.get(products.size() - 1);

			// retreive current Provider
			logProviderProduct.setProvider(logProvider);
			logProviderProduct.setQuantityAvailable(Integer.parseInt(txtAddProvProdQuantity.getText()));
			logProviderProduct.setProduct(lastProduct);
			providerProductService.addProviderProduct(logProviderProduct);

			lblSuccessMessage.setText("The product was added");
			lblSuccessMessage.setVisible(true);

			txtAddProvProdName.clear();
			txtAddProvProdPrice.clear();
			txtAddProvProdQuantity.clear();
			txtAddProvProdDescription.clear();
		}
	}
}
