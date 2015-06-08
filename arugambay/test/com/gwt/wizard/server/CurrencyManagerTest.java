package com.gwt.wizard.server;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Maps;
import com.gwt.wizard.shared.Currency;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

public class CurrencyManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    CurrencyManager manager = new CurrencyManager();

    AgentInfo agentInfo;
    ContractorInfo contractorInfo;
    float USDrate = 1.1218f;
    float AUDrate = 1.4534f;

    @Before
    public void setUp()
    {
        helper.setUp();
        Map<Currency, Float> map = Maps.newHashMap();
        map.put(Currency.USD, USDrate);
        map.put(Currency.AUD, AUDrate);
        manager.update(map);
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void should_give_rate()
    {
        Assert.assertEquals((Float) 1f, manager.getRate(Currency.USD));
        Assert.assertEquals((Float) (AUDrate / USDrate), manager.getRate(Currency.AUD));
        Assert.assertEquals((Float) (1 / USDrate), manager.getRate(Currency.EUR));
        Assert.assertEquals((Float) null, manager.getRate(Currency.GBP));
    }
}

//<Cube currency="USD" rate="1.1218"/>
//<Cube currency="JPY" rate="139.97"/>
//<Cube currency="BGN" rate="1.9557"/>
//<Cube currency="CZK" rate="27.396"/>
//<Cube currency="DKK" rate="7.4603"/>
//<Cube currency="GBP" rate="0.73170"/>
//<Cube currency="HUF" rate="311.28"/>
//<Cube currency="PLN" rate="4.1441"/>
//<Cube currency="RON" rate="4.4545"/>
//<Cube currency="SEK" rate="9.3417"/>
//<Cube currency="CHF" rate="1.0478"/>
//<Cube currency="NOK" rate="8.8085"/>
//<Cube currency="HRK" rate="7.5589"/>
//<Cube currency="RUB" rate="62.8100"/>
//<Cube currency="TRY" rate="2.9899"/>
//<Cube currency="AUD" rate="1.4534"/>
//<Cube currency="BRL" rate="3.5189"/>
//<Cube currency="CAD" rate="1.4023"/>
//<Cube currency="CNY" rate="6.9596"/>
//<Cube currency="HKD" rate="8.6979"/>
//<Cube currency="IDR" rate="14912.50"/>
//<Cube currency="ILS" rate="4.3118"/>
//<Cube currency="INR" rate="71.5602"/>
//<Cube currency="KRW" rate="1249.74"/>
//<Cube currency="MXN" rate="17.4451"/>
//<Cube currency="MYR" rate="4.1766"/>
//<Cube currency="NZD" rate="1.5713"/>
//<Cube currency="PHP" rate="50.501"/>
//<Cube currency="SGD" rate="1.5122"/>
//<Cube currency="THB" rate="37.829"/>
//<Cube currency="ZAR" rate="13.9126"/>
