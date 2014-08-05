package com.gwt.wizard.client.core;

import com.google.gwt.user.client.ui.Button;

public interface Showable
{
    void show(boolean visible, Button prev, Button next, Button cancel);
}
