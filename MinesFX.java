package mines;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MinesFX extends Application {// class that describes application mine sweeper
	private Mines game;// class of the game
	private HBox hb;// contain menu and matrix
	private GridPane menu;// contain reset button and 3 fields
	private GridPane matrix;// board of the game
	private Scene scene;
	private Stage PrimaryStage;
	private StackPane root;
	private boolean playingGame = false;// situation if the game finished or in game
	private int height = 10, width = 10, numOfMines = 10;// initial situation

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.PrimaryStage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("FxStack.fxml"));
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		game = new Mines(height, width, numOfMines);// new game
		playingGame = true;
		menu = createMenu();
		matrix = createMatrix();
		hb = new HBox(menu, matrix);
		root.getChildren().add(hb);
		scene = new Scene(root);
		primaryStage.setTitle("The Amazing Mines Sweeper");
		primaryStage.setScene(scene);
		primaryStage.show();
	}



	private GridPane createMenu() {// return GridPane that describes the menu of the game (data and reset)
		TextField widthTxt = new TextField("" + width);// initial text
		TextField heightTxt = new TextField("" + height);
		TextField minesTxt = new TextField("" + numOfMines);
		Button button = new Button("Reset");
		menu = new GridPane();
		button.setMaxWidth(Double.MAX_VALUE);
		menu.setPadding(new Insets(20));
		menu.setHgap(10);
		menu.setVgap(10);
		widthTxt.setMaxWidth(50);
		heightTxt.setMaxWidth(50);
		minesTxt.setMaxWidth(50);
		menu.add(button, 0, 0, 2, 1);// button reset
		menu.add(new Label("width"), 0, 1);
		menu.add(widthTxt, 1, 1);// button width
		menu.add(new Label("height"), 0, 2);
		menu.add(heightTxt, 1, 2);// button height
		menu.add(new Label("mines"), 0, 3);
		menu.add(minesTxt, 1, 3);// button mines
		menu.setAlignment(Pos.CENTER);
		class Game implements EventHandler<ActionEvent> {// inner class that describes what happen if pressed "reset"
			@Override
			public void handle(ActionEvent e) {
				String one, two, three;
				one = widthTxt.getText();// scan fields
				two = heightTxt.getText();
				three = minesTxt.getText();
				width = Integer.parseInt(one);// convert String to Integer
				height = Integer.parseInt(two);
				numOfMines = Integer.parseInt(three);
				if (numOfMines > width * height)
					message("The mines more over all squares!");

				else {
					try {
						start(PrimaryStage);// reset game with new data
					} catch (Exception e1) {
						e1.printStackTrace();
						return;
					}
				}
			}
		}
		button.setOnAction(new Game());// if pressed on reset
		return menu;
	}

	private GridPane createMatrix() {// return GridPane that describes the board of the game
		FontWeight fw = FontWeight.findByName("BOLD");// setting font in squares
		Font f = Font.font("New Roman", fw, 20);
		matrix = new GridPane();
		matrix.setPadding(new Insets(10));
		class Mouse implements EventHandler<MouseEvent> {// inner class that describes what happen if pressed on square
			private int i, j;// place in matrix

			public Mouse(int i, int j) {// constructor to save the specific place
				this.i = i;
				this.j = j;
			}

			public int getX() {
				return i;// line
			}

			public int getY() {
				return j;// column
			}

			@Override
			public void handle(MouseEvent e) {
				Button b = (Button) e.getSource();
				if (e.getButton() == MouseButton.SECONDARY) {// if right click set/remove flag
					game.toggleFlag(getX(), getY());
					b.setText(game.get(getX(), getY()));// set/remove "F"
				}

				else {// if left click
					if (game.open(i, j) == false && playingGame == true) {// if mine, so show all board
						game.setShowAll(true);// show all board (all squares will open)
						playingGame = false;// game finished
						Show();// show all board
						message("You loose!");
					} else {
						if (game.isDone() && playingGame == true) {// if win game
							game.setShowAll(true);
							playingGame = false;
							Show();// show all board
							message("You win!");

						} else {// normal situation
							b.setText(game.get(i, j));// expose square
							Show();// will expose all neighbors, if the square doesn't has neighbors with mine
									// show just opening place
						}

					}

				}
			}

			private void Show() {// show on the board the data of each square
				int x, y;
				ObservableList<Node> childrens = matrix.getChildren();// get buttons
				for (Node node : childrens) {
					Button b1 = (Button) node;
					x = ((Mouse) b1.getOnMouseClicked()).getX();// height
					y = ((Mouse) b1.getOnMouseClicked()).getY();// width
					if (game.get(x, y) == "X")// if mine
						b1.setStyle("-fx-text-fill: red");
					b1.setText(game.get(x, y));// set data in the board
				}

			}
		}
		for (int i = 0; i < height; i++)// Initialize matrix of buttons in suitable sizes
			for (int j = 0; j < width; j++) {
				Button b = new Button();
				b.setPrefSize(50, 50);
				b.setFont(f);
				b.setText(game.get(i, j));
				b.setOnMouseClicked(new Mouse(i, j));//// if pressed on button from the matrix
				matrix.add(b, j, i, 1, 1);// add button to grid pane
			}
		return matrix;
	}

	private void message(String s)// message on screen
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Amazing Mines");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

}
