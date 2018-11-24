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
public class DeliveryCarPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_contract")
    private int idContract;
    @Basic(optional = false)
    @Column(name = "id_car")
    private int idCar;
    @Basic(optional = false)
    @Column(name = "id_client")
    private int idClient;

    public DeliveryCarPK()
    {
    }

    public DeliveryCarPK(int idContract, int idCar, int idClient)
    {
        this.idContract = idContract;
        this.idCar = idCar;
        this.idClient = idClient;
    }

    public int getIdContract()
    {
        return idContract;
    }

    public void setIdContract(int idContract)
    {
        this.idContract = idContract;
    }

    public int getIdCar()
    {
        return idCar;
    }

    public void setIdCar(int idCar)
    {
        this.idCar = idCar;
    }

    public int getIdClient()
    {
        return idClient;
    }

    public void setIdClient(int idClient)
    {
        this.idClient = idClient;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (int) idContract;
        hash += (int) idCar;
        hash += (int) idClient;
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliveryCarPK))
        {
            return false;
        }
        DeliveryCarPK other = (DeliveryCarPK) object;
        if (this.idContract != other.idContract)
        {
            return false;
        }
        if (this.idCar != other.idCar)
        {
            return false;
        }
        if (this.idClient != other.idClient)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.DeliveryCarPK[ idContract=" + idContract + ", idCar=" + idCar + ", idClient=" + idClient + " ]";
    }
    
}
