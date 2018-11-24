/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity.controllers;

import carrent.entity.Cars;
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
public class CarsJpaController implements Serializable {

    public CarsJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Cars cars)
    {
        if (cars.getDeliveryCarCollection() == null)
        {
            cars.setDeliveryCarCollection(new ArrayList<DeliveryCar>());
        }
        if (cars.getReservationCollection() == null)
        {
            cars.setReservationCollection(new ArrayList<Reservation>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DeliveryCar> attachedDeliveryCarCollection = new ArrayList<DeliveryCar>();
            for (DeliveryCar deliveryCarCollectionDeliveryCarToAttach : cars.getDeliveryCarCollection())
            {
                deliveryCarCollectionDeliveryCarToAttach = em.getReference(deliveryCarCollectionDeliveryCarToAttach.getClass(), deliveryCarCollectionDeliveryCarToAttach.getDeliveryCarPK());
                attachedDeliveryCarCollection.add(deliveryCarCollectionDeliveryCarToAttach);
            }
            cars.setDeliveryCarCollection(attachedDeliveryCarCollection);
            Collection<Reservation> attachedReservationCollection = new ArrayList<Reservation>();
            for (Reservation reservationCollectionReservationToAttach : cars.getReservationCollection())
            {
                reservationCollectionReservationToAttach = em.getReference(reservationCollectionReservationToAttach.getClass(), reservationCollectionReservationToAttach.getIdContract());
                attachedReservationCollection.add(reservationCollectionReservationToAttach);
            }
            cars.setReservationCollection(attachedReservationCollection);
            em.persist(cars);
            for (DeliveryCar deliveryCarCollectionDeliveryCar : cars.getDeliveryCarCollection())
            {
                Cars oldCarsOfDeliveryCarCollectionDeliveryCar = deliveryCarCollectionDeliveryCar.getCars();
                deliveryCarCollectionDeliveryCar.setCars(cars);
                deliveryCarCollectionDeliveryCar = em.merge(deliveryCarCollectionDeliveryCar);
                if (oldCarsOfDeliveryCarCollectionDeliveryCar != null)
                {
                    oldCarsOfDeliveryCarCollectionDeliveryCar.getDeliveryCarCollection().remove(deliveryCarCollectionDeliveryCar);
                    oldCarsOfDeliveryCarCollectionDeliveryCar = em.merge(oldCarsOfDeliveryCarCollectionDeliveryCar);
                }
            }
            for (Reservation reservationCollectionReservation : cars.getReservationCollection())
            {
                Cars oldIdCarOfReservationCollectionReservation = reservationCollectionReservation.getIdCar();
                reservationCollectionReservation.setIdCar(cars);
                reservationCollectionReservation = em.merge(reservationCollectionReservation);
                if (oldIdCarOfReservationCollectionReservation != null)
                {
                    oldIdCarOfReservationCollectionReservation.getReservationCollection().remove(reservationCollectionReservation);
                    oldIdCarOfReservationCollectionReservation = em.merge(oldIdCarOfReservationCollectionReservation);
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

    public void edit(Cars cars) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Cars persistentCars = em.find(Cars.class, cars.getIdCar());
            Collection<DeliveryCar> deliveryCarCollectionOld = persistentCars.getDeliveryCarCollection();
            Collection<DeliveryCar> deliveryCarCollectionNew = cars.getDeliveryCarCollection();
            Collection<Reservation> reservationCollectionOld = persistentCars.getReservationCollection();
            Collection<Reservation> reservationCollectionNew = cars.getReservationCollection();
            List<String> illegalOrphanMessages = null;
            for (DeliveryCar deliveryCarCollectionOldDeliveryCar : deliveryCarCollectionOld)
            {
                if (!deliveryCarCollectionNew.contains(deliveryCarCollectionOldDeliveryCar))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DeliveryCar " + deliveryCarCollectionOldDeliveryCar + " since its cars field is not nullable.");
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
                    illegalOrphanMessages.add("You must retain Reservation " + reservationCollectionOldReservation + " since its idCar field is not nullable.");
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
            cars.setDeliveryCarCollection(deliveryCarCollectionNew);
            Collection<Reservation> attachedReservationCollectionNew = new ArrayList<Reservation>();
            for (Reservation reservationCollectionNewReservationToAttach : reservationCollectionNew)
            {
                reservationCollectionNewReservationToAttach = em.getReference(reservationCollectionNewReservationToAttach.getClass(), reservationCollectionNewReservationToAttach.getIdContract());
                attachedReservationCollectionNew.add(reservationCollectionNewReservationToAttach);
            }
            reservationCollectionNew = attachedReservationCollectionNew;
            cars.setReservationCollection(reservationCollectionNew);
            cars = em.merge(cars);
            for (DeliveryCar deliveryCarCollectionNewDeliveryCar : deliveryCarCollectionNew)
            {
                if (!deliveryCarCollectionOld.contains(deliveryCarCollectionNewDeliveryCar))
                {
                    Cars oldCarsOfDeliveryCarCollectionNewDeliveryCar = deliveryCarCollectionNewDeliveryCar.getCars();
                    deliveryCarCollectionNewDeliveryCar.setCars(cars);
                    deliveryCarCollectionNewDeliveryCar = em.merge(deliveryCarCollectionNewDeliveryCar);
                    if (oldCarsOfDeliveryCarCollectionNewDeliveryCar != null && !oldCarsOfDeliveryCarCollectionNewDeliveryCar.equals(cars))
                    {
                        oldCarsOfDeliveryCarCollectionNewDeliveryCar.getDeliveryCarCollection().remove(deliveryCarCollectionNewDeliveryCar);
                        oldCarsOfDeliveryCarCollectionNewDeliveryCar = em.merge(oldCarsOfDeliveryCarCollectionNewDeliveryCar);
                    }
                }
            }
            for (Reservation reservationCollectionNewReservation : reservationCollectionNew)
            {
                if (!reservationCollectionOld.contains(reservationCollectionNewReservation))
                {
                    Cars oldIdCarOfReservationCollectionNewReservation = reservationCollectionNewReservation.getIdCar();
                    reservationCollectionNewReservation.setIdCar(cars);
                    reservationCollectionNewReservation = em.merge(reservationCollectionNewReservation);
                    if (oldIdCarOfReservationCollectionNewReservation != null && !oldIdCarOfReservationCollectionNewReservation.equals(cars))
                    {
                        oldIdCarOfReservationCollectionNewReservation.getReservationCollection().remove(reservationCollectionNewReservation);
                        oldIdCarOfReservationCollectionNewReservation = em.merge(oldIdCarOfReservationCollectionNewReservation);
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
                Integer id = cars.getIdCar();
                if (findCars(id) == null)
                {
                    throw new NonexistentEntityException("The cars with id " + id + " no longer exists.");
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
            Cars cars;
            try
            {
                cars = em.getReference(Cars.class, id);
                cars.getIdCar();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The cars with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DeliveryCar> deliveryCarCollectionOrphanCheck = cars.getDeliveryCarCollection();
            for (DeliveryCar deliveryCarCollectionOrphanCheckDeliveryCar : deliveryCarCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cars (" + cars + ") cannot be destroyed since the DeliveryCar " + deliveryCarCollectionOrphanCheckDeliveryCar + " in its deliveryCarCollection field has a non-nullable cars field.");
            }
            Collection<Reservation> reservationCollectionOrphanCheck = cars.getReservationCollection();
            for (Reservation reservationCollectionOrphanCheckReservation : reservationCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cars (" + cars + ") cannot be destroyed since the Reservation " + reservationCollectionOrphanCheckReservation + " in its reservationCollection field has a non-nullable idCar field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cars);
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

    public List<Cars> findCarsEntities()
    {
        return findCarsEntities(true, -1, -1);
    }

    public List<Cars> findCarsEntities(int maxResults, int firstResult)
    {
        return findCarsEntities(false, maxResults, firstResult);
    }

    private List<Cars> findCarsEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cars.class));
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

    public Cars findCars(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Cars.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getCarsCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cars> rt = cq.from(Cars.class);
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
