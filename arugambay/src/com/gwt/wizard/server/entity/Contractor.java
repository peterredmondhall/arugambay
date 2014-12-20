package com.gwt.wizard.server.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.common.collect.Lists;
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
    private List<String> address;

    public List<String> getAddress()
    {
        return address;
    }

    public void setAddress(List<String> address)
    {
        this.address = address;
    }

    private Long agentId;

    public static Contractor getContractor(ContractorInfo contractorInfo)
    {
        Contractor contractor = new Contractor();
        contractor.setName(contractorInfo.getName());
        contractor.setAgentId(contractorInfo.getAgentId());
        contractor.setAddress(contractorInfo.getAddress());
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
        contractorInfo.setAddress(Lists.newArrayList(address));
        return contractorInfo;
    }

}