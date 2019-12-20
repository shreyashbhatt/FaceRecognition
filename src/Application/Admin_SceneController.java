package Application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

public class Admin_SceneController {
	private int passwd_length=8;
	@FXML
	AnchorPane admin_Pane,second_Pane,first_Pane;
	@FXML
	PasswordField root_Passwd;
	@FXML
	Button add_AdminBtn;
	@FXML
	Button add_UserBtn;
	@FXML
	Button profile;
	@FXML
	Button logout;
	@FXML
	Button change_Passwd,root_Submit;
	@FXML
	private void add_AdminBtnClicked() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("add_AdminScene.fxml"));
			AnchorPane pane=loader.load();
			admin_Pane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void add_UserBtnClicked() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("Add_UserScene.fxml"));
			AnchorPane pane=loader.load();
			admin_Pane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void profile_BtnClicked() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("Admin_ProfileScene.fxml"));
			AnchorPane pane=loader.load();
			admin_Pane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void logout() {
		try {
			System.out.println(UserSession.getemail());
			UserSession.cleanUserSession();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("First_Scene.fxml"));
			AnchorPane pane=loader.load();
			admin_Pane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void change_Password() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("change_Passwd.fxml"));
			AnchorPane pane=loader.load();
			admin_Pane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void root_SubmitClicked() {
		UserSession.getInstace("admin");
		Connection cn=DB_Connection.connectdb();
		try {
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("Select password from login where email='admin'");
			if(rs.next()==true) {
				if(root_Passwd.getText().equals(rs.getString(1))) {
					second_Pane.setDisable(false);
					second_Pane.setOpacity(1.0);
					first_Pane.setDisable(true);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@FXML
	private void passwd_Validate() {
		if(root_Passwd.getText().length()>=passwd_length) {
			root_Submit.setDisable(false);
		}
		else {
			root_Submit.setDisable(true);
		}
		
	}
	
	

}
