package carrent.view.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class LoginViewController implements Initializable {

    @FXML
    private TextField usrField, srvField;
    @FXML
    private PasswordField pswField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        usrField.setText(System.getProperty("user.name"));
        usrField.requestFocus();
    }
    
    public String getUser()
    {
        return usrField.getText().trim();
    }

    public String getPassword()
    {
        return pswField.getText().trim();
    }

    public String getServer()
    {
        return srvField.getText().trim();
    }
    
}
