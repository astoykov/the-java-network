package com.alesto.javanetwork.domain.rest.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class FollowsResource {

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

    private String followsUser;

    /*
     * ======================================================================
     * ----- Constructors
     * ======================================================================
     */

    public FollowsResource() {
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

    public String getFollowsUser() {
        return followsUser;
    }

    public void setFollowsUser(String followsUser) {
        this.followsUser = followsUser;
    }

}
