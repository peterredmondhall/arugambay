package com.gwt.wizard.shared.model;

public class ContractorInfo extends Info
{
    public enum SaveMode
    {
        UPDATE,
        ADD
    };

    private String name;
    // private Long id;
    private Long agentId;

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }

//    @Override
//    public Long getId()
//    {
//        return id;
//    }
//
//    @Override
//    public void setId(Long id)
//    {
//        this.id = id;
//    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
