/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.hbm.uk;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.unique.AlterTableUniqueDelegate;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.UniqueKey;
import org.hibernate.tool.schema.internal.SchemaCreatorImpl;
import org.hibernate.tool.schema.internal.SchemaDropperImpl;

import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.testing.util.ServiceRegistryUtil;

import org.hibernate.orm.test.hbm.index.JournalingSchemaToolingTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Steve Ebersole
 */
public class UniqueDelegateTest extends BaseUnitTestCase {
	private static int getColumnDefinitionUniquenessFragmentCallCount = 0;
	private static int getTableCreationUniqueConstraintsFragmentCallCount = 0;
	private static int getAlterTableToAddUniqueKeyCommandCallCount = 0;
	private static int getAlterTableToDropUniqueKeyCommandCallCount = 0;

	private StandardServiceRegistry ssr;

	@Before
	public void before() {
		ssr = ServiceRegistryUtil.serviceRegistryBuilder()
				.applySetting( AvailableSettings.DIALECT, MyDialect.class )
				.build();
	}

	@After
	public void after() {
		if ( ssr != null ) {
			StandardServiceRegistryBuilder.destroy( ssr );
		}
	}

	@Test
	@JiraKey( value = "HHH-10203" )
	public void testUniqueDelegateConsulted() {
		final Metadata metadata = new MetadataSources( ssr )
				.addResource( "org/hibernate/orm/test/hbm/uk/person_unique.hbm.xml" )
				.buildMetadata();

		final JournalingSchemaToolingTarget target = new JournalingSchemaToolingTarget();
		new SchemaCreatorImpl( ssr ).doCreation( metadata, false, target );

		assertThat( getAlterTableToAddUniqueKeyCommandCallCount, equalTo( 1 ) );
		assertThat( getColumnDefinitionUniquenessFragmentCallCount, equalTo( 1 ) );
		assertThat( getTableCreationUniqueConstraintsFragmentCallCount, equalTo( 1 ) );

		new SchemaDropperImpl( ssr ).doDrop( metadata, false, target );

		// unique keys are not dropped explicitly
		assertThat( getAlterTableToAddUniqueKeyCommandCallCount, equalTo( 1 ) );
		assertThat( getColumnDefinitionUniquenessFragmentCallCount, equalTo( 1 ) );
		assertThat( getTableCreationUniqueConstraintsFragmentCallCount, equalTo( 1 ) );
	}

	public static class MyDialect extends H2Dialect {
		private MyUniqueDelegate myUniqueDelegate;

		public MyDialect() {
			this.myUniqueDelegate = new MyUniqueDelegate( this );
		}

		@Override
		public UniqueDelegate getUniqueDelegate() {
			return myUniqueDelegate;
		}
	}

	public static class MyUniqueDelegate extends AlterTableUniqueDelegate {

		/**
		 * Constructs DefaultUniqueDelegate
		 *
		 * @param dialect The dialect for which we are handling unique constraints
		 */
		public MyUniqueDelegate(Dialect dialect) {
			super( dialect );
		}

		@Override
		public String getColumnDefinitionUniquenessFragment(Column column,
				SqlStringGenerationContext context) {
			getColumnDefinitionUniquenessFragmentCallCount++;
			return super.getColumnDefinitionUniquenessFragment( column, context );
		}

		@Override
		public String getTableCreationUniqueConstraintsFragment(Table table,
				SqlStringGenerationContext context) {
			getTableCreationUniqueConstraintsFragmentCallCount++;
			return super.getTableCreationUniqueConstraintsFragment( table, context );
		}

		@Override
		public String getAlterTableToAddUniqueKeyCommand(
				UniqueKey uniqueKey, Metadata metadata,
				SqlStringGenerationContext context) {
			getAlterTableToAddUniqueKeyCommandCallCount++;
			return super.getAlterTableToAddUniqueKeyCommand( uniqueKey, metadata, context );
		}

		@Override
		public String getAlterTableToDropUniqueKeyCommand(
				UniqueKey uniqueKey, Metadata metadata,
				SqlStringGenerationContext context) {
			getAlterTableToDropUniqueKeyCommandCallCount++;
			return super.getAlterTableToDropUniqueKeyCommand( uniqueKey, metadata, context );
		}
	}
}
