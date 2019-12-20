package Application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class change_Passwd {
	@FXML
	private Label warning;
	@FXML
	private Button back;
	@FXML
	private Tooltip text_passwd;
	@FXML
	private PasswordField text1;
	@FXML
	private PasswordField text2;
	@FXML
	private AnchorPane password_Pane;
	@FXML
	private Button submit;
	@FXML
	private void back_BtnClicked() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("admin_Scene.fxml"));
			AnchorPane pane=loader.load();
			password_Pane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	private void submit_Clicked() {
		Connection cn=DB_Connection.connectdb();
		try {
			Statement stmt=cn.createStatement();
			stmt.executeUpdate("update registration set password='"+text1.getText()+"'"+"where email='"+UserSession.getemail()+"';");
			stmt.executeUpdate("update login set password='"+text1.getText()+"'"+"where email='"+UserSession.getemail()+"';");
			cn.close();
			System.out.println("successful");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			text1.setText("");
			text2.setText("");
			
		}
		
	}
	@FXML
	private void passwd_validate() {
		String value="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{8,20}$";
		if(!text1.getText().isEmpty()&&!text2.getText().isEmpty()) {
			if(text1.getText().equals(text2.getText())) {
			if(text1.getText().matches(value)==true) {
				submit.setDisable(false);
			}
			else {
				//submit.setDisable(true);
				warning.setText("*Password not in correct Format!!");
			}
		}
			else {
				//submit.setDisable(true);
				warning.setText("*Both fields should have same value!!");
			}
	
		}
		else {
			submit.setDisable(true);
		}
	}
	


}
