package com.iSales.interfaces;

import com.iSales.remote.rest.AgendaEventsREST;

public interface FindAgendaEventsListener {
    void onFindAgendaEventsTaskComplete(AgendaEventsREST mAgendaEventsREST);
    //void onFindOrderLinesTaskComplete(long commande_ref, long commande_id, FindOrderLinesREST findOrderLinesREST);
}
