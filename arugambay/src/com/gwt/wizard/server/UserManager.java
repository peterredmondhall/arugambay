package com.gwt.wizard.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Agent;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

public class UserManager
{
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    private static final String TEST_AGENT = "test@example.com";

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public AgentInfo createAgent(String agentEmail)
    {
        // check local user
        EntityManager em = getEntityManager();
        AgentInfo agentInfo = null;
        try
        {
            // To create new user for testing
            createDefaultAgent(em);

            Agent agent = (Agent) em.createQuery("select u from Agent u where u.userEmail = '" + agentEmail + "'").getSingleResult();
            List<ContractorInfo> contractors = Lists.newArrayList();
            agentInfo = agent.getInfo(contractors);
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            em.close();
        }
        return agentInfo;
    }

    private void createDefaultAgent(EntityManager em)
    {

        try
        {
            em.createQuery("select u from Agent u where u.userEmail = '" + TEST_AGENT + "'").getSingleResult();
        }
        catch (NoResultException ex)
        {
            em.getTransaction().begin();
            com.gwt.wizard.server.entity.Agent newAppUser = new com.gwt.wizard.server.entity.Agent();
            newAppUser.setUserEmail(TEST_AGENT);
            em.persist(newAppUser);
            em.getTransaction().commit();

        }
    }

    public AgentInfo getUser(String email)
    {

        EntityManager em = getEntityManager();
        AgentInfo agentInfo = null;
        try
        {
            Agent agent = (Agent) em.createQuery("select u from Agent u where u.userEmail = '" + TEST_AGENT + "'").getSingleResult();
            @SuppressWarnings("unchecked")
            List<Contractor> contractors = em.createQuery("select u from Contractor u where u.agentId = " + agent.getKey().getId()).getResultList();
            List<ContractorInfo> contractorIdList = Lists.newArrayList();
            for (Contractor contractor : contractors)
            {
                contractorIdList.add(contractor.getInfo());
            }
            agentInfo = agent.getInfo(contractorIdList);
        }
        catch (NoResultException ex)
        {

        }
        finally
        {
            em.close();
        }
        return agentInfo;
    }

//    public AgentInfo getUser(User user) throws IllegalArgumentException
//    {
//        return createAgent(user.getEmail());
//    }

}
