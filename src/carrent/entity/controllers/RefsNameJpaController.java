/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import carrent.entity.Refs;
import carrent.entity.RefsName;
import carrent.entity.controllers.exceptions.IllegalOrphanException;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author v4e
 */
public class RefsNameJpaController implements Serializable {

    public RefsNameJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(RefsName refsName)
    {
        if (refsName.getRefsCollection() == null)
        {
            refsName.setRefsCollection(new ArrayList<Refs>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Refs> attachedRefsCollection = new ArrayList<Refs>();
            for (Refs refsCollectionRefsToAttach : refsName.getRefsCollection())
            {
                refsCollectionRefsToAttach = em.getReference(refsCollectionRefsToAttach.getClass(), refsCollectionRefsToAttach.getRefsPK());
                attachedRefsCollection.add(refsCollectionRefsToAttach);
            }
            refsName.setRefsCollection(attachedRefsCollection);
            em.persist(refsName);
            for (Refs refsCollectionRefs : refsName.getRefsCollection())
            {
                RefsName oldRefsNameOfRefsCollectionRefs = refsCollectionRefs.getRefsName();
                refsCollectionRefs.setRefsName(refsName);
                refsCollectionRefs = em.merge(refsCollectionRefs);
                if (oldRefsNameOfRefsCollectionRefs != null)
                {
                    oldRefsNameOfRefsCollectionRefs.getRefsCollection().remove(refsCollectionRefs);
                    oldRefsNameOfRefsCollectionRefs = em.merge(oldRefsNameOfRefsCollectionRefs);
                }
            }
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

    public void edit(RefsName refsName) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            RefsName persistentRefsName = em.find(RefsName.class, refsName.getIdRefName());
            Collection<Refs> refsCollectionOld = persistentRefsName.getRefsCollection();
            Collection<Refs> refsCollectionNew = refsName.getRefsCollection();
            List<String> illegalOrphanMessages = null;
            for (Refs refsCollectionOldRefs : refsCollectionOld)
            {
                if (!refsCollectionNew.contains(refsCollectionOldRefs))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Refs " + refsCollectionOldRefs + " since its refsName field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Refs> attachedRefsCollectionNew = new ArrayList<Refs>();
            for (Refs refsCollectionNewRefsToAttach : refsCollectionNew)
            {
                refsCollectionNewRefsToAttach = em.getReference(refsCollectionNewRefsToAttach.getClass(), refsCollectionNewRefsToAttach.getRefsPK());
                attachedRefsCollectionNew.add(refsCollectionNewRefsToAttach);
            }
            refsCollectionNew = attachedRefsCollectionNew;
            refsName.setRefsCollection(refsCollectionNew);
            refsName = em.merge(refsName);
            for (Refs refsCollectionNewRefs : refsCollectionNew)
            {
                if (!refsCollectionOld.contains(refsCollectionNewRefs))
                {
                    RefsName oldRefsNameOfRefsCollectionNewRefs = refsCollectionNewRefs.getRefsName();
                    refsCollectionNewRefs.setRefsName(refsName);
                    refsCollectionNewRefs = em.merge(refsCollectionNewRefs);
                    if (oldRefsNameOfRefsCollectionNewRefs != null && !oldRefsNameOfRefsCollectionNewRefs.equals(refsName))
                    {
                        oldRefsNameOfRefsCollectionNewRefs.getRefsCollection().remove(refsCollectionNewRefs);
                        oldRefsNameOfRefsCollectionNewRefs = em.merge(oldRefsNameOfRefsCollectionNewRefs);
                    }
                }
            }
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = refsName.getIdRefName();
                if (findRefsName(id) == null)
                {
                    throw new NonexistentEntityException("The refsName with id " + id + " no longer exists.");
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            RefsName refsName;
            try
            {
                refsName = em.getReference(RefsName.class, id);
                refsName.getIdRefName();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The refsName with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Refs> refsCollectionOrphanCheck = refsName.getRefsCollection();
            for (Refs refsCollectionOrphanCheckRefs : refsCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RefsName (" + refsName + ") cannot be destroyed since the Refs " + refsCollectionOrphanCheckRefs + " in its refsCollection field has a non-nullable refsName field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(refsName);
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

    public List<RefsName> findRefsNameEntities()
    {
        return findRefsNameEntities(true, -1, -1);
    }

    public List<RefsName> findRefsNameEntities(int maxResults, int firstResult)
    {
        return findRefsNameEntities(false, maxResults, firstResult);
    }

    private List<RefsName> findRefsNameEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RefsName.class));
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

    public RefsName findRefsName(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(RefsName.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getRefsNameCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RefsName> rt = cq.from(RefsName.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
