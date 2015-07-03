package com.alesto.javanetwork.domain.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity implements Persistable<String> {

	/*
	 * ======================================================================
	 * ----- Constants
	 * ======================================================================
	 */

    private static final long serialVersionUID = 1L;
	
	/*
	 * ======================================================================
	 * ----- Fields
	 * ======================================================================
	 */

    private String id = null;
	
	/*
	 * ======================================================================
	 * ----- Constructors
	 * ======================================================================
	 */

    public BaseEntity() {
    }
	
	/*
	 * ======================================================================
	 * ----- Primitive accessors
	 * ======================================================================
	 */

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
	
	/*
	 * ======================================================================
	 * ----- Methods
	 * ======================================================================
	 */

    @Transient
    @Override
    public boolean isNew() {
        return (this.id == null);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if ((other == null) || (this.getClass() != other.getClass())) {
            return false;
        }

        BaseEntity otherBaseEntity = (BaseEntity) other;

        return (this.id == null ? false : this.id.equals(otherBaseEntity.id));
    }

    @Override
    public int hashCode() {
        int hash = 1;

        int idCode = (this.id == null ? 0 : this.id.hashCode());

        hash = 31 * hash + idCode;

        return hash;
    }

}
