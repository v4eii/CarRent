package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Cars;
import carrent.entity.DeliveryCar;
import carrent.entity.RefsPK;
import carrent.entity.Reservation;
import com.flexganttfx.model.Activity;
import com.flexganttfx.model.Layer;
import com.flexganttfx.model.Row;
import com.flexganttfx.model.activity.MutableActivityBase;
import com.flexganttfx.model.layout.GanttLayout;
import com.flexganttfx.view.GanttChart;
import com.flexganttfx.view.graphics.ActivityEvent;
import com.flexganttfx.view.graphics.ListViewGraphics;
import com.flexganttfx.view.graphics.renderer.ActivityBarRenderer;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class ReservViewController implements Initializable {

    private AddIssueViewController addIssueCtrl;
    private DeleteIssueViewController deleteIssueCtrl;
    
    private static Stage addStage,
                         deleteStage;
    
    class ModelObject<
            P extends Row<?,?,?>,
            K extends Row<?,?,?>,
            A extends Activity> extends Row<P, K, A>
    {}
    
    class Auto extends ModelObject<AutoPark, Delivery, Activity>            // class Auto extends ModelObject<AutoPark, Row<?, ?, ?>, Activity>
    {}
    
    class AutoPark extends ModelObject<Row<?, ?, ?>, Auto, Activity>
    {}
    
    class Delivery extends ModelObject<Auto, Row<?, ?, ?>, Activity>
    {}
    
    class Deliv extends MutableActivityBase<DeliveryCar>
    {

        public Deliv(DeliveryCar obj, String name)
        {
            super(name);
            Calendar cal = Calendar.getInstance();
            cal.setTime(obj.getDateDelivery());
            setStartTime(cal.toInstant());
            cal.setTime(obj.getDateBack());
            setEndTime(cal.toInstant());
            setUserObject(obj);
        }
        
    }
    
    @FXML
    private BorderPane bPane;
    @FXML
    private Button btnAddDeliv,
                   btnDeleteDeliv;
    
    private AutoPark park;
    private Layer layer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        park = new AutoPark();
        park.setExpanded(true);
        park.setName("��������");
        GanttChart<ModelObject<?, ?, ?>> chart = new GanttChart<>(park);
        ListViewGraphics<ModelObject<?, ?, ?>> graphic = chart.getGraphics();
        
//        graphic.setOnActivityChangeFinished((ActivityEvent event) ->
//        {
//            System.out.println("!!");
//        });
        
        graphic.setOnActivityHorizontalDragStarted((ActivityEvent event) ->
        {
            DBBean.getInstance().showWarningDialog("��������", "����������� ���� ���������");
        });
        
        graphic.setOnActivityEndTimeChangeFinished((ActivityEvent event) ->
        {
            if (DBBean.getInstance().showConfirmDialog("��������� ������", "�������������� �������", "�� ������� ��� ������ �������� �������� ��������� \n"
                    + event.getActivityRef().getActivity().getName() + " � " + new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Date.from(event.getOldTime()))
                    + "\n�� " + new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Date.from(event.getActivityRef().getActivity().getEndTime())) + "?").get() == ButtonType.OK)
            {
                for (DeliveryCar val : DBBean.getInstance().getDeliveryCarJPACtrl().findDeliveryCarEntities())
                {
                    if (event.getActivityRef().getActivity().getName().equals(
                            DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, val.getCars().getIdMake())).getRefName().concat(
                            " " + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, val.getCars().getIdModel())).getRefName()).concat(
                            " " + val.getCars().getNumberregistration())) && Date.from(event.getActivityRef().getActivity().getStartTime()).equals(val.getDateDelivery()))
                    {
                        try
                        {
                            val.setDateBack(Date.from(event.getActivityRef().getActivity().getEndTime()));
                            DBBean.getInstance().getDeliveryCarJPACtrl().edit(val);
                        }
                        catch (Exception ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "������ ��������� �������", "");
                        }
                    }
                }
            }
        });
        
        graphic.setOnActivityStartTimeChangeStarted((ActivityEvent event) ->
        {
            DBBean.getInstance().showWarningDialog("��������", "��������� ������ ���� ������ ���������");
        });
        
        /*graphic.setOnActivityVerticalDragStarted((ActivityEvent event) ->
        {
            DBBean.getInstance().showWarningDialog("��������", "����������� ���� ���������");
        });*/
        
        //graphic.setOnActivityDragStarted(value);      //TODO ����� ��������� ������ �����������
        
        layer = new Layer("��������������");
        
        for (Cars val : DBBean.getInstance().getCarsJPACtrl().findCarsEntities())
        {
            Auto auto1 = new Auto();
            auto1.setName(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, val.getIdMake())).getRefName().concat(
                    " " + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, val.getIdModel())).getRefName()).concat(
                    " " + val.getNumberregistration()));
            auto1.userObjectProperty();
            park.getChildren().add(auto1);
            for (DeliveryCar a : val.getDeliveryCarCollection())
            {
                if (a == null)
                    continue;
                Deliv deliv1 = new Deliv(a, auto1.getName());
                Delivery d = new Delivery();
                d.setName(DBBean.getInstance().getClientJPACtrl().findClients(a.getDeliveryCarPK().getIdClient()).getLastName().concat(
                        " " + DBBean.getInstance().getClientJPACtrl().findClients(a.getDeliveryCarPK().getIdClient()).getFirstName().concat(
                        " " + DBBean.getInstance().getClientJPACtrl().findClients(a.getDeliveryCarPK().getIdClient()).getMiddleName())));
                d.addActivity(layer, deliv1);
                auto1.getChildren().add(d);
                //auto1.addActivity(layer, deliv1);
            }
            
        }
        chart.getLayers().add(layer);
        graphic.setActivityRenderer(Deliv.class, GanttLayout.class, new ActivityBarRenderer<>(graphic, "Auto Park"));
        graphic.showAllActivities();
        
        bPane.centerProperty().setValue(chart);
        btnAddDeliv.addEventHandler(ActionEvent.ACTION, addDelivEvent);
        btnDeleteDeliv.addEventHandler(ActionEvent.ACTION, deleteDelivEvent);
    }
    
    public BorderPane getReservPane()
    {
        return bPane;
    }
    
    private final EventHandler<ActionEvent> addDelivEvent = (ActionEvent event) ->
    {
        try
        {
            Reservation reserv = new Reservation();
            DeliveryCar deliver = new DeliveryCar();
            FXMLLoader loader = new FXMLLoader(ReservViewController.this.getClass().getResource("/carrent/view/AddIssueView.fxml"));
            AnchorPane aPane = loader.load();
            addIssueCtrl = loader.getController();
            addIssueCtrl.setDeliveryObj(deliver);
            addIssueCtrl.setReservationObj(reserv);
            
            Scene scene = new Scene(aPane);
            addStage = new Stage();
            addStage.setScene(scene);
            addStage.setTitle("���������� ������");
            addStage.getIcons().add(new Image("/carrent/resources/document_new.png"));
            addStage.showAndWait();
            
            if (addIssueCtrl.getRecordAdd())
            {
                deliver = addIssueCtrl.getDelivery();
                Delivery d = new Delivery();
                Deliv deliv = new Deliv(deliver, DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, deliver.getCars().getIdMake())).getRefName().concat(
                    " " + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, deliver.getCars().getIdModel())).getRefName()).concat(
                    " " + deliver.getCars().getNumberregistration()));
                
                d.setName(DBBean.getInstance().getClientJPACtrl().findClients(deliver.getDeliveryCarPK().getIdClient()).getLastName().concat(
                        " " + DBBean.getInstance().getClientJPACtrl().findClients(deliver.getDeliveryCarPK().getIdClient()).getFirstName().concat(
                        " " + DBBean.getInstance().getClientJPACtrl().findClients(deliver.getDeliveryCarPK().getIdClient()).getMiddleName())));
                
                for (Auto val : park.getChildren())
                {
                    if (val.getName().equals(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, deliver.getCars().getIdMake())).getRefName().concat(
                    " " + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, deliver.getCars().getIdModel())).getRefName()).concat(
                    " " + deliver.getCars().getNumberregistration())))
                    {
                        val.getChildren().add(d);
                        d.addActivity(layer, deliv);
                    }
                }
            }
        }
        catch (IOException ex)
        {
            DBBean.getInstance().showErrDialog(ex, "������ �������� ����� ����������", "");
        }
    };
    
    private final EventHandler<ActionEvent> deleteDelivEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(ReservViewController.this.getClass().getResource("/carrent/view/DeleteIssueView.fxml"));
                BorderPane bPane = loader.load();
                deleteIssueCtrl = loader.getController();
                deleteIssueCtrl.initData();
                Scene scene = new Scene(bPane);                 //TODO �������� �������������� ������� ����� ��������
                deleteStage = new Stage();
                deleteStage.setScene(scene);
                deleteStage.setTitle("�������� ������");
                deleteStage.getIcons().add(new Image("/carrent/resources/document_delete.png"));
                deleteStage.showAndWait();
            }
            catch (IOException ex)
            {
                DBBean.getInstance().showErrDialog(ex, "������ �������� ����� ����������", "");
            }
        }
    };

    public static Stage getDeleteStage()
    {
        return deleteStage;
    }

    public static Stage getAddStage()
    {
        return addStage;
    }
    
}
