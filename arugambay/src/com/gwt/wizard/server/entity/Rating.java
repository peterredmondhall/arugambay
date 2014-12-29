package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.RatingInfo;

@Entity
public class Rating implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    Long contractorId;

    Integer cleanliness;
    Integer safety;
    Integer punctuality;

    public Long getContractorId()
    {
        return contractorId;
    }

    public void setContractorId(Long contractorId)
    {
        this.contractorId = contractorId;
    }

    Integer professionality;
    String critic;
    String author;

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

    public String getCritic()
    {
        return critic;
    }

    public void setCritic(String critic)
    {
        this.critic = critic;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public static Rating getRating(RatingInfo ratingInfo)
    {
        Rating rating = new Rating();
        rating.setContractorId(ratingInfo.getContractorId());
        rating.setCleanliness(ratingInfo.getCleanliness());
        rating.setPunctuality(ratingInfo.getPunctuality());
        rating.setProfessionality(ratingInfo.getProfessionality());
        rating.setSafety(ratingInfo.getSafety());
        rating.setCritic(ratingInfo.getCritic());
        rating.setAuthor(ratingInfo.getAuthor());

        return rating;
    }

    public RatingInfo getInfo()
    {
        RatingInfo ratingInfo = new RatingInfo();
        ratingInfo.setCleanliness(cleanliness);
        ratingInfo.setPunctuality(punctuality);
        ratingInfo.setProfessionality(professionality);
        ratingInfo.setSafety(safety);
        ratingInfo.setCritic(critic);
        ratingInfo.setAuthor(author);

        return ratingInfo;
    }

}
