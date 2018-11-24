/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author v4e
 */
@Entity
@Table(name = "refs_name", catalog = "car_rent", schema = "")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "RefsName.findAll", query = "SELECT r FROM RefsName r"),
    @NamedQuery(name = "RefsName.findByIdRefName", query = "SELECT r FROM RefsName r WHERE r.idRefName = :idRefName"),
    @NamedQuery(name = "RefsName.findByRefName", query = "SELECT r FROM RefsName r WHERE r.refName = :refName")
})
public class RefsName implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ref_name")
    private Integer idRefName;
    @Basic(optional = false)
    @Column(name = "ref_name")
    private String refName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refsName")
    private Collection<Refs> refsCollection;

    public RefsName()
    {
    }

    public RefsName(Integer idRefName)
    {
        this.idRefName = idRefName;
    }

    public RefsName(Integer idRefName, String refName)
    {
        this.idRefName = idRefName;
        this.refName = refName;
    }

    public Integer getIdRefName()
    {
        return idRefName;
    }

    public void setIdRefName(Integer idRefName)
    {
        this.idRefName = idRefName;
    }

    public String getRefName()
    {
        return refName;
    }

    public void setRefName(String refName)
    {
        this.refName = refName;
    }

    @XmlTransient
    public Collection<Refs> getRefsCollection()
    {
        return refsCollection;
    }

    public void setRefsCollection(Collection<Refs> refsCollection)
    {
        this.refsCollection = refsCollection;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idRefName != null ? idRefName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RefsName))
        {
            return false;
        }
        RefsName other = (RefsName) object;
        if ((this.idRefName == null && other.idRefName != null) || (this.idRefName != null && !this.idRefName.equals(other.idRefName)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.RefsName[ idRefName=" + idRefName + " ]";
    }
    
}
