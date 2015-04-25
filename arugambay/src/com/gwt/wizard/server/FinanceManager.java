package com.gwt.wizard.server;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Agent;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Finance;
import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.FinanceInfo;

public class FinanceManager extends Manager
{
    private static final Logger logger = Logger.getLogger(FinanceManager.class.getName());

    public class FinanceInfoComparator implements Comparator<FinanceInfo>
    {

        @Override
        public int compare(FinanceInfo bi1, FinanceInfo bi2)
        {
            return (new DateTime(bi1.getDate()).isAfter(new DateTime(bi2.getDate()))) ? -1 : 1;
        }
    }

    public List<FinanceInfo> getFinance(AgentInfo agentInfo)
    {
        List<FinanceInfo> list = Lists.newArrayList();
        EntityManager em = getEntityManager();
        try
        {
            String query = "select t from Finance t";
            @SuppressWarnings("unchecked")
            List<Finance> resultList = em.createQuery(query).getResultList();
            if (resultList.size() == 0)
            {
                Finance transfer = new Finance();
                em.getTransaction().begin();
                em.persist(transfer);
                em.getTransaction().commit();
                em.detach(transfer);
            }
            else
            {
                query = "select t from Finance t where agentId=" + agentInfo.getId();
                resultList = em.createQuery(query).getResultList();
                for (Finance transfer : resultList)
                {
                    em.detach(transfer);
                    FinanceInfo info = transfer.getInfo();
                    list.add(transfer.getInfo());
                }
            }

        }
        finally
        {
            em.close();
        }
        Collections.sort(list, new FinanceInfoComparator());
        return list;
    }

    public void addTransfer(AgentInfo agentInfo, Date date, Long amount)
    {
        add(FinanceInfo.Type.TRANSFER, date, "taxisurfr", agentInfo, amount, "");
    }

    public void addPayment(BookingInfo bookingInfo, Date date)
    {
        Route route = getEntityManager().find(Route.class, bookingInfo.getRouteId());
        Contractor contractor = getEntityManager().find(Contractor.class, route.getContractorId());
        Agent agent = getEntityManager().find(Agent.class, contractor.getAgentId());
        AgentInfo agentInfo = agent.getInfo();
        Long amount = route.getAgentCents() != null ? route.getAgentCents() : (long) (route.getCents() * 0.90);
        logger.info("addpayment:" + amount);
        add(FinanceInfo.Type.PAYMENT, date, bookingInfo.getName(), agentInfo, amount, bookingInfo.getOrderRef());

    }

    private void add(FinanceInfo.Type type, Date date, String name, AgentInfo agentInfo, Long amount, String orderRef)
    {
        EntityManager em = getEntityManager();

        try
        {
            Finance finance = new Finance();
            finance.setAgentId(agentInfo.getId());
            finance.setType(type);
            finance.setDate(date);
            finance.setName(name);
            finance.setAmount(amount);
            finance.setOrderRef(orderRef);

            em.getTransaction().begin();
            em.persist(finance);
            em.getTransaction().commit();
            em.detach(finance);
        }
        finally
        {
            em.close();
        }

    }
}