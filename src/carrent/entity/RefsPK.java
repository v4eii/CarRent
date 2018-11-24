/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author v4e
 */
@Embeddable
public class RefsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_ref_name")
    private int idRefName;
    @Basic(optional = false)
    @Column(name = "id_ref")
    private int idRef;

    public RefsPK()
    {
    }

    public RefsPK(int idRefName, int idRef)
    {
        this.idRefName = idRefName;
        this.idRef = idRef;
    }

    public int getIdRefName()
    {
        return idRefName;
    }

    public void setIdRefName(int idRefName)
    {
        this.idRefName = idRefName;
    }

    public int getIdRef()
    {
        return idRef;
    }

    public void setIdRef(int idRef)
    {
        this.idRef = idRef;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (int) idRefName;
        hash += (int) idRef;
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RefsPK))
        {
            return false;
        }
        RefsPK other = (RefsPK) object;
        if (this.idRefName != other.idRefName)
        {
            return false;
        }
        if (this.idRef != other.idRef)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.RefsPK[ idRefName=" + idRefName + ", idRef=" + idRef + " ]";
    }
    
}
