/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.dialect;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.hql.spi.id.AbstractMultiTableBulkIdStrategyImpl;

import org.junit.Test;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;

public class Oracle8iDialectTestCase extends BaseUnitTestCase {

	@Test
	@TestForIssue(jiraKey = "HHH-9290")
	public void testTemporaryTableNameTruncation() throws Exception {
		final AbstractMultiTableBulkIdStrategyImpl strategy = (AbstractMultiTableBulkIdStrategyImpl) new Oracle8iDialect().getDefaultMultiTableBulkIdStrategy();

		String temporaryTableName = strategy.getIdTableSupport().generateIdTableName(
				"TABLE_NAME_THAT_EXCEEDS_30_CHARACTERS"
		);

		assertEquals(
				"Temporary table names should be truncated to 30 characters",
				30,
				temporaryTableName.length()
		);
		assertEquals(
				"Temporary table names should start with HT_",
				"HT_TABLE_NAME_THAT_EXCEEDS_30_",
				temporaryTableName
		);
	}

	@Test
	public void testGetForUpdateStringWithAliasesAndLockOptions() {
		Oracle8iDialect dialect = new Oracle8iDialect();
		LockOptions lockOptions = new LockOptions();
		lockOptions.setAliasSpecificLockMode( "tableAlias1", LockMode.PESSIMISTIC_WRITE );

		String forUpdateClause = dialect.getForUpdateString( "tableAlias1", lockOptions );
		assertEquals( " for update of tableAlias1", forUpdateClause );

		lockOptions.setAliasSpecificLockMode( "tableAlias2", LockMode.PESSIMISTIC_WRITE );
		forUpdateClause = dialect.getForUpdateString( "tableAlias1,tableAlias2", lockOptions );
		assertEquals( " for update of tableAlias1,tableAlias2", forUpdateClause );
	}

}