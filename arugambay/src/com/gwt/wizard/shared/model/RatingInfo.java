package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RatingInfo implements IsSerializable
{
    private Long providerId;

    public Long getProviderId()
    {
        return providerId;
    }

    public void setProviderId(Long providerId)
    {
        this.providerId = providerId;
    }

    private String critic;

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public Integer getCleanliness()
    {
        return cleanliness;
    }

    public void setCleanliness(Integer cleanliness)
    {
        this.cleanliness = cleanliness;
    }

    public Integer getSafety()
    {
        return safety;
    }

    public void setSafety(Integer safety)
    {
        this.safety = safety;
    }

    public Integer getPunctuality()
    {
        return punctuality;
    }

    public void setPunctuality(Integer punctuality)
    {
        this.punctuality = punctuality;
    }

    public Integer getProfessionality()
    {
        return professionality;
    }

    public void setProfessionality(Integer professionality)
    {
        this.professionality = professionality;
    }

    private String author;

    Integer cleanliness;
    Integer safety;
    Integer punctuality;
    Integer professionality;

    public String getCritic()
    {
        return critic;
    }

    public void setCritic(String critic)
    {
        this.critic = critic;
    }

    public String getNickname()
    {
        return author;
    }

    public void setNickname(String nickname)
    {
        this.author = nickname;
    }

}
