package Application;
	
import java.io.IOException;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@FXML
	private Button user_Btn;
	@FXML
	private Button root;
	@FXML
	private Button admin_Btn;
	@FXML
	public AnchorPane first_ScenePane;
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("First_Scene.fxml"));
			Scene scene = new Scene(root,650,550);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	 void user_BtnClicked(ActionEvent event) {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("face_Recog_Scene.fxml"));
			AnchorPane pane=loader.load();
			first_ScenePane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML 
	void admin_BtnClicked(ActionEvent event) {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("login_registration.fxml"));
			AnchorPane pane=loader.load();
			first_ScenePane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	void root_Clicked() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("root_Scene.fxml"));
			AnchorPane pane=loader.load();
			first_ScenePane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
