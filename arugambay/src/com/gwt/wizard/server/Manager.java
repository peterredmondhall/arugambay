package com.gwt.wizard.server;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.ArugamEntity;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ContractorInfo;
import com.gwt.wizard.shared.model.Info;
import com.thoughtworks.xstream.XStream;

public class Manager<T extends Info, K extends ArugamEntity<?>>
{

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public void deleteAll(Class<?> entityType)
    {
        EntityManager em = getEntityManager();
        @SuppressWarnings("unchecked")
        List<K> resultList = em.createQuery("select t from " + entityType.getName() + " t").getResultList();
        for (K entity : resultList)
        {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        em.close();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAllInfo(Class<?> entityType)
    {
        EntityManager em = getEntityManager();
        List<K> resultList = em.createQuery("select t from " + entityType.getName() + " t").getResultList();
        List<T> list = Lists.newArrayList();
        for (K entity : resultList)
        {
            list.add((T) entity.getInfo());
        }
        em.close();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<K> getAll(Class<?> entityType)
    {
        EntityManager em = getEntityManager();
        List<K> resultList = em.createQuery("select t from " + entityType.getName() + " t").getResultList();
        em.close();
        return resultList;
    }

    public void importDataset(String dataset, Class<?> type)
    {
        deleteAll(type);
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) new XStream().fromXML(dataset);
        for (T info : list)
        {
            if (type.equals(Booking.class))
            {
                Booking entity = Booking.getBooking((BookingInfo) info, null);
                entity.setKey(KeyFactory.createKey(type.getSimpleName(), info.getId()));
                save(entity);
            }
            if (type.equals(Contractor.class))
            {
                Contractor entity = Contractor.getContractor((ContractorInfo) info);
                entity.setKey(KeyFactory.createKey(type.getSimpleName(), info.getId()));
                save(entity);
            }
        }
    }

    public void save(ArugamEntity<?> entity)
    {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();

    }

    public String dump(Class<?> entityType)
    {
        return new XStream().toXML(getAllInfo(entityType));
    }

}
