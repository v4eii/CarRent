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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByIdUser", query = "SELECT u FROM Users u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "Users.findByUserLogin", query = "SELECT u FROM Users u WHERE u.userLogin = :userLogin"),
    @NamedQuery(name = "Users.findByUserPassword", query = "SELECT u FROM Users u WHERE u.userPassword = :userPassword"),
    @NamedQuery(name = "Users.findByRole", query = "SELECT u FROM Users u WHERE u.role = :role"),
    @NamedQuery(name = "Users.findByDateCreate", query = "SELECT u FROM Users u WHERE u.dateCreate = :dateCreate"),
    @NamedQuery(name = "Users.findByDateDelete", query = "SELECT u FROM Users u WHERE u.dateDelete = :dateDelete")
})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user")
    private Integer idUser;
    @Basic(optional = false)
    @Column(name = "user_login")
    private String userLogin;
    @Basic(optional = false)
    @Column(name = "user_password")
    private String userPassword;
    @Basic(optional = false)
    private String role;
    @Basic(optional = false)
    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date dateCreate;
    @Column(name = "date_delete")
    @Temporal(TemporalType.DATE)
    private Date dateDelete;
    
    @Transient
    public DBBean.RECORD_STATE rec_state = DBBean.RECORD_STATE.SAVED;

    public Users()
    {
    }

    public Users(Integer idUser)
    {
        this.idUser = idUser;
    }

    public Users(Integer idUser, String userLogin, String userPassword, String role, Date dateCreate)
    {
        this.idUser = idUser;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.role = role;
        this.dateCreate = dateCreate;
    }

    public Integer getIdUser()
    {
        return idUser;
    }

    public void setIdUser(Integer idUser)
    {
        this.idUser = idUser;
    }

    public String getUserLogin()
    {
        return userLogin;
    }

    public void setUserLogin(String userLogin)
    {
        this.userLogin = userLogin;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public Date getDateCreate()
    {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate)
    {
        this.dateCreate = dateCreate;
    }

    public Date getDateDelete()
    {
        return dateDelete;
    }

    public void setDateDelete(Date dateDelete)
    {
        this.dateDelete = dateDelete;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users))
        {
            return false;
        }
        Users other = (Users) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "carrent.entity.Users[ idUser=" + idUser + " ]";
    }
    
}
