package com.gwt.wizard.server;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Agent;
import com.gwt.wizard.server.entity.ArugamEntity;
import com.gwt.wizard.server.entity.ArugamImage;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ArugamImageInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ContractorInfo;
import com.gwt.wizard.shared.model.Info;
import com.gwt.wizard.shared.model.RouteInfo;
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

        String[] datasets = dataset.split("<list>");
        for (String ds : datasets)
        {
            if (ds.contains(type.getSimpleName() + "Info"))
            {
                dataset = "<list>" + ds;
                break;
            }
        }

        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) new XStream().fromXML(dataset);
        for (T info : list)
        {
            if (type.equals(Route.class))
            {
                Route entity = Route.getRoute((RouteInfo) info);
                save(entity, type, info);
            }
            if (type.equals(ArugamImage.class))
            {
                ArugamImage entity = ArugamImage.getArugamImage((ArugamImageInfo) info);
                save(entity, type, info);
            }
            if (type.equals(Booking.class))
            {
                Booking entity = Booking.getBooking((BookingInfo) info, null);
                save(entity, type, info);
            }
            if (type.equals(Contractor.class))
            {
                Contractor entity = Contractor.getContractor((ContractorInfo) info);
                save(entity, type, info);
            }
            if (type.equals(Agent.class))
            {
                Agent entity = Agent.getAgent((AgentInfo) info);
                save(entity, type, info);
            }
        }
    }

    public void save(ArugamEntity<?> entity, Class type, Info info)
    {
        EntityManager em = getEntityManager();
        entity.setKey(KeyFactory.createKey(type.getSimpleName(), info.getId()));
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
