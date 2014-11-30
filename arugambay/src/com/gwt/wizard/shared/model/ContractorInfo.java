package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContractorInfo implements IsSerializable
{
    public enum SaveMode
    {
        UPDATE,
        ADD
    };

    private String name;
    private Long id;
    private Long agentId;

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
