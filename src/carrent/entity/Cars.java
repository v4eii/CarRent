/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import carrent.beans.DBBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author v4e
 */
@Entity
@Table(catalog = "car_rent", schema = "")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Cars.findAll", query = "SELECT c FROM Cars c"),
    @NamedQuery(name = "Cars.findByIdCar", query = "SELECT c FROM Cars c WHERE c.idCar = :idCar"),
    @NamedQuery(name = "Cars.findByIdModel", query = "SELECT c FROM Cars c WHERE c.idModel = :idModel"),
    @NamedQuery(name = "Cars.findByIdMake", query = "SELECT c FROM Cars c WHERE c.idMake = :idMake"),
    @NamedQuery(name = "Cars.findByIdColor", query = "SELECT c FROM Cars c WHERE c.idColor = :idColor"),
    @NamedQuery(name = "Cars.findByReleasedate", query = "SELECT c FROM Cars c WHERE c.releasedate = :releasedate"),
    @NamedQuery(name = "Cars.findByVin", query = "SELECT c FROM Cars c WHERE c.vin = :vin"),
    @NamedQuery(name = "Cars.findByNumberregistration", query = "SELECT c FROM Cars c WHERE c.numberregistration = :numberregistration"),
    @NamedQuery(name = "Cars.findByKacko", query = "SELECT c FROM Cars c WHERE c.kacko = :kacko"),
    @NamedQuery(name = "Cars.findByOsago", query = "SELECT c FROM Cars c WHERE c.osago = :osago"),
    @NamedQuery(name = "Cars.findByTechnicalinspection", query = "SELECT c FROM Cars c WHERE c.technicalinspection = :technicalinspection"),
    @NamedQuery(name = "Cars.findByMilleage", query = "SELECT c FROM Cars c WHERE c.milleage = :milleage"),
    @NamedQuery(name = "Cars.findByIdStat", query = "SELECT c FROM Cars c WHERE c.idStat = :idStat")
})
public class Cars implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_car")
    private Integer idCar;
    @Basic(optional = false)
    @Column(name = "id_model")
    private int idModel;
    @Basic(optional = false)
    @Column(name = "id_make")
    private int idMake;
    @Basic(optional = false)
    @Column(name = "id_color")
    private int idColor;
    @Basic(optional = false)
    @Column(name = "Release_date")
    @Temporal(TemporalType.DATE)
    private Date releasedate;
    @Basic(optional = false)
    private String vin;
    @Basic(optional = false)
    @Column(name = "Number_registration")
    private String numberregistration;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date kacko;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date osago;
    @Basic(optional = false)
    @Column(name = "Technical_inspection")
    @Temporal(TemporalType.DATE)
    private Date technicalinspection;
    @Basic(optional = false)
    private int milleage;
    @Basic(optional = false)
    @Column(name = "id_stat")
    private int idStat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cars")
    private Collection<DeliveryCar> deliveryCarCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCar")
    private Collection<Reservation> reservationCollection;
    
    @Transient
    public DBBean.RECORD_STATE rec_state = DBBean.RECORD_STATE.SAVED;

    public Cars()
    {
    }

    public Cars(Integer idCar)
    {
        this.idCar = idCar;
    }

    public Cars(Integer idCar, int idModel, int idMake, int idColor, Date releasedate, String vin, String numberregistration, Date kacko, Date osago, Date technicalinspection, int milleage, int idStat)
    {
        this.idCar = idCar;
        this.idModel = idModel;
        this.idMake = idMake;
        this.idColor = idColor;
        this.releasedate = releasedate;
        this.vin = vin;
        this.numberregistration = numberregistration;
        this.kacko = kacko;
        this.osago = osago;
        this.technicalinspection = technicalinspection;
        this.milleage = milleage;
        this.idStat = idStat;
    }

    public Integer getIdCar()
    {
        return idCar;
    }

    public void setIdCar(Integer idCar)
    {
        this.idCar = idCar;
    }

    public int getIdModel()
    {
        return idModel;
    }

    public void setIdModel(int idModel)
    {
        this.idModel = idModel;
    }

    public int getIdMake()
    {
        return idMake;
    }

    public void setIdMake(int idMake)
    {
        this.idMake = idMake;
    }

    public int getIdColor()
    {
        return idColor;
    }

    public void setIdColor(int idColor)
    {
        this.idColor = idColor;
    }

    public Date getReleasedate()
    {
        return releasedate;
    }

    public void setReleasedate(Date releasedate)
    {
        this.releasedate = releasedate;
    }

    public String getVin()
    {
        return vin;
    }

    public void setVin(String vin)
    {
        this.vin = vin;
    }

    public String getNumberregistration()
    {
        return numberregistration;
    }

    public void setNumberregistration(String numberregistration)
    {
        this.numberregistration = numberregistration;
    }

    public Date getKacko()
    {
        return kacko;
    }

    public void setKacko(Date kacko)
    {
        this.kacko = kacko;
    }

    public Date getOsago()
    {
        return osago;
    }

    public void setOsago(Date osago)
    {
        this.osago = osago;
    }

    public Date getTechnicalinspection()
    {
        return technicalinspection;
    }

    public void setTechnicalinspection(Date technicalinspection)
    {
        this.technicalinspection = technicalinspection;
    }

    public int getMilleage()
    {
        return milleage;
    }

    public void setMilleage(int milleage)
    {
        this.milleage = milleage;
    }

    public int getIdStat()
    {
        return idStat;
    }

    public void setIdStat(int idStat)
    {
        this.idStat = idStat;
    }

    @XmlTransient
    public Collection<DeliveryCar> getDeliveryCarCollection()
    {
        return deliveryCarCollection == null ? new ArrayList<>() : deliveryCarCollection;
    }

    public void setDeliveryCarCollection(Collection<DeliveryCar> deliveryCarCollection)
    {
        this.deliveryCarCollection = deliveryCarCollection;
    }

    @XmlTransient
    public Collection<Reservation> getReservationCollection()
    {
        return reservationCollection == null ? new ArrayList<>() : reservationCollection;
    }

    public void setReservationCollection(Collection<Reservation> reservationCollection)
    {
        this.reservationCollection = reservationCollection;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idCar != null ? idCar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cars))
        {
            return false;
        }
        Cars other = (Cars) object;
        if ((this.idCar == null && other.idCar != null) || (this.idCar != null && !this.idCar.equals(other.idCar)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.Cars[ idCar=" + idCar + " ]";
    }
    
}
