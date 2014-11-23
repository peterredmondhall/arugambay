package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RatingInfo implements IsSerializable
{

    public RatingInfo()
    {

    }

    private Long stars;
    private String critic;
    private String nickname;

    public Long getStars()
    {
        return stars;
    }

    public void setStars(Long stars)
    {
        this.stars = stars;
    }

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
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

}
