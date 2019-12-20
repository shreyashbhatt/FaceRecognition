 package Application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Login_registerController {
	private int passwd_length=8;
	@FXML
	private AnchorPane login_register_Scene;
	@FXML
	private Button login_backBtn;
	@FXML
	private Button login_Btn;
	@FXML
	private Label warning;
	@FXML
	private TextField email;
	@FXML
	private TextField passwd;
	@FXML
	private Label passwd_status;
	@FXML
	private Label email_status;
	@FXML
	private void back_BtnClicked() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("First_Scene.fxml"));
			AnchorPane pane=loader.load();
			login_register_Scene.getChildren().setAll(pane);
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void login_BtnClicked() {
		try {
			System.out.println(UserSession.getemail());
			Connection cn=DB_Connection.connectdb();
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("select * from login where email='"+email.getText()+"'and password='"+passwd.getText()+"'");
			if(rs.next()==true) {
				System.out.println(rs.getString(1));
				if(rs.getString(1).equals(email.getText())&&rs.getString(2).equals(passwd.getText())) {
					UserSession.getInstace(email.getText());
					FXMLLoader loader=new FXMLLoader(getClass().getResource("admin_Scene.fxml"));
					AnchorPane pane=loader.load();
					login_register_Scene.getChildren().setAll(pane);
				}
				else {
					warning.setText("Email or Password Incorrect!!");
				}
			}
			else {
				warning.setText("Email or Password Incorrect!!");
			}
			
			} catch (IOException e) {
				e.printStackTrace();
			}catch(SQLException e) {
				warning.setText("Email or Password Incorrect!!");	
			}
		
	}
	@FXML 
	private void email_Validate() {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if(!email.getText().isEmpty()) {
			if(email.getText().matches(regex)==false) {
				email_status.setText("*Please Enter Correct E-Mail Address!!!");
			}
			else {
				email_status.setText("");
			}
		}
	}
	@FXML
	private void check_EmptyPasswd() {
		if(passwd.getText().length()>=passwd_length) {
			passwd_status.setText("");
			login_Btn.setDisable(false);
		}
		else {
			passwd_status.setText("*Password length should be at least 8 character");
			login_Btn.setDisable(true);
		}
	}
	
	
	

}
