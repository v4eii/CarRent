package carrent.entity;

import javax.persistence.Column;

/**
 *
 * @author v4e
 */
public class CarReport {
    
    @Column(name = "client_name")
    private String clientFirstName;
    @Column(name = "client_name2")
    private String clientLastName;
    @Column(name = "client_name3")
    private String clientMiddleName;
    @Column(name = "Reserv_in_month")
    private Long reservCount;
    @Column(name = "Car_make")
    private String carMake;
    @Column(name = "Car_model")
    private String carModel;
    @Column(name = "VIN_Car")
    private String carVIN;

    public CarReport()
    {
    }

    public CarReport(String clientFirstName, String clientLastName, String clientMiddleName, Long reservCount, String carMake, String carModel, String carVIN)
    {
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientMiddleName = clientMiddleName;
        this.reservCount = reservCount;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carVIN = carVIN;
    }
    
    

    public String getClientFirstName()
    {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName)
    {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName()
    {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName)
    {
        this.clientLastName = clientLastName;
    }

    public String getClientMiddleName()
    {
        return clientMiddleName;
    }

    public void setClientMiddleName(String clientMiddleName)
    {
        this.clientMiddleName = clientMiddleName;
    }

    public Long getReservCount()
    {
        return reservCount;
    }

    public void setReservCount(Long reservCount)
    {
        this.reservCount = reservCount;
    }

    public String getCarMake()
    {
        return carMake;
    }

    public void setCarMake(String carMake)
    {
        this.carMake = carMake;
    }

    public String getCarModel()
    {
        return carModel;
    }

    public void setCarModel(String carModel)
    {
        this.carModel = carModel;
    }

    public String getCarVIN()
    {
        return carVIN;
    }

    public void setCarVIN(String carVIN)
    {
        this.carVIN = carVIN;
    }

    
    
}
