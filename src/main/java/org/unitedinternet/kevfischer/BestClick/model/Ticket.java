package org.unitedinternet.kevfischer.BestClick.model;

import java.io.Serializable;

public class Ticket implements Serializable {
    public enum STATUS {
        WAITING, GOT, RESOLVED, DONE
    }

    public enum PROVIDER {
        INSIDE
    }

    private STATUS status;
    private PROVIDER provider;
    private String providerTicket;
    private String ticketId;

    public String getProviderTicket() {
        return providerTicket;
    }

    public void setProviderTicket(String providerTicket) {
        this.providerTicket = providerTicket;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public PROVIDER getProvider() {
        return provider;
    }

    public void setProvider(PROVIDER provider) {
        this.provider = provider;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
