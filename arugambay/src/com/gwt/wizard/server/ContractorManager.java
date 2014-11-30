package com.gwt.wizard.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

public class ContractorManager
{
    private static final Logger logger = Logger.getLogger(ContractorManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public ContractorInfo createContractor(ContractorInfo contractorInfo)
    {
        // check local user
        EntityManager em = getEntityManager();
        try
        {
            // To create new user for testing
            em.getTransaction().begin();
            Contractor contractor = Contractor.getContractor(contractorInfo);
            em.persist(contractor);
            em.getTransaction().commit();
            em.detach(contractor);

            contractorInfo = contractor.getInfo();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            em.close();
        }
        return contractorInfo;
    }

    public ContractorInfo getContractor(Long contractorId) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        ContractorInfo contractorInfo = null;
        {
            try
            {
                contractorInfo = em.find(Contractor.class, contractorId).getInfo();
            }
            catch (Exception e)
            {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            finally
            {
                em.close();
            }
        }
        return contractorInfo;

    }

    public List<ContractorInfo> getContractors(AgentInfo agentInfo)
    {
        // check local user
        EntityManager em = getEntityManager();
        List<ContractorInfo> list = Lists.newArrayList();
        try
        {
            @SuppressWarnings("unchecked")
            List<Contractor> contractorList = em.createQuery("select t from Contractor t where agentId=" + agentInfo.getId()).getResultList();
            for (Contractor contractor : contractorList)
            {
                list.add(contractor.getInfo());
            }
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            em.close();
        }
        return list;

    }

    public void delete(ContractorInfo contractorInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Contractor contractor = em.find(Contractor.class, contractorInfo.getId());
            em.remove(contractor);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            em.close();
        }
    }

    public List<ContractorInfo> deleteContractor(AgentInfo agentInfo, ContractorInfo contractorInfo)
    {
        List<ContractorInfo> contractors = null;
        EntityManager em = getEntityManager();
        try
        {
            Contractor contractor = em.find(Contractor.class, contractorInfo.getId());
            em.getTransaction().begin();
            em.remove(contractor);
            em.getTransaction().commit();
            contractor = em.find(Contractor.class, contractorInfo.getId());
            contractors = getContractors(agentInfo);
        }
        catch (Exception e)
        {
            logger.severe("deleting route");
        }
        finally
        {
            em.close();
        }
        return contractors;
    }

    public List<ContractorInfo> saveContractor(AgentInfo agentInfo, ContractorInfo contractorInfo, ContractorInfo.SaveMode mode) throws IllegalArgumentException
    {
        List<ContractorInfo> routes = null;
        EntityManager em = getEntityManager();
        contractorInfo.setAgentId(agentInfo.getId());
        try
        {
            Contractor contractor = null;
            switch (mode)
            {
                case ADD:
                    contractor = new Contractor();
                    persist(em, contractor, contractorInfo);
                    break;

                case UPDATE:
                    contractor = em.find(Contractor.class, contractorInfo.getId());
                    persist(em, contractor, contractorInfo);
                    break;
            }

            contractor = em.find(Contractor.class, contractor.getKey().getId());
            routes = getContractors(agentInfo);
        }

        catch (Exception e)
        {
            logger.severe("saving contractor");
        }
        finally
        {
            em.close();
        }
        return routes;
    }

    private void persist(EntityManager em, Contractor contractor, ContractorInfo contractorInfo)
    {
        contractor.setName(contractorInfo.getName());
        contractor.setAgentId(contractorInfo.getAgentId());

        em.getTransaction().begin();
        em.persist(contractor);
        em.getTransaction().commit();
        em.detach(contractor);

    }

}
