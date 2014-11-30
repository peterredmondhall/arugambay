package com.gwt.wizard.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

public class UserManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    BookingServiceManager bs = new BookingServiceManager();

    AgentInfo agentInfo;
    ContractorInfo contractorInfo;

    @Before
    public void setUp()
    {
        helper.setUp();
        new BookingServiceManager().getProfil();
        agentInfo = new BookingServiceImpl().createDefaultUser();
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void should_create_default_user()
    {
        agentInfo = new BookingServiceImpl().createDefaultUser();

    }

}
