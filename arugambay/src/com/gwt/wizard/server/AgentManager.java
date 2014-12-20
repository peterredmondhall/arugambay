package com.gwt.wizard.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Agent;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.AgentInfo;

public class AgentManager extends Manager
{
    private static final Logger logger = Logger.getLogger(AgentManager.class.getName());

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
            agentInfo = agent.getInfo();
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
            Agent agent = new Agent();
            agent.setUserEmail(TEST_AGENT);
            agent.setAdmin(true);
            em.persist(agent);
            em.getTransaction().commit();

        }
    }

    public AgentInfo getAgent(String email)
    {

        EntityManager em = getEntityManager();
        AgentInfo agentInfo = null;
        try
        {
            Agent agent = (Agent) em.createQuery("select u from Agent u where u.userEmail = '" + TEST_AGENT + "'").getSingleResult();
            agentInfo = agent.getInfo();
            logger.info("getUser for email " + email + " returned " + agentInfo.getEmail() + "  " + agentInfo.getId());
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

    public List<AgentInfo> getAgents()
    {
        List<AgentInfo> list = Lists.newArrayList();
        EntityManager em = getEntityManager();
        try
        {
            List<Agent> agents = em.createQuery("select u from Agent u ").getResultList();
            for (Agent agent : agents)
            {
                list.add(agent.getInfo());
            }
        }
        catch (NoResultException ex)
        {

        }
        finally
        {
            em.close();
        }
        return list;
    }

}