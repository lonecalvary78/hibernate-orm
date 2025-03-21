/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.processor.test.data.embeddedid;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;

@Repository
public interface ThingRepo extends CrudRepository<Thing, Thing.Id> {
	@Find
	Thing thing(Thing.Id id);
}
