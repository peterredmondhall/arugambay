package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

public class ContractorManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    ContractorManager contractorManager = new ContractorManager();

    AgentInfo agentInfo;

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
    public void should_fetch_contractors()
    {

        List<ContractorInfo> routes = contractorManager.getContractors(agentInfo);
        assertEquals(2, routes.size());
    }

    @Test
    public void should_delete_route()
    {
        List<ContractorInfo> contractors = contractorManager.getContractors(agentInfo);
        assertEquals(2, contractors.size());
        ContractorInfo routeInfo = contractors.get(0);
        contractors = contractorManager.deleteContractor(agentInfo, routeInfo);
        assertEquals(1, contractors.size());
    }

    @Test
    public void should_update_contractor()
    {
        List<ContractorInfo> contractors = contractorManager.getContractors(agentInfo);
        assertEquals(2, contractors.size());
        ContractorInfo contractorInfo = contractors.get(0);
        contractorInfo.setName("new name");
        assertEquals(2, contractorManager.saveContractor(agentInfo, contractorInfo, ContractorInfo.SaveMode.UPDATE).size());
        contractorInfo = contractors.get(0);
        assertEquals("new name", contractorInfo.getName());

    }

    @Test
    public void should_add_contractor()
    {

        ContractorInfo contractorInfo = new ContractorInfo();
        contractorInfo.setName("name");
        List<ContractorInfo> contractors = contractorManager.saveContractor(agentInfo, contractorInfo, ContractorInfo.SaveMode.ADD);
        assertEquals(3, contractors.size());
        contractorInfo = contractors.get(2);
        assertEquals("name", contractorInfo.getName());
    }

}
