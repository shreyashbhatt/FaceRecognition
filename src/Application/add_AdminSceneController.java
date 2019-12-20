package Application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class add_AdminSceneController {
	 String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
     String Small_chars = "abcdefghijklmnopqrstuvwxyz"; 
     String numbers = "0123456789"; 
     String symbols = "!@#$%&"; 
     String values = Capital_chars + Small_chars + 
                     numbers + symbols; 
     String password=new String();
	@FXML
	Button back_Btn;
	@FXML
	TextField email;
	@FXML
	Label passwd_view;
	@FXML
	Label warning;
	@FXML
	AnchorPane add_admin;
	@FXML
	Button register_Btn;
	@FXML
	Button generate_Password;
	@FXML
	private void generate_PasswdClicked(ActionEvent event) {
		int passwd_length=10;
		password=generate_Passwd(passwd_length);
		if(!password.isEmpty()) {
			passwd_view.setText(password);
			register_Btn.setDisable(false);
		}
		else {
			register_Btn.setDisable(true);
		}
		
		
	}
	
	private String generate_Passwd(int passwd_length) {
		Random rndm_obj = new Random(); 
		  
        char[] password = new char[passwd_length]; 
  
        for (int i = 0; i < passwd_length; i++) 
        { 
            password[i] = values.charAt(rndm_obj.nextInt(values.length())); 
        } 
        String convert_ToString=new String(password);
        return convert_ToString; 
		
	}

	@FXML
	private void back_BtnClicked(ActionEvent event) {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("admin_Scene.fxml"));
			AnchorPane pane=loader.load();
			add_admin.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void register_BtnClicked(ActionEvent event) {
		Connection cn=DB_Connection.connectdb();
		Statement stmt;
		try {
			stmt=cn.createStatement();
			String query="Insert into registration (email,password) values('"+email.getText()+"','"+passwd_view.getText()+"')";
			String query2="Insert into login (email,password) values('"+email.getText()+"','"+passwd_view.getText()+"')";
			stmt.executeUpdate(query);
			stmt.executeUpdate(query2);
			cn.close();
			
		} catch (SQLException e) {
			warning.setText("*User already exists!!");
		}
		finally {
			email.setText("");
			passwd_view.setText("Password");
			
		}
		
	}
	@FXML
	private void email_validate() {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if(!email.getText().isEmpty()) {
	       
	       if(email.getText().matches(regex)==true) {
	    	   generate_Password.setDisable(false);
			}
	       else {
	    	   generate_Password.setDisable(true);
	       }
		}
	}
	

}
