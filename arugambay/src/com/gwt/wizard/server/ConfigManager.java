package com.gwt.wizard.server;

import com.gwt.wizard.server.entity.Config;

public class ConfigManager extends Manager
{

    public Config getConfig()
    {
        return Config.getConfig(getEntityManager());
    }
}
