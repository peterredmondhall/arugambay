package com.gwt.wizard.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.ContractorInfo;

@Entity
public class Contractor extends ArugamEntity<ContractorInfo>
{

    public Key getKey()
    {
        return key;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    @Override
    public void setKey(Key key)
    {
        this.key = key;
    }

    private String name;
    private Long agentId;

    public static Contractor getContractor(ContractorInfo contractorInfo)
    {
        Contractor contractor = new Contractor();
        contractor.setName(contractorInfo.getName());
        contractor.setAgentId(contractorInfo.getAgentId());
        return contractor;
    }

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(Long userId)
    {
        this.agentId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public ContractorInfo getInfo()
    {
        ContractorInfo contractorInfo = new ContractorInfo();
        contractorInfo.setId(key.getId());
        contractorInfo.setName(name);
        contractorInfo.setAgentId(agentId);
        return contractorInfo;
    }

}