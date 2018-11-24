/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author v4e
 */
@Entity
@Table(name = "delivery_car", catalog = "car_rent", schema = "")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "DeliveryCar.findAll", query = "SELECT d FROM DeliveryCar d"),
    @NamedQuery(name = "DeliveryCar.findByIdContract", query = "SELECT d FROM DeliveryCar d WHERE d.deliveryCarPK.idContract = :idContract"),
    @NamedQuery(name = "DeliveryCar.findByDateDelivery", query = "SELECT d FROM DeliveryCar d WHERE d.dateDelivery = :dateDelivery"),
    @NamedQuery(name = "DeliveryCar.findByIdCar", query = "SELECT d FROM DeliveryCar d WHERE d.deliveryCarPK.idCar = :idCar"),
    @NamedQuery(name = "DeliveryCar.findByIdClient", query = "SELECT d FROM DeliveryCar d WHERE d.deliveryCarPK.idClient = :idClient"),
    @NamedQuery(name = "DeliveryCar.findByIdReason", query = "SELECT d FROM DeliveryCar d WHERE d.idReason = :idReason"),
    @NamedQuery(name = "DeliveryCar.findByNumPowerAttorney", query = "SELECT d FROM DeliveryCar d WHERE d.numPowerAttorney = :numPowerAttorney"),
    @NamedQuery(name = "DeliveryCar.findByEndNumpa", query = "SELECT d FROM DeliveryCar d WHERE d.endNumpa = :endNumpa"),
    @NamedQuery(name = "DeliveryCar.findByDateBack", query = "SELECT d FROM DeliveryCar d WHERE d.dateBack = :dateBack"),
    @NamedQuery(name = "DeliveryCar.findByMilleage", query = "SELECT d FROM DeliveryCar d WHERE d.milleage = :milleage")
})
public class DeliveryCar implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeliveryCarPK deliveryCarPK;
    @Basic(optional = false)
    @Column(name = "date_delivery")
    @Temporal(TemporalType.DATE)
    private Date dateDelivery;
    @Basic(optional = false)
    @Column(name = "id_reason")
    private int idReason;
    @Basic(optional = false)
    @Column(name = "num_power_attorney")
    private int numPowerAttorney;
    @Basic(optional = false)
    @Column(name = "end_numpa")
    @Temporal(TemporalType.DATE)
    private Date endNumpa;
    @Basic(optional = false)
    @Column(name = "date_back")
    @Temporal(TemporalType.DATE)
    private Date dateBack;
    private Integer milleage;
    @JoinColumn(name = "id_client", referencedColumnName = "id_client", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Clients clients;
    @JoinColumn(name = "id_contract", referencedColumnName = "id_contract", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Reservation reservation;
    @JoinColumn(name = "id_car", referencedColumnName = "id_car", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cars cars;

    public DeliveryCar()
    {
    }

    public DeliveryCar(DeliveryCarPK deliveryCarPK)
    {
        this.deliveryCarPK = deliveryCarPK;
    }

    public DeliveryCar(DeliveryCarPK deliveryCarPK, Date dateDelivery, int idReason, int numPowerAttorney, Date endNumpa, Date dateBack)
    {
        this.deliveryCarPK = deliveryCarPK;
        this.dateDelivery = dateDelivery;
        this.idReason = idReason;
        this.numPowerAttorney = numPowerAttorney;
        this.endNumpa = endNumpa;
        this.dateBack = dateBack;
    }

    public DeliveryCar(int idContract, int idCar, int idClient)
    {
        this.deliveryCarPK = new DeliveryCarPK(idContract, idCar, idClient);
    }

    public DeliveryCarPK getDeliveryCarPK()
    {
        return deliveryCarPK;
    }

    public void setDeliveryCarPK(DeliveryCarPK deliveryCarPK)
    {
        this.deliveryCarPK = deliveryCarPK;
    }

    public Date getDateDelivery()
    {
        return dateDelivery;
    }

    public void setDateDelivery(Date dateDelivery)
    {
        this.dateDelivery = dateDelivery;
    }

    public int getIdReason()
    {
        return idReason;
    }

    public void setIdReason(int idReason)
    {
        this.idReason = idReason;
    }

    public int getNumPowerAttorney()
    {
        return numPowerAttorney;
    }

    public void setNumPowerAttorney(int numPowerAttorney)
    {
        this.numPowerAttorney = numPowerAttorney;
    }

    public Date getEndNumpa()
    {
        return endNumpa;
    }

    public void setEndNumpa(Date endNumpa)
    {
        this.endNumpa = endNumpa;
    }

    public Date getDateBack()
    {
        return dateBack;
    }

    public void setDateBack(Date dateBack)
    {
        this.dateBack = dateBack;
    }

    public Integer getMilleage()
    {
        return milleage;
    }

    public void setMilleage(Integer milleage)
    {
        this.milleage = milleage;
    }

    public Clients getClients()
    {
        return clients;
    }

    public void setClients(Clients clients)
    {
        this.clients = clients;
    }

    public Reservation getReservation()
    {
        return reservation;
    }

    public void setReservation(Reservation reservation)
    {
        this.reservation = reservation;
    }

    public Cars getCars()
    {
        return cars;
    }

    public void setCars(Cars cars)
    {
        this.cars = cars;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (deliveryCarPK != null ? deliveryCarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliveryCar))
        {
            return false;
        }
        DeliveryCar other = (DeliveryCar) object;
        if ((this.deliveryCarPK == null && other.deliveryCarPK != null) || (this.deliveryCarPK != null && !this.deliveryCarPK.equals(other.deliveryCarPK)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.DeliveryCar[ deliveryCarPK=" + deliveryCarPK + " ]";
    }
    
}
