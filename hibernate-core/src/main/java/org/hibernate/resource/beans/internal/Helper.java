/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.resource.beans.internal;

import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.resource.beans.container.internal.ContainerManagedLifecycleStrategy;
import org.hibernate.resource.beans.container.internal.JpaCompliantLifecycleStrategy;
import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.hibernate.service.ServiceRegistry;

import static org.hibernate.cfg.ManagedBeanSettings.ALLOW_EXTENSIONS_IN_CDI;
import static org.hibernate.engine.config.spi.StandardConverters.BOOLEAN;

/**
 * @author Steve Ebersole
 */
public final class Helper {

	private Helper() {
	}

	public static String determineBeanCacheKey(Class<?> beanType) {
		return beanType.getName();
	}

	public static String determineBeanCacheKey(String name, Class<?> beanType) {
		return beanType.getName() + ':' + name;
	}

	public static boolean allowExtensionsInCdi(ServiceRegistry serviceRegistry) {
		return serviceRegistry.requireService( ConfigurationService.class )
				.getSetting( ALLOW_EXTENSIONS_IN_CDI, BOOLEAN, false );
	}

	@SuppressWarnings("unused")
	public static BeanLifecycleStrategy getLifecycleStrategy(boolean shouldRegistryManageLifecycle) {
		return shouldRegistryManageLifecycle
				? JpaCompliantLifecycleStrategy.INSTANCE
				: ContainerManagedLifecycleStrategy.INSTANCE;
	}
}
