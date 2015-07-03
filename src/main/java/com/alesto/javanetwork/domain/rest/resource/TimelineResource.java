package com.alesto.javanetwork.domain.rest.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class TimelineResource {
    
    /*
     * ======================================================================
     * ----- Constants
     * ======================================================================
     */

    public static final String LINK_NAME_ACTIONS = "actions";
    
    /*
     * ======================================================================
     * ----- Fields
     * ======================================================================
     */

    private String id;

    private Date created;

    private String username;

    private String message;
    
    /*
     * ======================================================================
     * ----- Constructors
     * ======================================================================
     */

    public TimelineResource() {
    }

    /*
     * ======================================================================
     * ----- Primitive accessors
     * ======================================================================
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
