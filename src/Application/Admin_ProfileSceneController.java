package Application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class Admin_ProfileSceneController {
	static boolean check_FName,check_LName,check_email;
	@FXML
	private AnchorPane profile_scene;
	@FXML
	private TextField first_Name;
	@FXML
	private TextField last_Name;
	@FXML
	private TextField email;
	@FXML
	private TextField address;
	@FXML
	private MenuButton gender;
	@FXML
	private Button register;
	@FXML
	private Button back_Btn;
	@FXML
	private Tooltip text_FName,text_LName,text_Email;
	@FXML
	private void male_Selected() {
		gender.setText("Male");
	}
	@FXML
	private void female_Selected() {
		gender.setText("Female");
	}
	@FXML
	private void others_Selected() {
		gender.setText("Others");
	}
	@FXML
	private void select_Selected() {
		gender.setText("Select");
	}
	@FXML
	private void back_BtnClicked(ActionEvent event) {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("admin_Scene.fxml"));
			AnchorPane pane=loader.load();
			profile_scene.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void first_NameValidate() {
		String value="^[a-zA-Z]*$";
		if(!first_Name.getText().isEmpty()) {
			if(first_Name.getText().matches(value)==true) {
				text_FName.setText("*Only Alphabets are allowed!!");
				check_FName=true;
			}
			else {
				text_FName.setText("*Incorrect First Name ");
				check_FName=false;
			}
		}
		enable_registerButton();
	}
	@FXML
	private void last_NameValidate() {
		String value="^[a-zA-Z]*$";
		if(!last_Name.getText().isEmpty()) {
			if(last_Name.getText().matches(value)==true) {
				text_LName.setText("*Only Alphabets are allowed!!");
				check_LName=true;
			}
			else {
				text_LName.setText("*Incorrect Last Name ");
				check_LName=false;
			}	
		}
		enable_registerButton();
	}
	@FXML
	private void email_Validate() {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if(!email.getText().isEmpty()) {
	       if(email.getText().matches(regex)==true) {
	    	   text_Email.setText("*Email Format Should be Correct");
	    	   check_email=true;
			}
	       else {
	    	   text_Email.setText("Incorrect Email");
	    	   check_email=false;
	       }
		}
		enable_registerButton();
	}
	private void enable_registerButton() {
		if(!first_Name.getText().isEmpty()&&!last_Name.getText().isEmpty()&&
			!email.getText().isEmpty()&&!address.getText().isEmpty()&&
			!gender.getText().equalsIgnoreCase("Select")) {
			if(check_FName==true&&check_LName==true&&check_email==true) {
			register.setDisable(false);
			}
			else {
			register.setDisable(true);
			}
		}
	}
	@FXML
	private void add_details() {
		
	}
	
	

}
