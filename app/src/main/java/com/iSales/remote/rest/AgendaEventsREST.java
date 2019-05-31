package com.iSales.remote.rest;

import com.iSales.remote.model.AgendaEvents;

import java.util.ArrayList;

public class AgendaEventsREST extends ISalesREST {
    private ArrayList<AgendaEvents> agendaEvents;

    public AgendaEventsREST(){
    }

    public AgendaEventsREST(ArrayList<AgendaEvents> agendaEvents){
        this.agendaEvents = agendaEvents;
    }

    public ArrayList<AgendaEvents> getAgendaEvents() {
        return agendaEvents;
    }

    public void setAgendaEvents(ArrayList<AgendaEvents> agendaEvents) {
        this.agendaEvents = agendaEvents;
    }
}
