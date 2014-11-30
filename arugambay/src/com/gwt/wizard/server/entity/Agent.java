package com.gwt.wizard.server.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

@Entity
public class Agent implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private String userEmail;

    public Key getKey()
    {
        return key;
    }

    public void setKey(Key key)
    {
        this.key = key;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public AgentInfo getInfo(List<ContractorInfo> contractors)
    {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setId(key.getId());
        agentInfo.setEmail(userEmail);
        return agentInfo;

    }
}
