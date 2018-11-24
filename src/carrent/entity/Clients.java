/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import carrent.beans.DBBean;
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
    @NamedQuery(name = "Clients.findAll", query = "SELECT c FROM Clients c"),
    @NamedQuery(name = "Clients.findByIdClient", query = "SELECT c FROM Clients c WHERE c.idClient = :idClient"),
    @NamedQuery(name = "Clients.findByLastName", query = "SELECT c FROM Clients c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "Clients.findByFirstName", query = "SELECT c FROM Clients c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "Clients.findByMiddleName", query = "SELECT c FROM Clients c WHERE c.middleName = :middleName"),
    @NamedQuery(name = "Clients.findByBirhday", query = "SELECT c FROM Clients c WHERE c.birhday = :birhday"),
    @NamedQuery(name = "Clients.findByPhoneNumber", query = "SELECT c FROM Clients c WHERE c.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Clients.findByIdDoc", query = "SELECT c FROM Clients c WHERE c.idDoc = :idDoc"),
    @NamedQuery(name = "Clients.findBySeriesDoc", query = "SELECT c FROM Clients c WHERE c.seriesDoc = :seriesDoc"),
    @NamedQuery(name = "Clients.findByNumberDoc", query = "SELECT c FROM Clients c WHERE c.numberDoc = :numberDoc"),
    @NamedQuery(name = "Clients.findByDateIssueDoc", query = "SELECT c FROM Clients c WHERE c.dateIssueDoc = :dateIssueDoc"),
    @NamedQuery(name = "Clients.findByIssuedByDoc", query = "SELECT c FROM Clients c WHERE c.issuedByDoc = :issuedByDoc"),
    @NamedQuery(name = "Clients.findByRegistrationAdress", query = "SELECT c FROM Clients c WHERE c.registrationAdress = :registrationAdress")
})
public class Clients implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_client")
    private Integer idClient;
    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "middle_name")
    private String middleName;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date birhday;
    @Basic(optional = false)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic(optional = false)
    @Column(name = "id_doc")
    private int idDoc;
    @Basic(optional = false)
    @Column(name = "series_doc")
    private String seriesDoc;
    @Basic(optional = false)
    @Column(name = "number_doc")
    private String numberDoc;
    @Basic(optional = false)
    @Column(name = "date_issue_doc")
    @Temporal(TemporalType.DATE)
    private Date dateIssueDoc;
    @Basic(optional = false)
    @Column(name = "issued_by_doc")
    private String issuedByDoc;
    @Basic(optional = false)
    @Column(name = "registration_adress")
    private String registrationAdress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clients")
    private Collection<DeliveryCar> deliveryCarCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
    private Collection<Reservation> reservationCollection;
    
    @Transient
    public DBBean.RECORD_STATE rec_state = DBBean.RECORD_STATE.SAVED;

    public Clients()
    {
    }

    public Clients(Integer idClient)
    {
        this.idClient = idClient;
    }

    public Clients(Integer idClient, String lastName, String firstName, String middleName, Date birhday, String phoneNumber, int idDoc, String seriesDoc, String numberDoc, Date dateIssueDoc, String issuedByDoc, String registrationAdress)
    {
        this.idClient = idClient;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birhday = birhday;
        this.phoneNumber = phoneNumber;
        this.idDoc = idDoc;
        this.seriesDoc = seriesDoc;
        this.numberDoc = numberDoc;
        this.dateIssueDoc = dateIssueDoc;
        this.issuedByDoc = issuedByDoc;
        this.registrationAdress = registrationAdress;
    }

    public Integer getIdClient()
    {
        return idClient;
    }

    public void setIdClient(Integer idClient)
    {
        this.idClient = idClient;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public Date getBirhday()
    {
        return birhday;
    }

    public void setBirhday(Date birhday)
    {
        this.birhday = birhday;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public int getIdDoc()
    {
        return idDoc;
    }

    public void setIdDoc(int idDoc)
    {
        this.idDoc = idDoc;
    }

    public String getSeriesDoc()
    {
        return seriesDoc;
    }

    public void setSeriesDoc(String seriesDoc)
    {
        this.seriesDoc = seriesDoc;
    }

    public String getNumberDoc()
    {
        return numberDoc;
    }

    public void setNumberDoc(String numberDoc)
    {
        this.numberDoc = numberDoc;
    }

    public Date getDateIssueDoc()
    {
        return dateIssueDoc;
    }

    public void setDateIssueDoc(Date dateIssueDoc)
    {
        this.dateIssueDoc = dateIssueDoc;
    }

    public String getIssuedByDoc()
    {
        return issuedByDoc;
    }

    public void setIssuedByDoc(String issuedByDoc)
    {
        this.issuedByDoc = issuedByDoc;
    }

    public String getRegistrationAdress()
    {
        return registrationAdress;
    }

    public void setRegistrationAdress(String registrationAdress)
    {
        this.registrationAdress = registrationAdress;
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

    @XmlTransient
    public Collection<Reservation> getReservationCollection()
    {
        return reservationCollection;
    }

    public void setReservationCollection(Collection<Reservation> reservationCollection)
    {
        this.reservationCollection = reservationCollection;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idClient != null ? idClient.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clients))
        {
            return false;
        }
        Clients other = (Clients) object;
        if ((this.idClient == null && other.idClient != null) || (this.idClient != null && !this.idClient.equals(other.idClient)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.Clients[ idClient=" + idClient + " ]";
    }
    
}
