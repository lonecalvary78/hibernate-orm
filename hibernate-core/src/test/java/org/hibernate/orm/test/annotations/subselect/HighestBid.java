/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.annotations.subselect;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

/**
 * @author Sharath Reddy
 *
 */
@Entity
@Subselect("select i.name as name, max(b.amount) as amount from Item i, Bid b where b.itemId = i.id group by i.name")
@Synchronize({"Item", "Bid"})
public class HighestBid {

	private String name;
	private double amount;

	@Id
	public String getName() {
		return name;
	}
	public void setName(String val) {
		this.name = val;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}



}
