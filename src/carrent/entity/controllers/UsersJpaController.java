/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity.controllers;

import carrent.beans.DBBean;
import carrent.entity.Users;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author v4e
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Users users)
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(users);
            em.getTransaction().commit();
        }
        finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            users = em.merge(users);
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = users.getIdUser();
                if (findUsers(id) == null)
                {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
        finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try
            {
                users = em.getReference(Users.class, id);
                users.getIdUser();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            em.remove(users);
            em.getTransaction().commit();
        }
        finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities()
    {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult)
    {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
            Query q = em.createQuery(cq);
            if (!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    public Users findUsers(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Users.class, id);
        }
        finally
        {
            em.close();
        }
    }
    
    public Users findUsers(String login)
    {
        EntityManager em = getEntityManager();
        try
        {
            Query q = em.createNamedQuery("Users.findByUserLogin", Users.class);
            q.setParameter("userLogin", login);
            return q.getFirstResult() == -1 ? null : (Users) q.getSingleResult();
        }
        finally
        {
            em.close();
        }
    }

    public int getUsersCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
    public String getUserRole(String login)
    {
        EntityManager em = getEntityManager();
        try
        {
            Query q = em.createNativeQuery("SELECT role FROM users WHERE user_login = ".concat("\"" + DBBean.getInstance().getUser() + "\""));
            return (String) q.getSingleResult();
        }
        finally
        {
            em.close();
        }
    }
    
    public String getUserPassword(String login)
    {
        EntityManager em = getEntityManager();
        try
        {
            Query q = em.createNativeQuery("SELECT user_password FROM users WHERE user_login = ".concat("\"" + DBBean.getInstance().getUser() + "\""));
            System.out.println(q.toString());
            return (String) q.getSingleResult();
        }
        finally
        {
            em.close();
        }
    }
    
    public Date getUserDateDelete(String login)
    {
        EntityManager em = getEntityManager();
        try
        {
            Query q = em.createNativeQuery("SELECT date_delete FROM users WHERE user_login = ".concat("\"" + DBBean.getInstance().getUser() + "\""));
            return (Date) q.getSingleResult();
        }
        finally
        {
            em.close();
        }
    }
    
}
