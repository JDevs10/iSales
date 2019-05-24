package com.iSales.interfaces;

import com.iSales.pages.calendar.Events;

public interface ItemClickListenerAgendaEvents {
    public void OnItemClickAgendaEventAdd(int position);
    public void OnItemClickAgendaEventView(int position, Events eventData);
    public void OnItemClickAgendaEventDelete(int position, Events eventData);
}
