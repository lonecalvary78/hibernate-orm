/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.envers.entities.onetomany.detached;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Entity for {@link org.hibernate.orm.test.envers.integration.onetomany.detached.DoubleJoinColumnBidirectionalList} test.
 * Owned side of the first relation.
 *
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "DoubleJoinColBiRefEd1")
@Audited
public class DoubleListJoinColumnBidirectionalRefEdEntity1 {
	@Id
	@GeneratedValue
	private Integer id;

	private String data;

	@ManyToOne
	@JoinColumn(name = "some_join_column_1", insertable = false, updatable = false)
	private DoubleListJoinColumnBidirectionalRefIngEntity owner;

	public DoubleListJoinColumnBidirectionalRefEdEntity1() {
	}

	public DoubleListJoinColumnBidirectionalRefEdEntity1(
			Integer id,
			String data,
			DoubleListJoinColumnBidirectionalRefIngEntity owner) {
		this.id = id;
		this.data = data;
		this.owner = owner;
	}

	public DoubleListJoinColumnBidirectionalRefEdEntity1(
			String data,
			DoubleListJoinColumnBidirectionalRefIngEntity owner) {
		this.data = data;
		this.owner = owner;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public DoubleListJoinColumnBidirectionalRefIngEntity getOwner() {
		return owner;
	}

	public void setOwner(DoubleListJoinColumnBidirectionalRefIngEntity owner) {
		this.owner = owner;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof DoubleListJoinColumnBidirectionalRefEdEntity1) ) {
			return false;
		}

		DoubleListJoinColumnBidirectionalRefEdEntity1 that = (DoubleListJoinColumnBidirectionalRefEdEntity1) o;

		if ( data != null ? !data.equals( that.data ) : that.data != null ) {
			return false;
		}
		//noinspection RedundantIfStatement
		if ( id != null ? !id.equals( that.id ) : that.id != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}

	public String toString() {
		return "DoubleListJoinColumnBidirectionalRefIngEntity1(id = " + id + ", data = " + data + ")";
	}
}
