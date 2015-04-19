package com.gwt.wizard.server.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.FinanceInfo;

@Entity
public class Finance extends ArugamEntity<FinanceInfo>
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private Date date;

    public static final Double AGENT_MARGIN = 0.90;
    private Long amount;
    private Long agentId;
    private String name;
    private String orderRef;
    private FinanceInfo.Type type;

    public FinanceInfo.Type getType()
    {
        return type;
    }

    public void setType(FinanceInfo.Type type)
    {
        this.type = type;
    }

    public Key getKey()
    {
        return key;
    }

    @Override
    public void setKey(Key key)
    {
        this.key = key;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Long getAmount()
    {
        return amount;
    }

    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
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
    public FinanceInfo getInfo()
    {
        FinanceInfo info = new FinanceInfo();
        info.setType(type);
        info.setName(name);
        info.setDate(date);
        info.setAmount(amount);
        info.setOrder(orderRef);
        return info;
    }

    public void setOrderRef(String orderRef)
    {
        this.orderRef = orderRef;

    }
}
