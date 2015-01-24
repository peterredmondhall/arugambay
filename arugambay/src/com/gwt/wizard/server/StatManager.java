package com.gwt.wizard.server;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.common.collect.Maps;
import com.gwt.wizard.server.entity.Stat;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.model.StatInfo;

public class StatManager extends Manager
{
    private static final Logger logger = Logger.getLogger(StatManager.class.getName());
    public static String newline = System.getProperty("line.separator");

    public void sendStat(StatInfo statInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            Stat stat = Stat.getStat(statInfo);
            em.persist(stat);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
    }

    public void report()
    {
        Map<String, Integer> countries = Maps.newHashMap();
        List<Stat> list = getAll(Stat.class);
        for (Stat stat : list)
        {
            String country = stat.getCountry();
            if (countries.get(country) == null)
            {
                countries.put(country, 0);
            }
            Integer count = countries.get(country);
            count++;
            countries.put(country, count);
        }

        String report = "countries:" + countries.keySet().size();
        for (String country : countries.keySet())
        {
            Integer count = countries.get(country);
            report += "<br>" + country + " " + count;
        }

        Mailer.sendReport(report);
        deleteAll(Stat.class);

    }
}
