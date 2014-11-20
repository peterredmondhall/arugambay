package com.gwt.wizard.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.gwt.wizard.server.UserManager;
import com.gwt.wizard.shared.model.UserInfo;

/*
 * Login Google user and redirect to application main page
 * 
 */
public class LoginServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(LoginServlet.class.getName());
    private final UserManager userManager = new UserManager();

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

        UserInfo userInfo = userManager.createUser(user.getEmail());
        if (userInfo != null)
        {
            req.getSession().setAttribute("user", user);

            resp.sendRedirect("/dashboard.html");

        }
        else
        {
            resp.getWriter().write("You are not authorized to access Admin Portal!");

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        doPost(req, resp);
    }
}
