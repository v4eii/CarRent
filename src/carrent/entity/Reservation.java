/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r"),
    @NamedQuery(name = "Reservation.findByIdContract", query = "SELECT r FROM Reservation r WHERE r.idContract = :idContract"),
    @NamedQuery(name = "Reservation.findByConfidant", query = "SELECT r FROM Reservation r WHERE r.confidant = :confidant"),
    @NamedQuery(name = "Reservation.findByStartreserve", query = "SELECT r FROM Reservation r WHERE r.startreserve = :startreserve"),
    @NamedQuery(name = "Reservation.findByEndreserve", query = "SELECT r FROM Reservation r WHERE r.endreserve = :endreserve"),
    @NamedQuery(name = "Reservation.findByComment", query = "SELECT r FROM Reservation r WHERE r.comment = :comment"),
    @NamedQuery(name = "Reservation.findByDatecancel", query = "SELECT r FROM Reservation r WHERE r.datecancel = :datecancel"),
    @NamedQuery(name = "Reservation.findByUsercancel", query = "SELECT r FROM Reservation r WHERE r.usercancel = :usercancel")
})
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contract")
    private Integer idContract;
    @Basic(optional = false)
    private String confidant;
    @Basic(optional = false)
    @Column(name = "Start_reserve")
    @Temporal(TemporalType.DATE)
    private Date startreserve;
    @Basic(optional = false)
    @Column(name = "End_reserve")
    @Temporal(TemporalType.DATE)
    private Date endreserve;
    @Column(name = "Comment")           //!!!!
    private String comment;
    @Column(name = "Date_cancel")
    @Temporal(TemporalType.DATE)
    private Date datecancel;
    @Column(name = "User_cancel")
    private String usercancel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
    private Collection<DeliveryCar> deliveryCarCollection;
    @JoinColumn(name = "id_car", referencedColumnName = "id_car")
    @ManyToOne(optional = false)
    private Cars idCar;
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    @ManyToOne(optional = false)
    private Clients idClient;

    public Reservation()
    {
    }

    public Reservation(Integer idContract)
    {
        this.idContract = idContract;
    }

    public Reservation(Integer idContract, String confidant, Date startreserve, Date endreserve)
    {
        this.idContract = idContract;
        this.confidant = confidant;
        this.startreserve = startreserve;
        this.endreserve = endreserve;
    }

    public Integer getIdContract()
    {
        return idContract;
    }

    public void setIdContract(Integer idContract)
    {
        this.idContract = idContract;
    }

    public String getConfidant()
    {
        return confidant;
    }

    public void setConfidant(String confidant)
    {
        this.confidant = confidant;
    }

    public Date getStartreserve()
    {
        return startreserve;
    }

    public void setStartreserve(Date startreserve)
    {
        this.startreserve = startreserve;
    }

    public Date getEndreserve()
    {
        return endreserve;
    }

    public void setEndreserve(Date endreserve)
    {
        this.endreserve = endreserve;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getDatecancel()
    {
        return datecancel;
    }

    public void setDatecancel(Date datecancel)
    {
        this.datecancel = datecancel;
    }

    public String getUsercancel()
    {
        return usercancel;
    }

    public void setUsercancel(String usercancel)
    {
        this.usercancel = usercancel;
    }

    @XmlTransient
    public Collection<DeliveryCar> getDeliveryCarCollection()
    {
        return deliveryCarCollection;
    }

    public void setDeliveryCarCollection(Collection<DeliveryCar> deliveryCarCollection)
    {
        this.deliveryCarCollection = deliveryCarCollection;
    }

    public Cars getIdCar()
    {
        return idCar;
    }

    public void setIdCar(Cars idCar)
    {
        this.idCar = idCar;
    }

    public Clients getIdClient()
    {
        return idClient;
    }

    public void setIdClient(Clients idClient)
    {
        this.idClient = idClient;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idContract != null ? idContract.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation))
        {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.idContract == null && other.idContract != null) || (this.idContract != null && !this.idContract.equals(other.idContract)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.Reservation[ idContract=" + idContract + " ]";
    }
    
}
