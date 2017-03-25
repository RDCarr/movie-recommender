package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {
	private static final String url = "jdbc:mysql://144.217.243.209:3306/project?useSSL=true";
	private static final String user = "project";
	private static final String password = "letmeinplease";

	private static Connection con;
	private static Statement stmt;

	protected Stage stage;
	protected Scene menu,loginMenu;
	protected TextField txfSearch, numEntries, txfID, txfPW;
	protected Label txtInfo;
	protected VBox root;
	protected GridPane login;

	//protected Map<String, Account> users = new HashMap<String, Account>();

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			login = buildLoginPane();
			loginMenu = new Scene(login);
			loginMenu.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//root = buildMenuPane();
			//menu = new Scene(root);
			//menu.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setTitle("Movie Recommender");
			stage.setScene(loginMenu);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public GridPane buildLoginPane() {
		GridPane gridLogin = new GridPane();
		gridLogin.setVgap(5);
		gridLogin.setHgap(10);

		Label id = new Label("User ID");
		gridLogin.add(id, 0, 0);

		//Label pw = new Label("Password");
		//gridLogin.add(pw, 1, 0);

		txfID = new TextField();
		gridLogin.add(txfID, 0, 1);

		//txfPW = new TextField();
		//gridLogin.add(txfPW, 1, 1);

		Button login = new Button("Login");
		login.setOnAction(new loginEventHandler());
		gridLogin.add(login, 0, 2);

		//Button createNewUser = new Button("Create new Account");
		//createNewUser.setOnAction(new createNewUserEventHandler());
		//gridLogin.add(createNewUser,1, 2);

		return gridLogin;
	}

	/*private class loginEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try{

				if(users.containsKey(txfID.getText())) {
					if(users.get(txfID.getText()).getPassword().equals(txfPW.getText())) {
						user = users.get(txfID.getText()).getPerson();
						root.getChildren().clear();
						root.getChildren().add(displayInfo());
					}
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}*/

	private class loginEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			root = buildMenuPane();
			menu = new Scene(root);
			menu.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(menu);
		}
	}

	private VBox buildMenuPane() {
		VBox menu = new VBox();
		menu.setPadding(new Insets(5,5,5,5));
		menu.setSpacing(10);



		GridPane buttons = new GridPane();
		buttons.setAlignment(Pos.CENTER);
		buttons.setHgap(5);
		buttons.setVgap(5);
		buttons.setPadding(new Insets(5,5,5,5));

		numEntries = new TextField();
		numEntries.setPromptText("Top X Movies");
		numEntries.setMaxWidth(150);
		buttons.add(numEntries, 0, 0);

		Button topMovies = new Button("Top Movies");
		topMovies.setOnAction(e -> {
			try {
				seeTopMovies();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		topMovies.setMinSize(150, 25);
		buttons.add(topMovies, 0, 1);

		Button search = new Button("Search");
		search.setOnAction(e -> {
			System.out.println("stuff");
		});
		search.setMinSize(150, 25);
		buttons.add(search, 1, 2);

//		Button topGenre = new Button("Search");
//		topGenre.setOnAction(e -> {
//			System.out.println("stuff");
//		});
//		topGenre.setMinSize(150, 25);
//		menu.add(topGenre, 2, 0);

//		Button director = new Button("Search");
//		director.setOnAction(e -> {
//			System.out.println("stuff");
//		});
//		director.setMinSize(150, 25);
//		menu.add(director, 3, 0);

//		Button actor = new Button("Search");
//		actor.setOnAction(e -> {
//			System.out.println("stuff");
//		});
//		actor.setMinSize(150, 25);
//		menu.add(actor, 4, 0);

//		Button tag = new Button("Search");
//		tag.setOnAction(e -> {
//			System.out.println("stuff");
//		});
//		tag.setMinSize(150, 25);
//		menu.add(tag, 0, 1);

		Button topPopularDirectors = new Button("Top Popular Directors");
		topPopularDirectors.setOnAction(e -> {
			try {
				seeTopDirectors();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		topPopularDirectors.setMinSize(150, 25);
		buttons.add(topPopularDirectors, 1, 1);

		Button topPopularActors = new Button("Top Popular Actors");
		topPopularActors.setOnAction(e -> {
			seeTopActors();
		});
		topPopularActors.setMinSize(150, 25);
		buttons.add(topPopularActors, 2, 1);

//		Button rating = new Button("Search");
//		rating.setOnAction(e -> {
//			System.out.println("stuff");
//		});
//		rating.setMinSize(150, 25);
//		menu.add(rating, 3, 1);

		Button movieTags = new Button("User Ratings");
		movieTags.setOnAction(e -> {
			System.out.println("stuff");
		});
		movieTags.setMinSize(150, 25);
		buttons.add(movieTags, 2, 2);

		txfSearch = new TextField();
		txfSearch.setPromptText("Search");
		buttons.add(txfSearch, 0, 2);

		txtInfo = new Label();
		txtInfo.setText("This is our Movie Recommender");

		menu.getChildren().addAll(buttons, txtInfo);
		menu.setAlignment(Pos.CENTER);

		return menu;
	}

	private void seeTopMovies() throws SQLException{
		ArrayList<String> list = new ArrayList<>();

		String query = "SELECT DISTINCT title, rtAllCriticsRating FROM movies ORDER BY rtAllCriticsRating DESC LIMIT ?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, Integer.parseInt(numEntries.getText()));

		ResultSet rs = ps.executeQuery();

		while(rs.next()) {
			list.add(rs.getString("title"));
		}

		rs.close();
		ps.close();

		setPages(list);
	}

	private void seeTopDirectors() throws SQLException{
		ArrayList<String> list = new ArrayList<>();

		String query = "SELECT directorName, rtAllCriticsRating FROM movie_directors, movies WHERE movie_directors.movieID = movies.id";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, Integer.parseInt(numEntries.getText()));

		ResultSet rs = ps.executeQuery();

		while(rs.next()) {
			list.add(rs.getString("directorName"));
		}

		rs.close();
		ps.close();

		/*
		try {
			Scanner scan = new Scanner(new File("C:/Users/Jacob/Documents/My Classes/Fall 2016/Intro to Cyber Security/Workspace/Database_GUI/src/directors"));
			while(scan.hasNextLine()) {
				list.add(scan.nextLine());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}*/


		setPages(list);
	}

	private void seeTopActors() {
		ArrayList<String> list = new ArrayList<>();
		try {
			Scanner scan = new Scanner(new File("C:/Users/Jacob/Documents/My Classes/Fall 2016/Intro to Cyber Security/Workspace/Database_GUI/src/actors"));
			while(scan.hasNextLine()) {
				list.add(scan.nextLine());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}
		setPages(list);
	}

	private void setPages(ArrayList<String> list) {
		int elementsPerPage = 5;
		int pages = list.size()/elementsPerPage;
		Pagination page = new Pagination(pages,0);
		page.setPageFactory(new Callback<Integer, Node>() {
			public Node call(Integer pageIndex) {
				VBox movieList = new VBox(5);
				int currPage = pageIndex*elementsPerPage;
				for(int i=currPage; i < currPage+elementsPerPage; i++){
					Label str = new Label(list.get(i));
					movieList.getChildren().add(str);
				}
				return movieList;
			}
		});

		root.getChildren().remove(1);
		root.getChildren().add(page);
		stage.setHeight(303);
	}

	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException{
		con = DriverManager.getConnection(url, user, password);
		System.out.println("Database connected sucessfully");
		stmt = con.createStatement();

		launch(args);
	}
}
