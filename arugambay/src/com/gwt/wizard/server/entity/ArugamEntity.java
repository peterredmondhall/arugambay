package com.gwt.wizard.server.entity;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.Info;

abstract public class ArugamEntity<T extends Info> implements Serializable
{
    private static final long serialVersionUID = 1L;

    abstract public void setKey(Key key);

    abstract public T getInfo();
}
