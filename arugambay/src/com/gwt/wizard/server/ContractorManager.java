package com.gwt.wizard.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.ContractorInfo;
import com.gwt.wizard.shared.model.UserInfo;

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

    public List<ContractorInfo> getContractors(UserInfo userInfo)
    {
        // check local user
        EntityManager em = getEntityManager();
        List<ContractorInfo> list = Lists.newArrayList();
        try
        {
            List<Contractor> contractorList = em.createQuery("select t from Contractor t where userId=" + userInfo.getId()).getResultList();
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
            Contractor contractor = Contractor.getContractor(contractorInfo);
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
}
