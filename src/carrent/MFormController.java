package carrent;

import carrent.beans.DBBean;
import carrent.view.controllers.CarsViewController;
import carrent.view.controllers.ClientsViewController;
import carrent.view.controllers.RefsViewController;
import carrent.view.controllers.ReservViewController;
import carrent.view.controllers.UsersViewController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class MFormController implements Initializable {

    
    private CarsViewController carsViewCtrl;
    private RefsViewController refsViewCtrl;
    private UsersViewController usersViewCtrl;
    private ClientsViewController clientsViewCtrl;
    private ReservViewController reservViewCtrl;
    
    @FXML
    private MenuItem mConnect, 
                     mDisconnect,
                     mExit,
                     mUsers,
                     mRefs,
                     mReserv,
                     mCar,
                     mClient,
                     mAbout;
    @FXML
    private BorderPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        mExit.addEventHandler(ActionEvent.ACTION, mExitEvent);
        mAbout.addEventHandler(ActionEvent.ACTION, mAboutEvent);
        mCar.addEventHandler(ActionEvent.ACTION, mCarEvent);
        mRefs.addEventHandler(ActionEvent.ACTION, mRefsEvent);
        mUsers.addEventHandler(ActionEvent.ACTION, mUsersEvent);
        mClient.addEventHandler(ActionEvent.ACTION, mClientsEvent);
        mReserv.addEventHandler(ActionEvent.ACTION, mReservEvent);
        mDisconnect.addEventHandler(ActionEvent.ACTION, mDisconnectEvent);
        mConnect.addEventHandler(ActionEvent.ACTION, mConnectEvent);
    }    
    
    private final EventHandler<ActionEvent> mExitEvent = (ActionEvent event) ->
    {
        if(DBBean.getInstance().showConfirmDialog("Выход из системы", "Вы хотите завершить работу?", "Перед выходом рекомендуется сохранить все изменения.").get() == ButtonType.OK)
            Platform.exit();
    };
    
    private final EventHandler<ActionEvent> mAboutEvent = (ActionEvent event) ->
    {
        DBBean.getInstance().showInfoDialog("О программе", "Пока здесь ничего не работает");
    };
    
    private final EventHandler<ActionEvent> mCarEvent = (ActionEvent event) ->
    {
        if (carsViewCtrl == null)
        {
            try
            {
                URL url = getClass().getResource("/carrent/view/CarsView.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                BorderPane bPane = loader.load();
                carsViewCtrl = loader.getController();
                carsViewCtrl.initData();
                rootPane.centerProperty().setValue(bPane);
                rootPane.setVisible(true);
            }
            catch (IOException ex)
            {
                DBBean.getInstance().showErrDialog(ex, "Ошибка в загрузке формы", "При загрузке формы справочника автомобилей произошла ошибка");
            }
        }
        else
        {
            rootPane.centerProperty().setValue(carsViewCtrl.getCarsPane());
            carsViewCtrl.getCarsPane().setVisible(true);
        }
    };
    
    private final EventHandler<ActionEvent> mRefsEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (refsViewCtrl == null)
            {
                try 
                {
                    FXMLLoader loader = new FXMLLoader(MFormController.this.getClass().getResource("/carrent/view/RefsView.fxml"));
                    BorderPane bPane = loader.load();
                    refsViewCtrl = loader.getController();
                    rootPane.centerProperty().setValue(bPane);
                    rootPane.setVisible(true);
                    bPane.setVisible(true);
                }
                catch (IOException ex)
                {
                    DBBean.getInstance().showErrDialog(ex, "Ошибка загрузки формы", "При загрузке формы справочников произошла ошибка");
                }
            }
            else
            {
                rootPane.centerProperty().setValue(refsViewCtrl.getRefsPane());
                refsViewCtrl.getRefsPane().setVisible(true);
            }
        }
    };
    
    private final EventHandler<ActionEvent> mUsersEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (usersViewCtrl == null)
            {
                try
                {
                    FXMLLoader loader = new FXMLLoader(MFormController.this.getClass().getResource("/carrent/view/UsersView.fxml"));
                    BorderPane bPane = loader.load();
                    usersViewCtrl = loader.getController();
                    usersViewCtrl.initData();
                    rootPane.centerProperty().setValue(bPane);
                    bPane.setVisible(true);
                }
                catch (IOException ex)
                {
                    DBBean.getInstance().showErrDialog(ex, "Ошибка загрузки формы пользователей", "");
                }
            }
            else
            {
                rootPane.centerProperty().setValue(usersViewCtrl.getUsersPane());
                usersViewCtrl.getUsersPane().setVisible(true);
            }
        }
    };
    
    private final EventHandler<ActionEvent> mClientsEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (clientsViewCtrl == null)
            {
                try 
                {
                    FXMLLoader loader = new FXMLLoader(MFormController.this.getClass().getResource("/carrent/view/ClientsView.fxml"));
                    BorderPane bPane = loader.load();
                    clientsViewCtrl = loader.getController();
                    clientsViewCtrl.initData();
                    rootPane.centerProperty().setValue(bPane);
                    bPane.setVisible(true);
                }
                catch (IOException ex)
                {
                    DBBean.getInstance().showErrDialog(ex, "Ошибка загрузки формы клиентов", "");
                }
            }
            else
            {
                rootPane.centerProperty().setValue(clientsViewCtrl.getCLientsPane());
                clientsViewCtrl.getCLientsPane().setVisible(true);
            }
        }
    };
    
    private final EventHandler<ActionEvent> mReservEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (reservViewCtrl == null)
            {
                try
                {
                    FXMLLoader loader = new FXMLLoader(MFormController.this.getClass().getResource("/carrent/view/ReservView.fxml"));
                    BorderPane bPane = loader.load();
                    reservViewCtrl = loader.getController();
                    rootPane.centerProperty().setValue(bPane);
                    bPane.setVisible(true);
                }
                catch (IOException ex)
                {
                    DBBean.getInstance().showErrDialog(ex, "Ошибка загрузки графика резервирования", "");
                }
            }
            else
            {
                rootPane.centerProperty().setValue(reservViewCtrl.getReservPane());
                reservViewCtrl.getReservPane().setVisible(true);
            }
        }
    };
    
    private final EventHandler<ActionEvent> mDisconnectEvent = (ActionEvent event) ->
    {
        if (DBBean.getInstance().showConfirmDialog("Подтверждение", "Отключение", "Вы уверены что хотите отключиться от базы данных?").get() == ButtonType.OK)
        {
            DBBean.getInstance().getEMF().close();
            DBBean.getInstance().setEMFNull();
            mCar.setVisible(false);
            mClient.setVisible(false);
            mRefs.setVisible(false);
            mReserv.setVisible(false);
            mUsers.setVisible(false);
        }
    };
    
    private final EventHandler<ActionEvent> mConnectEvent = (ActionEvent event) ->
    {
//        DBBean.getInstance()  TODO Дописать!
//        mCar.setVisible(true);
//        mClient.setVisible(true);
//        mRefs.setVisible(true);
//        mReserv.setVisible(true);
//        mUsers.setVisible(true);
    };

}
