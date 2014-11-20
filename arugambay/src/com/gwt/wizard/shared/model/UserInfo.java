package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserInfo implements IsSerializable
{

    public UserInfo()
    {

    }

    transient public static final Long PUBLIC = -1L;
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
