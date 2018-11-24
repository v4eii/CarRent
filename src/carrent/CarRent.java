/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent;

import carrent.beans.DBBean;
import carrent.view.controllers.LoginViewController;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.security.auth.login.AccountException;

/**
 *
 * @author v4e
 */
public class CarRent extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception
    {
     
        try
        {
            URL urlLog = getClass().getResource("/carrent/view/LoginView.fxml");
            FXMLLoader loader = new FXMLLoader(urlLog);
            AnchorPane aPane = loader.load();
            LoginViewController lCtrl = loader.getController();
            Dialog dialog = new Alert(Alert.AlertType.NONE);
            dialog.setTitle("Подключение");
            dialog.setGraphic(new ImageView("/carrent/resources/car_sedan_blue.png"));
            DialogPane dialogPane = new DialogPane();
            dialogPane.getButtonTypes().add(ButtonType.OK);
            dialogPane.getButtonTypes().add(ButtonType.CANCEL);
            dialogPane.setContent(aPane);
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                DBBean.getInstance().setUser(lCtrl.getUser());
                DBBean.getInstance().setPassword(DBBean.getInstance().getMD5String(lCtrl.getPassword()));
                DBBean.getInstance().setServer(lCtrl.getServer());
                DBBean.getInstance().setRole(DBBean.getInstance().getUsersJPACtrl().getUserRole(lCtrl.getUser()));
                if (DBBean.getInstance().getUsersJPACtrl().findUsers(DBBean.getInstance().getUser()) == null ||
                        !DBBean.getInstance().getUsersJPACtrl().getUserPassword(DBBean.getInstance().getUser()).equals(DBBean.getInstance().getPassword()))
                {
                    DBBean.getInstance().showErrDialog(new AccountException("Пользователь не зарегистрирован в системе. Обратитесь к администратору."), "Ошибка авторизации", "");
                    Platform.exit();
                }
                Date tmp = DBBean.getInstance().getUsersJPACtrl().getUserDateDelete(lCtrl.getUser());
                if (tmp != null && tmp.before(new Date()))
                {
                    DBBean.getInstance().showErrDialog(new AccountException("Пользователь был заблокирован! Обратитесь к администратору."), "Ошибка авторизации", "");
                    Platform.exit();
                }

                Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
                Parent root = FXMLLoader.load(getClass().getResource("MForm.fxml"));
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle(String.format("Подменный парк автомобилей. Пользователь %s Сервер @%s", DBBean.getInstance().getUser(), DBBean.getInstance().getServer()));
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/carrent/resources/car_sedan_blue.png")));
                stage.setMaximized(true);
                stage.show();
                
            }
            else
            {
                stop();
                Platform.exit();
            }
        }
        catch(IOException ex)
        {
            DBBean.getInstance().showErrDialog(ex, "Ошибка авторизации", "");
            Platform.exit();
        }
        
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
