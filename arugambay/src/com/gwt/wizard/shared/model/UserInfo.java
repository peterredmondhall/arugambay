package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserInfo implements IsSerializable
{

    public Long getContractorId()
    {
        return contractorId;
    }

    public void setProviderId(Long providerId)
    {
        this.contractorId = providerId;
    }

    public UserInfo()
    {

    }

    private Long id;
    private String email;
    private Long contractorId;

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
