package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.ContractorInfo;

@Entity
public class Contractor implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String name;
    private Long userId;

    public static Contractor getContractor(ContractorInfo contractorInfo)
    {
        Contractor contractor = new Contractor();
        contractor.setName(contractorInfo.getName());
        contractor.setUserId(contractorInfo.getUserId());
        return contractor;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ContractorInfo getInfo()
    {
        ContractorInfo contractorInfo = new ContractorInfo();
        contractorInfo.setId(key.getId());
        contractorInfo.setName(name);
        contractorInfo.setUserId(userId);
        return contractorInfo;
    }

}