/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.entity.controllers;

import carrent.entity.CarReport;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import carrent.entity.Clients;
import carrent.entity.Reservation;
import carrent.entity.Cars;
import carrent.entity.DeliveryCar;
import carrent.entity.DeliveryCarPK;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import carrent.entity.controllers.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author v4e
 */
public class DeliveryCarJpaController implements Serializable {

    public DeliveryCarJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(DeliveryCar deliveryCar) throws PreexistingEntityException, Exception
    {
        if (deliveryCar.getDeliveryCarPK() == null)
        {
            deliveryCar.setDeliveryCarPK(new DeliveryCarPK());
        }
        deliveryCar.getDeliveryCarPK().setIdContract(deliveryCar.getReservation().getIdContract());
        deliveryCar.getDeliveryCarPK().setIdCar(deliveryCar.getCars().getIdCar());
        deliveryCar.getDeliveryCarPK().setIdClient(deliveryCar.getClients().getIdClient());
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Clients clients = deliveryCar.getClients();
            if (clients != null)
            {
                clients = em.getReference(clients.getClass(), clients.getIdClient());
                deliveryCar.setClients(clients);
            }
            Reservation reservation = deliveryCar.getReservation();
            if (reservation != null)
            {
                reservation = em.getReference(reservation.getClass(), reservation.getIdContract());
                deliveryCar.setReservation(reservation);
            }
            Cars cars = deliveryCar.getCars();
            if (cars != null)
            {
                cars = em.getReference(cars.getClass(), cars.getIdCar());
                deliveryCar.setCars(cars);
            }
            em.persist(deliveryCar);
            if (clients != null)
            {
                clients.getDeliveryCarCollection().add(deliveryCar);
                clients = em.merge(clients);
            }
            if (reservation != null)
            {
                reservation.getDeliveryCarCollection().add(deliveryCar);
                reservation = em.merge(reservation);
            }
            if (cars != null)
            {
                cars.getDeliveryCarCollection().add(deliveryCar);
                cars = em.merge(cars);
            }
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            if (findDeliveryCar(deliveryCar.getDeliveryCarPK()) != null)
            {
                throw new PreexistingEntityException("DeliveryCar " + deliveryCar + " already exists.", ex);
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

    public void edit(DeliveryCar deliveryCar) throws NonexistentEntityException, Exception
    {
        deliveryCar.getDeliveryCarPK().setIdContract(deliveryCar.getReservation().getIdContract());
        deliveryCar.getDeliveryCarPK().setIdCar(deliveryCar.getCars().getIdCar());
        deliveryCar.getDeliveryCarPK().setIdClient(deliveryCar.getClients().getIdClient());
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            DeliveryCar persistentDeliveryCar = em.find(DeliveryCar.class, deliveryCar.getDeliveryCarPK());
            Clients clientsOld = persistentDeliveryCar.getClients();
            Clients clientsNew = deliveryCar.getClients();
            Reservation reservationOld = persistentDeliveryCar.getReservation();
            Reservation reservationNew = deliveryCar.getReservation();
            Cars carsOld = persistentDeliveryCar.getCars();
            Cars carsNew = deliveryCar.getCars();
            if (clientsNew != null)
            {
                clientsNew = em.getReference(clientsNew.getClass(), clientsNew.getIdClient());
                deliveryCar.setClients(clientsNew);
            }
            if (reservationNew != null)
            {
                reservationNew = em.getReference(reservationNew.getClass(), reservationNew.getIdContract());
                deliveryCar.setReservation(reservationNew);
            }
            if (carsNew != null)
            {
                carsNew = em.getReference(carsNew.getClass(), carsNew.getIdCar());
                deliveryCar.setCars(carsNew);
            }
            deliveryCar = em.merge(deliveryCar);
            if (clientsOld != null && !clientsOld.equals(clientsNew))
            {
                clientsOld.getDeliveryCarCollection().remove(deliveryCar);
                clientsOld = em.merge(clientsOld);
            }
            if (clientsNew != null && !clientsNew.equals(clientsOld))
            {
                clientsNew.getDeliveryCarCollection().add(deliveryCar);
                clientsNew = em.merge(clientsNew);
            }
            if (reservationOld != null && !reservationOld.equals(reservationNew))
            {
                reservationOld.getDeliveryCarCollection().remove(deliveryCar);
                reservationOld = em.merge(reservationOld);
            }
            if (reservationNew != null && !reservationNew.equals(reservationOld))
            {
                reservationNew.getDeliveryCarCollection().add(deliveryCar);
                reservationNew = em.merge(reservationNew);
            }
            if (carsOld != null && !carsOld.equals(carsNew))
            {
                carsOld.getDeliveryCarCollection().remove(deliveryCar);
                carsOld = em.merge(carsOld);
            }
            if (carsNew != null && !carsNew.equals(carsOld))
            {
                carsNew.getDeliveryCarCollection().add(deliveryCar);
                carsNew = em.merge(carsNew);
            }
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                DeliveryCarPK id = deliveryCar.getDeliveryCarPK();
                if (findDeliveryCar(id) == null)
                {
                    throw new NonexistentEntityException("The deliveryCar with id " + id + " no longer exists.");
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

    public void destroy(DeliveryCarPK id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            DeliveryCar deliveryCar;
            try
            {
                deliveryCar = em.getReference(DeliveryCar.class, id);
                deliveryCar.getDeliveryCarPK();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The deliveryCar with id " + id + " no longer exists.", enfe);
            }
            Clients clients = deliveryCar.getClients();
            if (clients != null)
            {
                clients.getDeliveryCarCollection().remove(deliveryCar);
                clients = em.merge(clients);
            }
            Reservation reservation = deliveryCar.getReservation();
            if (reservation != null)
            {
                reservation.getDeliveryCarCollection().remove(deliveryCar);
                reservation = em.merge(reservation);
            }
            Cars cars = deliveryCar.getCars();
            if (cars != null)
            {
                cars.getDeliveryCarCollection().remove(deliveryCar);
                cars = em.merge(cars);
            }
            em.remove(deliveryCar);
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

    public List<DeliveryCar> findDeliveryCarEntities()
    {
        return findDeliveryCarEntities(true, -1, -1);
    }

    public List<DeliveryCar> findDeliveryCarEntities(int maxResults, int firstResult)
    {
        return findDeliveryCarEntities(false, maxResults, firstResult);
    }

    private List<DeliveryCar> findDeliveryCarEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DeliveryCar.class));
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

    public DeliveryCar findDeliveryCar(DeliveryCarPK id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(DeliveryCar.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getDeliveryCarCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DeliveryCar> rt = cq.from(DeliveryCar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
    public List<CarReport> getReportCarCount(String dateAfter, String dateBefore)
    {
        EntityManager em = getEntityManager();
        try
        {
            
            //TODO Разобраться с листом
            String tmp = String.format("SELECT (SELECT last_name FROM clients WHERE delivery_car.id_client = clients.id_client) AS client_name2, \n" +
                    "(SELECT first_name FROM clients WHERE delivery_car.id_client = clients.id_client) AS client_name, \n" +
                    "(SELECT middle_name FROM clients WHERE delivery_car.id_client = clients.id_client) AS client_name3, COUNT(*) AS Reserv_in_month,\n" +
                    "(SELECT ref_name FROM refs WHERE id_ref =\n" +
                    "(SELECT id_make FROM cars WHERE cars.id_car = delivery_car.id_car)) AS Car_make,\n" +
                    "(SELECT ref_name FROM refs WHERE id_ref =\n" +
                    "(SELECT id_model FROM cars WHERE cars.id_car = delivery_car.id_car)) AS Car_model,\n" +
                    "(SELECT VIN FROM cars WHERE cars.id_car = delivery_car.id_car) AS VIN_Car\n" +
                    "FROM delivery_car\n" +
                    "WHERE date_delivery\n" +
                    "BETWEEN \"%s\" AND \"%s\"\n" +
                    "GROUP BY id_car;", dateAfter, dateBefore);
            
            Query q = em.createNativeQuery(tmp);
            
            return q.getResultList();
            
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
        finally
        {
            em.close();
        }
        /*
        SELECT (SELECT first_name FROM clients WHERE delivery_car.id_client = clients.id_client) AS client_name, 
        (SELECT last_name FROM clients WHERE delivery_car.id_client = clients.id_client) AS client_name2, 
        (SELECT middle_name FROM clients WHERE delivery_car.id_client = clients.id_client) AS client_name3, COUNT(*) AS Reserv_in_month,
        (SELECT ref_name FROM refs WHERE id_ref =
        (SELECT id_make FROM cars WHERE cars.id_car = delivery_car.id_car)) AS Car_make,
        (SELECT ref_name FROM refs WHERE id_ref =
        (SELECT id_model FROM cars WHERE cars.id_car = delivery_car.id_car)) AS Car_model,
        (SELECT VIN FROM cars WHERE cars.id_car = delivery_car.id_car) AS VIN_Car
        FROM delivery_car
        WHERE date_delivery
        BETWEEN "2018.11.12" AND "2019.01.12"
        GROUP BY id_car;
         */
    }
    
}
