/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import carrent.beans.DBBean;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author v4e
 */
@Entity
@Table(catalog = "car_rent", schema = "")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Refs.findAll", query = "SELECT r FROM Refs r"),
    @NamedQuery(name = "Refs.findByIdRefName", query = "SELECT r FROM Refs r WHERE r.refsPK.idRefName = :idRefName"),
    @NamedQuery(name = "Refs.findByIdRef", query = "SELECT r FROM Refs r WHERE r.refsPK.idRef = :idRef"),
    @NamedQuery(name = "Refs.findByRefName", query = "SELECT r FROM Refs r WHERE r.refName = :refName"),
    @NamedQuery(name = "Refs.findByDateCreate", query = "SELECT r FROM Refs r WHERE r.dateCreate = :dateCreate"),
    @NamedQuery(name = "Refs.findByDateCancel", query = "SELECT r FROM Refs r WHERE r.dateCancel = :dateCancel")
})
public class Refs implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RefsPK refsPK;
    @Basic(optional = false)
    @Column(name = "ref_name")
    private String refName;
    @Basic(optional = false)
    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date dateCreate;
    @Column(name = "date_cancel")
    @Temporal(TemporalType.DATE)
    private Date dateCancel;
    @JoinColumn(name = "id_ref_name", referencedColumnName = "id_ref_name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RefsName refsName;
    
    @Transient
    public DBBean.RECORD_STATE rec_state = DBBean.RECORD_STATE.SAVED;

    public Refs()
    {
    }

    public Refs(RefsPK refsPK)
    {
        this.refsPK = refsPK;
    }

    public Refs(RefsPK refsPK, String refName, Date dateCreate)
    {
        this.refsPK = refsPK;
        this.refName = refName;
        this.dateCreate = dateCreate;
    }

    public Refs(int idRefName, int idRef)
    {
        this.refsPK = new RefsPK(idRefName, idRef);
    }

    public RefsPK getRefsPK()
    {
        return refsPK;
    }

    public void setRefsPK(RefsPK refsPK)
    {
        this.refsPK = refsPK;
    }

    public String getRefName()
    {
        return refName;
    }

    public void setRefName(String refName)
    {
        this.refName = refName;
    }

    public Date getDateCreate()
    {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate)
    {
        this.dateCreate = dateCreate;
    }

    public Date getDateCancel()
    {
        return dateCancel;
    }

    public void setDateCancel(Date dateCancel)
    {
        this.dateCancel = dateCancel;
    }

    public RefsName getRefsName()
    {
        return refsName;
    }

    public void setRefsName(RefsName refsName)
    {
        this.refsName = refsName;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (refsPK != null ? refsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Refs))
        {
            return false;
        }
        Refs other = (Refs) object;
        if ((this.refsPK == null && other.refsPK != null) || (this.refsPK != null && !this.refsPK.equals(other.refsPK)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.Refs[ refsPK=" + refsPK + " ]";
    }
    
}
