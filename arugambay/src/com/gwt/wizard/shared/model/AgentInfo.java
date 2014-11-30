package com.gwt.wizard.shared.model;

import java.io.Serializable;

public class AgentInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

}
