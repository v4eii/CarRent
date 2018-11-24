/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity.controllers;

import carrent.entity.Clients;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import carrent.entity.DeliveryCar;
import java.util.ArrayList;
import java.util.Collection;
import carrent.entity.Reservation;
import carrent.entity.controllers.exceptions.IllegalOrphanException;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author v4e
 */
public class ClientsJpaController implements Serializable {

    public ClientsJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Clients clients)
    {
        if (clients.getDeliveryCarCollection() == null)
        {
            clients.setDeliveryCarCollection(new ArrayList<DeliveryCar>());
        }
        if (clients.getReservationCollection() == null)
        {
            clients.setReservationCollection(new ArrayList<Reservation>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DeliveryCar> attachedDeliveryCarCollection = new ArrayList<DeliveryCar>();
            for (DeliveryCar deliveryCarCollectionDeliveryCarToAttach : clients.getDeliveryCarCollection())
            {
                deliveryCarCollectionDeliveryCarToAttach = em.getReference(deliveryCarCollectionDeliveryCarToAttach.getClass(), deliveryCarCollectionDeliveryCarToAttach.getDeliveryCarPK());
                attachedDeliveryCarCollection.add(deliveryCarCollectionDeliveryCarToAttach);
            }
            clients.setDeliveryCarCollection(attachedDeliveryCarCollection);
            Collection<Reservation> attachedReservationCollection = new ArrayList<Reservation>();
            for (Reservation reservationCollectionReservationToAttach : clients.getReservationCollection())
            {
                reservationCollectionReservationToAttach = em.getReference(reservationCollectionReservationToAttach.getClass(), reservationCollectionReservationToAttach.getIdContract());
                attachedReservationCollection.add(reservationCollectionReservationToAttach);
            }
            clients.setReservationCollection(attachedReservationCollection);
            em.persist(clients);
            for (DeliveryCar deliveryCarCollectionDeliveryCar : clients.getDeliveryCarCollection())
            {
                Clients oldClientsOfDeliveryCarCollectionDeliveryCar = deliveryCarCollectionDeliveryCar.getClients();
                deliveryCarCollectionDeliveryCar.setClients(clients);
                deliveryCarCollectionDeliveryCar = em.merge(deliveryCarCollectionDeliveryCar);
                if (oldClientsOfDeliveryCarCollectionDeliveryCar != null)
                {
                    oldClientsOfDeliveryCarCollectionDeliveryCar.getDeliveryCarCollection().remove(deliveryCarCollectionDeliveryCar);
                    oldClientsOfDeliveryCarCollectionDeliveryCar = em.merge(oldClientsOfDeliveryCarCollectionDeliveryCar);
                }
            }
            for (Reservation reservationCollectionReservation : clients.getReservationCollection())
            {
                Clients oldIdClientOfReservationCollectionReservation = reservationCollectionReservation.getIdClient();
                reservationCollectionReservation.setIdClient(clients);
                reservationCollectionReservation = em.merge(reservationCollectionReservation);
                if (oldIdClientOfReservationCollectionReservation != null)
                {
                    oldIdClientOfReservationCollectionReservation.getReservationCollection().remove(reservationCollectionReservation);
                    oldIdClientOfReservationCollectionReservation = em.merge(oldIdClientOfReservationCollectionReservation);
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

    public void edit(Clients clients) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Clients persistentClients = em.find(Clients.class, clients.getIdClient());
            Collection<DeliveryCar> deliveryCarCollectionOld = persistentClients.getDeliveryCarCollection();
            Collection<DeliveryCar> deliveryCarCollectionNew = clients.getDeliveryCarCollection();
            Collection<Reservation> reservationCollectionOld = persistentClients.getReservationCollection();
            Collection<Reservation> reservationCollectionNew = clients.getReservationCollection();
            List<String> illegalOrphanMessages = null;
            for (DeliveryCar deliveryCarCollectionOldDeliveryCar : deliveryCarCollectionOld)
            {
                if (!deliveryCarCollectionNew.contains(deliveryCarCollectionOldDeliveryCar))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DeliveryCar " + deliveryCarCollectionOldDeliveryCar + " since its clients field is not nullable.");
                }
            }
            for (Reservation reservationCollectionOldReservation : reservationCollectionOld)
            {
                if (!reservationCollectionNew.contains(reservationCollectionOldReservation))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservation " + reservationCollectionOldReservation + " since its idClient field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DeliveryCar> attachedDeliveryCarCollectionNew = new ArrayList<DeliveryCar>();
            for (DeliveryCar deliveryCarCollectionNewDeliveryCarToAttach : deliveryCarCollectionNew)
            {
                deliveryCarCollectionNewDeliveryCarToAttach = em.getReference(deliveryCarCollectionNewDeliveryCarToAttach.getClass(), deliveryCarCollectionNewDeliveryCarToAttach.getDeliveryCarPK());
                attachedDeliveryCarCollectionNew.add(deliveryCarCollectionNewDeliveryCarToAttach);
            }
            deliveryCarCollectionNew = attachedDeliveryCarCollectionNew;
            clients.setDeliveryCarCollection(deliveryCarCollectionNew);
            Collection<Reservation> attachedReservationCollectionNew = new ArrayList<Reservation>();
            for (Reservation reservationCollectionNewReservationToAttach : reservationCollectionNew)
            {
                reservationCollectionNewReservationToAttach = em.getReference(reservationCollectionNewReservationToAttach.getClass(), reservationCollectionNewReservationToAttach.getIdContract());
                attachedReservationCollectionNew.add(reservationCollectionNewReservationToAttach);
            }
            reservationCollectionNew = attachedReservationCollectionNew;
            clients.setReservationCollection(reservationCollectionNew);
            clients = em.merge(clients);
            for (DeliveryCar deliveryCarCollectionNewDeliveryCar : deliveryCarCollectionNew)
            {
                if (!deliveryCarCollectionOld.contains(deliveryCarCollectionNewDeliveryCar))
                {
                    Clients oldClientsOfDeliveryCarCollectionNewDeliveryCar = deliveryCarCollectionNewDeliveryCar.getClients();
                    deliveryCarCollectionNewDeliveryCar.setClients(clients);
                    deliveryCarCollectionNewDeliveryCar = em.merge(deliveryCarCollectionNewDeliveryCar);
                    if (oldClientsOfDeliveryCarCollectionNewDeliveryCar != null && !oldClientsOfDeliveryCarCollectionNewDeliveryCar.equals(clients))
                    {
                        oldClientsOfDeliveryCarCollectionNewDeliveryCar.getDeliveryCarCollection().remove(deliveryCarCollectionNewDeliveryCar);
                        oldClientsOfDeliveryCarCollectionNewDeliveryCar = em.merge(oldClientsOfDeliveryCarCollectionNewDeliveryCar);
                    }
                }
            }
            for (Reservation reservationCollectionNewReservation : reservationCollectionNew)
            {
                if (!reservationCollectionOld.contains(reservationCollectionNewReservation))
                {
                    Clients oldIdClientOfReservationCollectionNewReservation = reservationCollectionNewReservation.getIdClient();
                    reservationCollectionNewReservation.setIdClient(clients);
                    reservationCollectionNewReservation = em.merge(reservationCollectionNewReservation);
                    if (oldIdClientOfReservationCollectionNewReservation != null && !oldIdClientOfReservationCollectionNewReservation.equals(clients))
                    {
                        oldIdClientOfReservationCollectionNewReservation.getReservationCollection().remove(reservationCollectionNewReservation);
                        oldIdClientOfReservationCollectionNewReservation = em.merge(oldIdClientOfReservationCollectionNewReservation);
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
                Integer id = clients.getIdClient();
                if (findClients(id) == null)
                {
                    throw new NonexistentEntityException("The clients with id " + id + " no longer exists.");
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
            Clients clients;
            try
            {
                clients = em.getReference(Clients.class, id);
                clients.getIdClient();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The clients with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DeliveryCar> deliveryCarCollectionOrphanCheck = clients.getDeliveryCarCollection();
            for (DeliveryCar deliveryCarCollectionOrphanCheckDeliveryCar : deliveryCarCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clients (" + clients + ") cannot be destroyed since the DeliveryCar " + deliveryCarCollectionOrphanCheckDeliveryCar + " in its deliveryCarCollection field has a non-nullable clients field.");
            }
            Collection<Reservation> reservationCollectionOrphanCheck = clients.getReservationCollection();
            for (Reservation reservationCollectionOrphanCheckReservation : reservationCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clients (" + clients + ") cannot be destroyed since the Reservation " + reservationCollectionOrphanCheckReservation + " in its reservationCollection field has a non-nullable idClient field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clients);
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

    public List<Clients> findClientsEntities()
    {
        return findClientsEntities(true, -1, -1);
    }

    public List<Clients> findClientsEntities(int maxResults, int firstResult)
    {
        return findClientsEntities(false, maxResults, firstResult);
    }

    private List<Clients> findClientsEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clients.class));
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

    public Clients findClients(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Clients.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getClientsCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clients> rt = cq.from(Clients.class);
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
