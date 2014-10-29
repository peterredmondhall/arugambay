package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@Entity
public class ArugamImage implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private Blob image;

    public Key getKey()
    {
        return key;
    }

    public Blob getImage()
    {
        return image;
    }

    public void setImage(Blob image)
    {
        this.image = image;
    }

}