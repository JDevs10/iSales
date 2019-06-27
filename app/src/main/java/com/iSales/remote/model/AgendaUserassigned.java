package com.iSales.remote.model;


/**
 *      Created by Jean-Laurent
 */
public class AgendaUserassigned {
    private String id;
    /*
    private String mandatory;
    private String answer_status;
    */
    private String transparency;

    public AgendaUserassigned() {
    }

    public AgendaUserassigned(String id, String mandatory, String answer_status, String transparency) {
        this.id = id;
        /*
        this.mandatory = mandatory;
        this.answer_status = answer_status;
        */
        this.transparency = transparency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(String answer_status) {
        this.answer_status = answer_status;
    }
*/

    public String getTransparency() {
        return transparency;
    }

    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }
}
