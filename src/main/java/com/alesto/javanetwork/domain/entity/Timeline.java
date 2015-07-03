package com.alesto.javanetwork.domain.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "timeline")
public class Timeline extends BaseEntity {

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

    private String message = null;

	/*
	 * ======================================================================
	 * ----- Constructors
	 * ======================================================================
	 */

    public Timeline() {
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
    @Column(name = "message", nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*
	 * ======================================================================
	 * ----- General Methods
	 * ======================================================================
	 */

    public String toString() {
        return "Timeline[ " +
                "username = " + username + " | " +
                "message = " + message +
                " ]";
    }

}
