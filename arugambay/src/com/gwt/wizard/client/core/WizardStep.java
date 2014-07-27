package com.gwt.wizard.client.core;

import com.google.gwt.user.client.ui.Composite;

public interface WizardStep
{

    public String getCaption();

    public Composite getContent();

    public Boolean onNext();

    public Boolean onBack();

    public void clear();
}
