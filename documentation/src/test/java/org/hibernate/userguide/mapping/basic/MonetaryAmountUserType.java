/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.userguide.mapping.basic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;

/**
 * @author Emmanuel Bernard
 */
public class MonetaryAmountUserType implements CompositeUserType<MonetaryAmount> {

	@Override
	public Object getPropertyValue(MonetaryAmount component, int property) throws HibernateException {
		switch ( property ) {
			case 0:
				return component.getAmount();
			case 1:
				return component.getCurrency();
		}
		throw new HibernateException( "Illegal property index: " + property );
	}

	@Override
	public MonetaryAmount instantiate(ValueAccess valueAccess, SessionFactoryImplementor sessionFactory) {
		final BigDecimal amount = valueAccess.getValue(0, BigDecimal.class);
		final Currency currency = valueAccess.getValue(1, Currency.class);

		if ( amount == null && currency == null ) {
			return null;
		}

		return new MonetaryAmount( amount, currency );
	}

	@Override
	public Class<?> embeddable() {
		return MonetaryAmountEmbeddable.class;
	}

	@Override
	public Class<MonetaryAmount> returnedClass() {
		return MonetaryAmount.class;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Object deepCopy(Object value) {
		MonetaryAmount ma = (MonetaryAmount) value;
		return new MonetaryAmount( ma.getAmount(), ma.getCurrency() );
	}

	@Override
	public boolean equals(Object x, Object y) {
		if ( x == y ) {
			return true;
		}
		if ( x == null || y == null ) {
			return false;
		}
		return x.equals( y );
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) deepCopy( value );
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return deepCopy( cached );
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy( original ); //TODO: improve
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public static class MonetaryAmountEmbeddable {
		private BigDecimal amount;
		private Currency currency;
	}
}
