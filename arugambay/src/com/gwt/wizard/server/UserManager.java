package com.gwt.wizard.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.google.appengine.api.users.User;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.UserInfo;

public class UserManager
{
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    private static final String TEST_USER = "test@example.com";

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public UserInfo createUser(String userEmail)
    {
        // check local user
        EntityManager em = getEntityManager();
        UserInfo userInfo = null;
        try
        {
            // To create new user for testing
            createDefaultUser(em);

            com.gwt.wizard.server.entity.User appUser = (com.gwt.wizard.server.entity.User) em.createQuery("select u from User u where u.userEmail = '" + userEmail + "'").getSingleResult();
            userInfo = appUser.getInfo();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            em.close();
        }
        return userInfo;
    }

    private void createDefaultUser(EntityManager em)
    {

        try
        {
            em.createQuery("select u from User u where u.userEmail = '" + TEST_USER + "'").getSingleResult();
        }
        catch (NoResultException ex)
        {
            em.getTransaction().begin();
            com.gwt.wizard.server.entity.User newAppUser = new com.gwt.wizard.server.entity.User();
            newAppUser.setUserEmail(TEST_USER);
            em.persist(newAppUser);
            em.getTransaction().commit();

        }
    }

    public UserInfo getUser(String email)
    {

        EntityManager em = getEntityManager();
        UserInfo userInfo = null;
        try
        {
            com.gwt.wizard.server.entity.User user = (com.gwt.wizard.server.entity.User) em.createQuery("select u from User u where u.userEmail = '" + TEST_USER + "'").getSingleResult();
            userInfo = user.getInfo();
        }
        catch (NoResultException ex)
        {

        }
        finally
        {
            em.close();
        }
        return userInfo;
    }

    public UserInfo getUser(User user) throws IllegalArgumentException
    {
        return createUser(user.getEmail());
//      // load user devices
//      if (user == null)
//      {
//          return false;
//      }
//      return true;
    }

}
