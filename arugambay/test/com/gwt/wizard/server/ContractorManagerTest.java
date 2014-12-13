package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.gwt.wizard.server.entity.Contractor;
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

    @Test
    public void should_delete_all()
    {

        ContractorInfo contractorInfo = new ContractorInfo();
        contractorInfo.setName("name");
        List<ContractorInfo> contractors = contractorManager.saveContractor(agentInfo, contractorInfo, ContractorInfo.SaveMode.ADD);
        assertEquals(3, contractors.size());
        contractorManager.deleteAll(Contractor.class);
        assertEquals(true, contractorManager.getAllInfo(Contractor.class).isEmpty());
    }

    @Test
    public void should_create_dataset()
    {

        ContractorInfo contractorInfo = new ContractorInfo();
        contractorInfo.setName("name");
        List<ContractorInfo> contractors = contractorManager.saveContractor(agentInfo, contractorInfo, ContractorInfo.SaveMode.ADD);
        assertEquals(3, contractors.size());

        String dataset = contractorManager.dump(Contractor.class);
        assertEquals((dataset.split("com.gwt.wizard.shared.model.ContractorInfo").length), 7);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_import() throws IOException
    {
        URL url = Resources.getResource("dataset.txt");
        String text = Resources.toString(url, Charsets.UTF_8);
        String dataset = null;
        for (String s : text.split("<list>"))
        {
            if (s.contains(ContractorInfo.class.getName()))
            {
                dataset = "<list>" + s;
                System.out.println(dataset);
                break;
            }
        }
        contractorManager.importDataset(dataset, Contractor.class);
        List<ContractorInfo> list = contractorManager.getAllInfo(Contractor.class);
        assertEquals(5, list.size());
        List<Long> ids = Lists.newArrayList();
        for (ContractorInfo info : list)
        {
            ids.add(info.getId());
        }
        Collections.sort(ids);
        System.out.println(ids);

        // System.out.println(dataset);
//        bs.importDataset(dataset, Booking.class);
//        List<BookingInfo> bookings = bs.getBookings();
//        assertEquals(2, bookings.size());

    }

}
