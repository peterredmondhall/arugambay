package com.gwt.wizard.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.shared.GWT;
import com.gwt.wizard.server.jpa.EMF;

/*
 * Login Google user and redirect to application main page
 * 
 */
public class LoginServlet extends HttpServlet
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null)
        {
            // send to Google login page
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
            return;
        }

        // check local user
        EntityManager em = getEntityManager();
        com.gwt.wizard.server.entity.User appUser = null;
        try
        {
            // To create new user for testing
            createDefaultUser(em);

            try
            {
                appUser = (com.gwt.wizard.server.entity.User) em.createQuery("select u from User u where u.userEmail = '" + user.getEmail() + "'").getSingleResult();
            }
            catch (Exception e)
            {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
            if (appUser == null)
            {
                resp.getWriter().write("You are not authorized to access Admin Portal!");
                return;
            }
            req.getSession().setAttribute("user", appUser);

            if (!GWT.isProdMode())
            {
                resp.sendRedirect("/dashboard.html?gwt.codesvr=127.0.0.1:9997");  // localhost dev mode
            }
            else
            {
                resp.sendRedirect("/dashboard.html");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
    }

    private void createDefaultUser(EntityManager em)
    {
        String user = "peterredmondhall@gmail.com";

        if (!GWT.isProdMode())
        {
            user = "test@example.com";
        }
        try
        {
            em.createQuery("select u from User u where u.userEmail = '" + user + "'").getSingleResult();
        }
        catch (NoResultException ex)
        {
            em.getTransaction().begin();
            com.gwt.wizard.server.entity.User newAppUser = new com.gwt.wizard.server.entity.User();
            newAppUser.setUserEmail(user);
            em.persist(newAppUser);
            em.getTransaction().commit();

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        doPost(req, resp);
    }

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

}
