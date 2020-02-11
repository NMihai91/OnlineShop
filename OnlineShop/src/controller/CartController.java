package controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Product;

public class CartController implements Initializable {

	@FXML
	private Label totalPrice;

	@FXML
	private ListView<String> cartList;

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
	}

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	ObservableList<String> obsProductList = FXCollections.observableArrayList();

	@FXML
	private List<Product> productsList = new ArrayList<Product>();

	public void getList(List<Product> request) {
		productsList = request;
	}

	@FXML
	Vector<Integer> numberOfProducts = new Vector<>();

	public void getNumberOfProducts(Vector<Integer> number) {
		numberOfProducts = number;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		for (int i = 0; i < productsList.size(); i++) {
			obsProductList.add(productsList.get(i).getProductName());
		}

		cartList.setItems(obsProductList);
	}

	public void btnShowList() {
		obsProductList.clear();
		for (int i = 0; i < productsList.size(); i++) {
			obsProductList.add("NAME: " + productsList.get(i).getProductName() + ", PRICE: "
					+ productsList.get(i).getPrice() + ", REQUESTED PIECES: " + numberOfProducts.get(i));
		}

		cartList.setItems(obsProductList);
	}

	public void btnTotalPrice() {
		double tempPrice = 0;
		for (int i = 0; i < productsList.size(); i++) {
			tempPrice += productsList.get(i).getPrice() * numberOfProducts.get(i);
		}
		totalPrice.setText(String.valueOf(df2.format(tempPrice)));
		totalPrice.setVisible(true);
	}

}
