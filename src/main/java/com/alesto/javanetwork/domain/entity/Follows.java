package com.alesto.javanetwork.domain.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "follows")
public class Follows extends BaseEntity {

	/*
     * ======================================================================
	 * ----- Constants
	 * ======================================================================
	 */

    public static final int CONSTRAINT_USERNAME_MIN_SIZE = 1;
    public static final int CONSTRAINT_USERNAME_MAX_SIZE = 255;

    private static final long serialVersionUID = 1L;

	/*
     * ======================================================================
	 * ----- Fields
	 * ======================================================================
	 */

    private Date created = null;

    private String username = null;

    private String followsUser = null;

	/*
	 * ======================================================================
	 * ----- Constructors
	 * ======================================================================
	 */

    public Follows() {
    }
	
	/*
	 * ======================================================================
	 * ----- Primitive accessors
	 * ======================================================================
	 */

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @NotBlank
    @Size(min = CONSTRAINT_USERNAME_MIN_SIZE, max = CONSTRAINT_USERNAME_MAX_SIZE)
    @Column(name = "username", length = CONSTRAINT_USERNAME_MAX_SIZE, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank
    @Size(min = CONSTRAINT_USERNAME_MIN_SIZE, max = CONSTRAINT_USERNAME_MAX_SIZE)
    @Column(name = "follows_user", length = CONSTRAINT_USERNAME_MAX_SIZE, nullable = false)
    public String getFollowsUser() {
        return followsUser;
    }

    public void setFollowsUser(String followsUser) {
        this.followsUser = followsUser;
    }

    /*
	 * ======================================================================
	 * ----- General Methods
	 * ======================================================================
	 */

    public String toString() {
        return "Timeline[ " +
                "username = " + username + " | " +
                "followsUser = " + followsUser +
                " ]";
    }

}
