package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Config implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private String profil;

    public String getProfil()
    {
        return profil;
    }

    public void setProfil(String profil)
    {
        this.profil = profil;
    }

}
