/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity.controllers;

import carrent.entity.Refs;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import carrent.entity.RefsName;
import carrent.entity.RefsPK;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import carrent.entity.controllers.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author v4e
 */
public class RefsJpaController implements Serializable {

    public RefsJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Refs refs) throws PreexistingEntityException, Exception
    {
        if (refs.getRefsPK() == null)
        {
            refs.setRefsPK(new RefsPK());
        }
        refs.getRefsPK().setIdRefName(refs.getRefsName().getIdRefName());
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            RefsName refsName = refs.getRefsName();
            if (refsName != null)
            {
                refsName = em.getReference(refsName.getClass(), refsName.getIdRefName());
                refs.setRefsName(refsName);
            }
            em.persist(refs);
            if (refsName != null)
            {
                refsName.getRefsCollection().add(refs);
                refsName = em.merge(refsName);
            }
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            if (findRefs(refs.getRefsPK()) != null)
            {
                throw new PreexistingEntityException("Refs " + refs + " already exists.", ex);
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

    public void edit(Refs refs) throws NonexistentEntityException, Exception
    {
        refs.getRefsPK().setIdRefName(refs.getRefsName().getIdRefName());
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Refs persistentRefs = em.find(Refs.class, refs.getRefsPK());
            RefsName refsNameOld = persistentRefs.getRefsName();
            RefsName refsNameNew = refs.getRefsName();
            if (refsNameNew != null)
            {
                refsNameNew = em.getReference(refsNameNew.getClass(), refsNameNew.getIdRefName());
                refs.setRefsName(refsNameNew);
            }
            refs = em.merge(refs);
            if (refsNameOld != null && !refsNameOld.equals(refsNameNew))
            {
                refsNameOld.getRefsCollection().remove(refs);
                refsNameOld = em.merge(refsNameOld);
            }
            if (refsNameNew != null && !refsNameNew.equals(refsNameOld))
            {
                refsNameNew.getRefsCollection().add(refs);
                refsNameNew = em.merge(refsNameNew);
            }
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                RefsPK id = refs.getRefsPK();
                if (findRefs(id) == null)
                {
                    throw new NonexistentEntityException("The refs with id " + id + " no longer exists.");
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

    public void destroy(RefsPK id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Refs refs;
            try
            {
                refs = em.getReference(Refs.class, id);
                refs.getRefsPK();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The refs with id " + id + " no longer exists.", enfe);
            }
            RefsName refsName = refs.getRefsName();
            if (refsName != null)
            {
                refsName.getRefsCollection().remove(refs);
                refsName = em.merge(refsName);
            }
            em.remove(refs);
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

    public List<Refs> findRefsEntities()
    {
        return findRefsEntities(true, -1, -1);
    }

    public List<Refs> findRefsEntities(int maxResults, int firstResult)
    {
        return findRefsEntities(false, maxResults, firstResult);
    }

    private List<Refs> findRefsEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Refs.class));
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

    public Refs findRefs(RefsPK id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Refs.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getRefsCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Refs> rt = cq.from(Refs.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
    public List<Refs> getRefsByIdRefsName(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            Query q = em.createNamedQuery("Refs.findByIdRefName");
            q.setParameter("idRefName", id);
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }
    
}
