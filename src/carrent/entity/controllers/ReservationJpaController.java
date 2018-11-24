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
import carrent.entity.Cars;
import carrent.entity.Clients;
import carrent.entity.DeliveryCar;
import carrent.entity.Reservation;
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
public class ReservationJpaController implements Serializable {

    public ReservationJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Reservation reservation)
    {
        if (reservation.getDeliveryCarCollection() == null)
        {
            reservation.setDeliveryCarCollection(new ArrayList<DeliveryCar>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Cars idCar = reservation.getIdCar();
            if (idCar != null)
            {
                idCar = em.getReference(idCar.getClass(), idCar.getIdCar());
                reservation.setIdCar(idCar);
            }
            Clients idClient = reservation.getIdClient();
            if (idClient != null)
            {
                idClient = em.getReference(idClient.getClass(), idClient.getIdClient());
                reservation.setIdClient(idClient);
            }
            Collection<DeliveryCar> attachedDeliveryCarCollection = new ArrayList<DeliveryCar>();
            for (DeliveryCar deliveryCarCollectionDeliveryCarToAttach : reservation.getDeliveryCarCollection())
            {
                deliveryCarCollectionDeliveryCarToAttach = em.getReference(deliveryCarCollectionDeliveryCarToAttach.getClass(), deliveryCarCollectionDeliveryCarToAttach.getDeliveryCarPK());
                attachedDeliveryCarCollection.add(deliveryCarCollectionDeliveryCarToAttach);
            }
            reservation.setDeliveryCarCollection(attachedDeliveryCarCollection);
            em.persist(reservation);
            if (idCar != null)
            {
                idCar.getReservationCollection().add(reservation);
                idCar = em.merge(idCar);
            }
            if (idClient != null)
            {
                idClient.getReservationCollection().add(reservation);
                idClient = em.merge(idClient);
            }
            for (DeliveryCar deliveryCarCollectionDeliveryCar : reservation.getDeliveryCarCollection())
            {
                Reservation oldReservationOfDeliveryCarCollectionDeliveryCar = deliveryCarCollectionDeliveryCar.getReservation();
                deliveryCarCollectionDeliveryCar.setReservation(reservation);
                deliveryCarCollectionDeliveryCar = em.merge(deliveryCarCollectionDeliveryCar);
                if (oldReservationOfDeliveryCarCollectionDeliveryCar != null)
                {
                    oldReservationOfDeliveryCarCollectionDeliveryCar.getDeliveryCarCollection().remove(deliveryCarCollectionDeliveryCar);
                    oldReservationOfDeliveryCarCollectionDeliveryCar = em.merge(oldReservationOfDeliveryCarCollectionDeliveryCar);
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

    public void edit(Reservation reservation) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservation persistentReservation = em.find(Reservation.class, reservation.getIdContract());
            Cars idCarOld = persistentReservation.getIdCar();
            Cars idCarNew = reservation.getIdCar();
            Clients idClientOld = persistentReservation.getIdClient();
            Clients idClientNew = reservation.getIdClient();
            Collection<DeliveryCar> deliveryCarCollectionOld = persistentReservation.getDeliveryCarCollection();
            Collection<DeliveryCar> deliveryCarCollectionNew = reservation.getDeliveryCarCollection();
            List<String> illegalOrphanMessages = null;
            for (DeliveryCar deliveryCarCollectionOldDeliveryCar : deliveryCarCollectionOld)
            {
                if (!deliveryCarCollectionNew.contains(deliveryCarCollectionOldDeliveryCar))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DeliveryCar " + deliveryCarCollectionOldDeliveryCar + " since its reservation field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCarNew != null)
            {
                idCarNew = em.getReference(idCarNew.getClass(), idCarNew.getIdCar());
                reservation.setIdCar(idCarNew);
            }
            if (idClientNew != null)
            {
                idClientNew = em.getReference(idClientNew.getClass(), idClientNew.getIdClient());
                reservation.setIdClient(idClientNew);
            }
            Collection<DeliveryCar> attachedDeliveryCarCollectionNew = new ArrayList<DeliveryCar>();
            for (DeliveryCar deliveryCarCollectionNewDeliveryCarToAttach : deliveryCarCollectionNew)
            {
                deliveryCarCollectionNewDeliveryCarToAttach = em.getReference(deliveryCarCollectionNewDeliveryCarToAttach.getClass(), deliveryCarCollectionNewDeliveryCarToAttach.getDeliveryCarPK());
                attachedDeliveryCarCollectionNew.add(deliveryCarCollectionNewDeliveryCarToAttach);
            }
            deliveryCarCollectionNew = attachedDeliveryCarCollectionNew;
            reservation.setDeliveryCarCollection(deliveryCarCollectionNew);
            reservation = em.merge(reservation);
            if (idCarOld != null && !idCarOld.equals(idCarNew))
            {
                idCarOld.getReservationCollection().remove(reservation);
                idCarOld = em.merge(idCarOld);
            }
            if (idCarNew != null && !idCarNew.equals(idCarOld))
            {
                idCarNew.getReservationCollection().add(reservation);
                idCarNew = em.merge(idCarNew);
            }
            if (idClientOld != null && !idClientOld.equals(idClientNew))
            {
                idClientOld.getReservationCollection().remove(reservation);
                idClientOld = em.merge(idClientOld);
            }
            if (idClientNew != null && !idClientNew.equals(idClientOld))
            {
                idClientNew.getReservationCollection().add(reservation);
                idClientNew = em.merge(idClientNew);
            }
            for (DeliveryCar deliveryCarCollectionNewDeliveryCar : deliveryCarCollectionNew)
            {
                if (!deliveryCarCollectionOld.contains(deliveryCarCollectionNewDeliveryCar))
                {
                    Reservation oldReservationOfDeliveryCarCollectionNewDeliveryCar = deliveryCarCollectionNewDeliveryCar.getReservation();
                    deliveryCarCollectionNewDeliveryCar.setReservation(reservation);
                    deliveryCarCollectionNewDeliveryCar = em.merge(deliveryCarCollectionNewDeliveryCar);
                    if (oldReservationOfDeliveryCarCollectionNewDeliveryCar != null && !oldReservationOfDeliveryCarCollectionNewDeliveryCar.equals(reservation))
                    {
                        oldReservationOfDeliveryCarCollectionNewDeliveryCar.getDeliveryCarCollection().remove(deliveryCarCollectionNewDeliveryCar);
                        oldReservationOfDeliveryCarCollectionNewDeliveryCar = em.merge(oldReservationOfDeliveryCarCollectionNewDeliveryCar);
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
                Integer id = reservation.getIdContract();
                if (findReservation(id) == null)
                {
                    throw new NonexistentEntityException("The reservation with id " + id + " no longer exists.");
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
            Reservation reservation;
            try
            {
                reservation = em.getReference(Reservation.class, id);
                reservation.getIdContract();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The reservation with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DeliveryCar> deliveryCarCollectionOrphanCheck = reservation.getDeliveryCarCollection();
            for (DeliveryCar deliveryCarCollectionOrphanCheckDeliveryCar : deliveryCarCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reservation (" + reservation + ") cannot be destroyed since the DeliveryCar " + deliveryCarCollectionOrphanCheckDeliveryCar + " in its deliveryCarCollection field has a non-nullable reservation field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cars idCar = reservation.getIdCar();
            if (idCar != null)
            {
                idCar.getReservationCollection().remove(reservation);
                idCar = em.merge(idCar);
            }
            Clients idClient = reservation.getIdClient();
            if (idClient != null)
            {
                idClient.getReservationCollection().remove(reservation);
                idClient = em.merge(idClient);
            }
            em.remove(reservation);
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

    public List<Reservation> findReservationEntities()
    {
        return findReservationEntities(true, -1, -1);
    }

    public List<Reservation> findReservationEntities(int maxResults, int firstResult)
    {
        return findReservationEntities(false, maxResults, firstResult);
    }

    private List<Reservation> findReservationEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservation.class));
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

    public Reservation findReservation(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Reservation.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getReservationCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservation> rt = cq.from(Reservation.class);
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
