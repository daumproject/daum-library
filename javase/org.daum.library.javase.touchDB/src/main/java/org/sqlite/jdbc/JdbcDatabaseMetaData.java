package org.sqlite.jdbc;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.sqlite.schema.ColumnMetaData;
import org.sqlite.Database;
import org.sqlite.Driver;
import org.sqlite.schema.DatabaseList;
import org.sqlite.schema.ForeignKeyList;
import org.sqlite.schema.IndexInfo;
import org.sqlite.schema.IndexList;
import org.sqlite.schema.TableInfo;
import static org.sqlite.swig.SQLite3.sqlite3_libversion;
import static org.sqlite.swig.SQLite3.sqlite3_libversion_number;
import static org.sqlite.swig.SQLite3Constants.SQLITE_INTEGER;
import static org.sqlite.swig.SQLite3Constants.SQLITE_FLOAT;
import static org.sqlite.swig.SQLite3Constants.SQLITE3_TEXT;
import static org.sqlite.swig.SQLite3Constants.SQLITE_BLOB;
import static org.sqlite.swig.SQLite3Constants.SQLITE_NULL;

/**
 *
 * @author calico
 */
public class JdbcDatabaseMetaData implements DatabaseMetaData {

    private final Database db;
    private final Connection conn;
    private final String url;
    
    JdbcDatabaseMetaData(Database db, Connection conn, String url) {
        this.db = db;
        this.conn = conn;
        this.url = url;
    }
    
    // START implements
    /**
     * It always returns false.
     * @return false
     */
    public boolean allProceduresAreCallable() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean allTablesAreSelectable() {
        return true;
    }

    /**
     * Retrieves the URL for this DBMS.
     * @return the URL for this DBMS.
     */
    public String getURL() {
        return url;
    }

    /**
     * It always returns null.
     * @return null
     */
    public String getUserName() {
        return null;
    }

    /**
     * invoke JdbcConnection#isReadOnly() method.
     * @return Returs the value of JdbcConnection#isReadOnly()
     * @throws java.sql.SQLException
     * @see org.sqlite.jdbc.JdbcConnection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean nullsAreSortedHigh() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean nullsAreSortedLow() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean nullsAreSortedAtStart() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean nullsAreSortedAtEnd() {
        return false;
    }

    /**
     * invoke org.sqlite.Database#getProductName() function.
     * @return Returns the value of org.sqlite.Database#getProductName()
     * @see org.sqlite.Database#getProductName()
     */
    public String getDatabaseProductName() {
        return Database.getProductName();
    }

    /**
     * invoke sqlite3_libversion() function.
     * @return Returns the value of sqlite3_libversion()
     * @see <a href="http://www.sqlite.org/c3ref/libversion.html">Run-Time Library Version Numbers</a>
     */
    public String getDatabaseProductVersion() {
        return sqlite3_libversion();
    }

    /**
     * invoke org.sqlite.Driver#getDriverName() function.
     * @return Returns the value of org.sqlite.Driver#getDriverName()
     * @see org.sqlite.Driver#getDriverName()
     */
    public String getDriverName() {
        return Driver.getDriverName();
    }

    /**
     * invoke org.sqlite.Driver#getDriverVersion() function.
     * @return
     * @see org.sqlite.Driver#getDriverVersion()
     */
    public String getDriverVersion() {
        return Driver.getDriverVersion();
    }

    /**
     * invoke org.sqlite.Driver#getDriverMajorVersion() function.
     * @return Returns the value of org.sqlite.Driver#getDriverMajorVersion()
     * @see org.sqlite.Driver#getDriverMajorVersion()
     */
    public int getDriverMajorVersion() {
        return Driver.getDriverMajorVersion();
    }

    /**
     * invoke org.sqlite.Driver#getDriverMinorVersion() function.
     * @return Returns the value of org.sqlite.Driver#getDriverMinorVersion()
     * @see org.sqlite.Driver#getDriverMinorVersion()
     */
    public int getDriverMinorVersion() {
        return Driver.getDriverMinorVersion();
    }

    /**
     * false is returned for the in-memory database. 
     * @return false is returned for the in-memory database. 
     * @see org.sqlite.Database#isInMemoryMode()
     */
    public boolean usesLocalFiles() {
        return !db.isInMemoryMode();
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean usesLocalFilePerTable() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsMixedCaseIdentifiers() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean storesUpperCaseIdentifiers() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean storesLowerCaseIdentifiers() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean storesMixedCaseIdentifiers() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsMixedCaseQuotedIdentifiers() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean storesUpperCaseQuotedIdentifiers() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean storesLowerCaseQuotedIdentifiers() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean storesMixedCaseQuotedIdentifiers() {
        return true;
    }

    /**
     * It always returns double quotes (").
     * @return double quotes (")
     */
    public String getIdentifierQuoteString() {
        return "\"";    // or single quote (')
    }

    /**
     * It always returns empty string.
     * @return empty string
     */
    public String getSQLKeywords() {
        // TODO 要調査！
        return "";
//        return
//            "ALTER TABLE,"
//            + "ANALYZE,"
//            + "ATTACH DATABASE,"
//            + "BEGIN TRANSACTION,"
//            + "comment,"
//            + "COMMIT TRANSACTION,"
//            + "CREATE INDEX,"
//            + "CREATE TABLE,"
//            + "CREATE TRIGGER,"
//            + "CREATE VIEW,"
//            + "CREATE VIRTUAL TABLE,"
//            + "DELETE,"
//            + "DETACH DATABASE,"
//            + "DROP INDEX,"
//            + "DROP TABLE,"
//            + "DROP TRIGGER,"
//            + "DROP VIEW,"
//            + "END TRANSACTION,"
//            + "EXPLAIN,"
//            + "expression,"
//            + "INSERT,"
//            + "ON CONFLICT clause,"
//            + "PRAGMA,"
//            + "REINDEX,"
//            + "REPLACE,"
//            + "ROLLBACK TRANSACTION,"
//            + "SELECT,"
//            + "UPDATE,"
//            + "VACUUM";
    }

    /**
     * It always returns empty string.
     * @return empty string
     */
    public String getNumericFunctions() {
        return "";
    }

    /**
     * It always returns empty string.
     * @return empty string
     */
    public String getStringFunctions() {
        return "";
    }

    /**
     * It always returns empty string.
     * @return empty string
     */
    public String getSystemFunctions() {
        return "";
    }

    /***
     * 
     * @return
     * @see <a href="http://www.sqlite.org/cvstrac/wiki?p=DateAndTimeFunctions">Date And Time Functions</a>
     */
    public String getTimeDateFunctions() {
        return
            "date,"
            + "time,"
            + "datetime,"
            + "julianday,"
            + "strftime";
    }

    /**
     * Escape charactor is NOTHING.
     * It always returns null.
     * @return null
     */
    public String getSearchStringEscape() {
        return null;
    }

    /**
     * It always returns empty string.
     * @return empty string
     */
    public String getExtraNameCharacters() {
        return "";
    }

    /**
     * Supported by SQLite 3.0.0 or later.
     * It always returns true.
     * @return true
     */
    public boolean supportsAlterTableWithAddColumn() {
//        return (SQLITE_VERSION_NUMBER > 3000000);
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsAlterTableWithDropColumn() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsColumnAliasing() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean nullPlusNonNullIsNull() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsConvert() {
        return false;
    }

    /**
     * It always returns false.
     * @param fromType ignored
     * @param toType ignored
     * @return false
     */
    public boolean supportsConvert(int fromType, int toType) {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsTableCorrelationNames() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsDifferentTableCorrelationNames() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsExpressionsInOrderBy() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsOrderByUnrelated() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsGroupBy() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsGroupByUnrelated() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsGroupByBeyondSelect() {
        // TODO JavaDoc の説明文が意味不明...
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsLikeEscapeClause() {
        return true;
    }

    /***
     * It always returns true.
     * @return true
     * @see org.sqlite.jdbc.JdbcStatement#execute(String)
     * @see org.sqlite.jdbc.JdbcStatement#getMoreResults()
     * @see org.sqlite.jdbc.JdbcStatement#getMoreResults(int)
     */
    public boolean supportsMultipleResultSets() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsMultipleTransactions() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsNonNullableColumns() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsMinimumSQLGrammar() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsCoreSQLGrammar() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsExtendedSQLGrammar() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     * @see <a href="http://www.sqlite.org/cvstrac/wiki?p=UnsupportedSql">sqlite - Unsupported Sql</a>
     */
    public boolean supportsANSI92EntryLevelSQL() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     * @see <a href="http://www.sqlite.org/cvstrac/wiki?p=UnsupportedSql">sqlite - Unsupported Sql</a>
     */
    public boolean supportsANSI92IntermediateSQL() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     * @see <a href="http://www.sqlite.org/cvstrac/wiki?p=UnsupportedSql">sqlite - Unsupported Sql</a>
     */
    public boolean supportsANSI92FullSQL() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsIntegrityEnhancementFacility() {
        // TODO 要調査！
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsOuterJoins() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsFullOuterJoins() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsLimitedOuterJoins() {
        // TODO 要調査！
        return true;
    }

    /**
     * Schema is not supported yet.
     * It always returns null.
     * @return null
     */
    public String getSchemaTerm() {
        // SQLiteはschemaに対応していない
        return null;
    }

    /**
     * Procedure is not supported yet.
     * It always returns null.
     * @return null
     */
    public String getProcedureTerm() {
        return null;
    }

    /**
     * Catalog is not supported yet.
     * It always returns null.
     * @return null
     */
    public String getCatalogTerm() {
        return null;
    }

    /**
     * Catalog is not supported yet.
     * It always returns false.
     * @return false
     */
    public boolean isCatalogAtStart() {
        return false;
    }

    /**
     * Catalog is not supported yet.
     * It always returns null.
     * @return null
     */
    public String getCatalogSeparator() {
        return null;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSchemasInProcedureCalls() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSchemasInIndexDefinitions() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSchemasInPrivilegeDefinitions() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsCatalogsInProcedureCalls() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsCatalogsInIndexDefinitions() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsCatalogsInPrivilegeDefinitions() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsPositionedDelete() {
        // OracleのROWNUM、SQL ServerのROW_NUMBER()、H2のROWNUM()のような
        // レコードセット毎に付番される擬似列が存在し、且つDELETE文で
        // 使用可能であるならtrueを返す。
        // SQLiteのROWID、OID、_ROWID_はレコードセット毎に付番されるものではないためfalse。
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsPositionedUpdate() {
        // OracleのROWNUM、SQL ServerのROW_NUMBER()、H2のROWNUM()のような
        // レコードセット毎に付番される擬似列が存在し、且つUPDATEE文で
        // 使用可能であるならtrueを返す。
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSelectForUpdate() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsStoredProcedures() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsSubqueriesInComparisons() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsSubqueriesInExists() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsSubqueriesInIns() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSubqueriesInQuantifieds() {
        // TODO 要調査！
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsCorrelatedSubqueries() {
        // 「Correlated Subqueries」とは相関サブクエリのこと
        // SQLiteは相関サブクエリに対応している
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsUnion() {
        return true;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsUnionAll() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsOpenCursorsAcrossCommit() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsOpenCursorsAcrossRollback() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsOpenStatementsAcrossCommit() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsOpenStatementsAcrossRollback() {
        return false;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxBinaryLiteralLength() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxCharLiteralLength() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxColumnNameLength() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxColumnsInGroupBy() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxColumnsInIndex() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxColumnsInOrderBy() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxColumnsInSelect() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxColumnsInTable() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxConnections() {
        return 0;
    }

    /**
     * Cursor is not supported yet.
     * It always returns 0.
     * @return 0
     */
    public int getMaxCursorNameLength() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxIndexLength() {
        return 0;
    }

    /**
     * Schema name is not supported yet.
     * It always returns 0.
     * @return 0
     */
    public int getMaxSchemaNameLength() {
        return 0;
    }

    /**
     * Procedure is not supported yet.
     * It always returns 0.
     * @return 0
     */
    public int getMaxProcedureNameLength() {
        return 0;
    }

    /**
     * Catalog is not supported yet.
     * It always returns 0.
     * @return 0
     */
    public int getMaxCatalogNameLength() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxRowSize() {
        return 0;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean doesMaxRowSizeIncludeBlobs() {
        return true;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxStatementLength() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxStatements() {
        return 0;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getMaxTableNameLength() {
        return 0;
    }

    /**
     * It always returns 64.
     * @return 64
     * @see <a href="http://www.sqlite.org/limits.html">Maximum Number Of Tables In A Join</a>
     */
    public int getMaxTablesInSelect() {
        return 64;
    }

    /**
     * User name is not supported yet.
     * It always returns 0.
     * @return 0
     */
    public int getMaxUserNameLength() {
        return 0;
    }

    /**
     * It always returns TRANSACTION_SERIALIZABLE.
     * @return java.sql.Connection.TRANSACTION_SERIALIZABLE
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_read_uncommitted">PRAGMA read_uncommitted</a>
     */
    public int getDefaultTransactionIsolation() {
        return Connection.TRANSACTION_SERIALIZABLE;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsTransactions() {
        return true;
    }

    /**
     * 
     * @param level
     * @return true if level is java.sql.Connection.TRANSACTION_SERIALIZABLE.
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_read_uncommitted">Pragma statements supported by SQLite:</a>
     */
    public boolean supportsTransactionIsolationLevel(int level) {
        return (level == Connection.TRANSACTION_SERIALIZABLE);
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsDataDefinitionAndDataManipulationTransactions() {
        return true;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsDataManipulationTransactionsOnly() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean dataDefinitionCausesTransactionCommit() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean dataDefinitionIgnoredInTransactions() {
        return false;
    }

    /**
     * Procedure is not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param procedureNamePattern ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS PROCEDURE_CAT"
                    + ", NULL AS PROCEDURE_SCHEM"
                    + ", NULL AS PROCEDURE_NAME"
                    + ", NULL AS RESERVED_4"
                    + ", NULL AS RESERVED_5"
                    + ", NULL AS RESERVED_6"
                    + ", NULL AS REMARKS"
                    + ", NULL AS SPECIFIC_NAME"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * Procedure is not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param procedureNamePattern ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS PROCEDURE_CAT"
                    + ", NULL AS PROCEDURE_SCHEM"
                    + ", NULL AS PROCEDURE_NAME"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS COLUMN_TYPE"
                    + ", NULL AS DATA_TYPE"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS PRECISION"
                    + ", NULL AS LENGTH"
                    + ", NULL AS SCALE"
                    + ", NULL AS RADIX"
                    + ", NULL AS NULLABLE"
                    + ", NULL AS REMARKS"
                    + ", NULL AS COLUMN_DEF"
                    + ", NULL AS SQL_DATA_TYPE"
                    + ", NULL AS SQL_DATETIME_SUB"
                    + ", NULL AS CHAR_OCTET_LENGTH"
                    + ", NULL AS ORDINAL_POSITION"
                    + ", NULL AS IS_NULLABLE"
                    + ", NULL AS SPECIFIC_NAME"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param tableNamePattern
     * @param types
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        final String sqlTemplate
                = "SELECT"
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", name AS TABLE_NAME"
                    + ", upper(type) AS TABLE_TYPE"
                    + ", NULL AS REMARKS"
                    + ", NULL AS TYPE_CAT"
                    + ", NULL AS TYPE_SCHEM"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS SELF_REFERENCING_COL_NAME"
                    + ", NULL AS REF_GENERATION"
                    + ", sql AS SQL"
                + " FROM sqlite_master"
                + " WHERE (?1 IS NULL OR name LIKE ?1)"
                    + " AND (type IN ({0}))"
                + " UNION ALL "
                + "SELECT"
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", name AS TABLE_NAME"
                    + ", upper(type) AS TABLE_TYPE"
                    + ", NULL AS REMARKS"
                    + ", NULL AS TYPE_CAT"
                    + ", NULL AS TYPE_SCHEM"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS SELF_REFERENCING_COL_NAME"
                    + ", NULL AS REF_GENERATION"
                    + ", sql AS SQL"
                + " FROM sqlite_temp_master"
                + " WHERE (?1 IS NULL OR name LIKE ?1)"
                    + " AND (type IN ({0}))"
                + " ORDER BY TABLE_TYPE, TABLE_NAME";

        if (types == null || types.length == 0) {
            types = new String[] { "table", "view" };
        }
        final int typesSize = types.length;
        final StringBuilder paramTypes = new StringBuilder();
        for (int i = 0; i < typesSize; ++i) {
            paramTypes.append("?").append(i + 2).append(",");
        }
        final int last = paramTypes.length();
        paramTypes.delete(last - 1, last);
        
        final Object[] arguments = new Object[] { paramTypes.toString() };
        final String sql = new MessageFormat(sqlTemplate).format(arguments);
        final JdbcPreparedStatement pstmt
                = (JdbcPreparedStatement) conn.prepareStatement(sql);
        int parameterIndex = 1;
        pstmt.setString(parameterIndex++, tableNamePattern);
        for (final String type : types) {
            pstmt.setString(parameterIndex++, type.toLowerCase());
        }
        final ResultSet rs = pstmt.executeQuery();
        pstmt.close(rs);
        return rs;
    }

    /**
     * Retrieves th open database list.
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getSchemas() throws SQLException {
        final List<DatabaseList> dl = getDatabaseList();
        String sql = null;
        if (dl.size() != 0) {
            final StringBuilder inlineView = new StringBuilder();
            for (final DatabaseList d : dl) {
                inlineView.append("SELECT '").append(d.name).append("' AS TABLE_SCHEM UNION ALL ");
            }
            sql = "SELECT TABLE_SCHEM, NULL AS TABLE_CATALOG "
                + "FROM ("
                        + inlineView.substring(0, inlineView.length() - 11)
                + ") ORDER BY TABLE_SCHEM";
        } else {
            // database not found
            sql = "SELECT NULL AS TABLE_SCHEM, NULL AS TABLE_CATALOG LIMIT 0";
        }
        return produceDetachedResultSet(sql);
    }

    /**
     * Catalog is not suppoted yet.
     * It always returns empty ResultSet.
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getCatalogs() throws SQLException {
        final String sql = "SELECT NULL AS TABLE_CAT LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * It always returns "TABLE" and "VIEW".
     * @return "TABLE" and "VIEW"
     * @throws java.sql.SQLException
     */
    public ResultSet getTableTypes() throws SQLException {
        final String sql
                = "SELECT 'TABLE' AS TABLE_TYPE "
                + "UNION ALL "
                + "SELECT 'VIEW' AS TABLE_TYPE";
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param tableNamePattern
     * @param columnNamePattern
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        String sql
                = "SELECT name FROM sqlite_master "
                    + "WHERE type = 'table' AND (?1 IS NULL OR upper(name) LIKE upper(?1)) "
                + "UNION ALL "
                + "SELECT name FROM sqlite_temp_master "
                    + "WHERE type = 'table' AND (?1 IS NULL OR upper(name) LIKE upper(?1))";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, tableNamePattern);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            rs.close();
            pstmt.close();
            sql = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", NULL AS TABLE_NAME"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS DATA_TYPE"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS COLUMN_SIZE"
                    + ", NULL AS BUFFER_LENGTH"
                    + ", NULL AS DECIMAL_DIGITS"
                    + ", NULL AS NUM_PREC_RADIX"
                    + ", NULL AS NULLABLE"
                    + ", NULL AS REMARKS"
                    + ", NULL AS COLUMN_DEF"
                    + ", NULL AS SQL_DATA_TYPE"
                    + ", NULL AS SQL_DATETIME_SUB"
                    + ", NULL AS CHAR_OCTET_LENGTH"
                    + ", NULL AS ORDINAL_POSITION"
                    + ", NULL AS IS_NULLABLE"
                    + ", NULL AS SCOPE_CATLOG"
                    + ", NULL AS SCOPE_SCHEMA"
                    + ", NULL AS SCOPE_TABLE"
                    + ", NULL AS SOURCE_DATA_TYPE"
                    + ", NULL AS IS_AUTOINCREMENT"
                + " LIMIT 0";
            return produceDetachedResultSet(sql);
        }
        
        final Statement stmt = conn.createStatement();
        final StringBuilder inlineView = new StringBuilder();
        do {
            final String tableName = rs.getString(1);
            final List<TableInfo> tblInf = getTableInfo(tableName);
            final ResultSet rsTbl = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
            final boolean hasRecord = rsTbl.next();
            final JdbcResultSetMetaData rsMeta = (JdbcResultSetMetaData) rsTbl.getMetaData();
            for (int i = 0; i < rsMeta.getColumnCount(); ++i) {
                final int col = i + 1;
                final String columnName = rsMeta.getColumnName(col);
                final TableInfo inf = searchTableInfo(tblInf, columnName);
                final int columnType = rsMeta.getSQLiteColumnType(col);
                final int dataType = getColumnType(columnType);
                final String typeName = rsMeta.getColumnTypeName(col);
                final int precision = getPrecision(columnType);
                final int radix = getRadix(columnType);
                final ColumnMetaData meta = db.getColumnMetaData(null, tableName, columnName);
                final boolean isNull = !meta.isNotNull;
                final boolean isAutoInc = meta.isAutoIncrement;
                
                inlineView.append("SELECT ");
                inlineView.append("'").append(tableName).append("' AS TABLE_NAME");
                inlineView.append(", '").append(columnName).append("' AS COLUMN_NAME");
                inlineView.append(", ").append(dataType).append(" AS DATA_TYPE");
                inlineView.append(", '").append(typeName).append("' AS TYPE_NAME");
                inlineView.append(", ").append((precision != 0 ? String.valueOf(precision) : "NULL")).append(" AS COLUMN_SIZE");
                inlineView.append(", ").append((radix != 0 ? String.valueOf(radix) : "NULL")).append(" AS NUM_PREC_RADIX");
                inlineView.append(", ").append((isNull ? columnNullable : columnNoNulls)).append(" AS NULLABLE");
                inlineView.append(", ").append(inf.defaultValue).append(" AS COLUMN_DEF");
                inlineView.append(", ").append(inf.columnIndex + 1).append(" AS ORDINAL_POSITION");
                inlineView.append(", '").append((isNull ? "YES" : "NO")).append("' AS IS_NULLABLE");
                inlineView.append(", '").append((isAutoInc ? "YES" : "NO")).append("' AS IS_AUTOINCREMENT");

                inlineView.append(" UNION ALL ");
            }
            rsTbl.close();
        } while (rs.next());
        stmt.close();
        rs.close();
        pstmt.close();

        sql = "SELECT "
                + "  NULL AS TABLE_CAT"
                + ", NULL AS TABLE_SCHEM"
                + ", TABLE_NAME"
                + ", COLUMN_NAME"
                + ", DATA_TYPE"
                + ", TYPE_NAME"
                + ", COLUMN_SIZE"
                + ", NULL AS BUFFER_LENGTH"
                + ", NULL AS DECIMAL_DIGITS"
                + ", NUM_PREC_RADIX"
                + ", NULLABLE"
                + ", NULL AS REMARKS"
                + ", COLUMN_DEF"
                + ", NULL AS SQL_DATA_TYPE"
                + ", NULL AS SQL_DATETIME_SUB"
                + ", NULL AS CHAR_OCTET_LENGTH"
                + ", ORDINAL_POSITION"
                + ", IS_NULLABLE"
                + ", NULL AS SCOPE_CATLOG"
                + ", NULL AS SCOPE_SCHEMA"
                + ", NULL AS SCOPE_TABLE"
                + ", NULL AS SOURCE_DATA_TYPE"
                + ", IS_AUTOINCREMENT"
            + " FROM (" + inlineView.substring(0, inlineView.length() - 11) + ")"
            + " WHERE (?1 IS NULL OR COLUMN_NAME LIKE ?1)"
            + " ORDER BY TABLE_NAME, ORDINAL_POSITION";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, columnNamePattern);
        rs = pstmt.executeQuery();
        ((JdbcPreparedStatement) pstmt).close(rs);
        return rs;
    }

    /**
     * Not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schema ignored
     * @param table ignored
     * @param columnNamePattern ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", NULL AS TABLE_NAME"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS GRANTOR"
                    + ", NULL AS GRANTEE"
                    + ", NULL AS PRIVILEGE"
                    + ", NULL AS IS_GRANTABLE"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * Not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param tableNamePattern ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", NULL AS TABLE_NAME"
                    + ", NULL AS GRANTOR"
                    + ", NULL AS GRANTEE"
                    + ", NULL AS PRIVILEGE"
                    + ", NULL AS IS_GRANTABLE"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param catalog ignored
     * @param schema ignored
     * @param table
     * @param scope ignored
     * @param nullable
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        String sql
                = "SELECT name FROM sqlite_master "
                    + "WHERE type = 'table' AND upper(name) = upper(?1) "
                + "UNION ALL "
                + "SELECT name FROM sqlite_temp_master "
                    + "WHERE type = 'table' AND upper(name) = upper(?1)";
        final PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, table);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            // table not found
            rs.close();
            pstmt.close();
            sql = "SELECT "
                    + "  NULL AS SCOPE"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS DATA_TYPE"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS COLUMN_SIZE"
                    + ", NULL AS BUFFER_LENGTH"
                    + ", NULL AS DECIMAL_DIGITS"
                    + ", NULL AS PSEUDO_COLUMN"
                + " LIMIT 0";
            return produceDetachedResultSet(sql);
        }
        
        final Statement stmt = conn.createStatement();
        final StringBuilder inlineView = new StringBuilder();
        do {
            final String tableName = rs.getString(1);
            final ResultSet rsTbl = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
            final boolean hasRecord = rsTbl.next();
            final JdbcResultSetMetaData rsMeta = (JdbcResultSetMetaData) rsTbl.getMetaData();
            final int max = rsMeta.getColumnCount();
            for (int i = 0; i < max; ++i) {
                final int col = i + 1;
                final String columnName = rsMeta.getColumnName(col);
                final ColumnMetaData meta = db.getColumnMetaData(null, tableName, columnName);
                if (meta.isPrimaryKey && (nullable || meta.isNotNull)) {
                    final int columnType = rsMeta.getSQLiteColumnType(col);
                    final int dataType = getColumnType(columnType);
                    final String typeName = rsMeta.getColumnTypeName(col);
                    final int precision = getPrecision(columnType);
                    
                    inlineView.append("SELECT ");
                    inlineView.append("'").append(columnName).append("' AS COLUMN_NAME");
                    inlineView.append(", ").append(dataType).append(" AS DATA_TYPE");
                    inlineView.append(", '").append(typeName).append("' AS TYPE_NAME");
                    inlineView.append(", ").append((precision != 0 ? String.valueOf(precision) : "NULL")).append(" AS COLUMN_SIZE");
                    inlineView.append(" UNION ALL ");
                }
            }
            rsTbl.close();
        } while (rs.next());
        stmt.close();
        rs.close();
        pstmt.close();

        if (inlineView.length() == 0) {
            // primary key not found
            final int columnType = SQLITE_INTEGER;
            final int dataType = getColumnType(columnType);
            final int precision = getPrecision(columnType);
            inlineView.append("SELECT '_ROWID_' AS COLUMN_NAME, ");
            inlineView.append(dataType).append(" AS DATA_TYPE");
            inlineView.append(", 'INTEGER' AS TYPE_NAME");
            inlineView.append(", ").append(precision).append(" AS COLUMN_SIZE");
        } else {
            // delete last ' UNION ALL '
            inlineView.delete(inlineView.length() - 11, inlineView.length());
        }

        sql = "SELECT "
                + bestRowSession + " AS SCOPE"
                + ", COLUMN_NAME"
                + ", DATA_TYPE"
                + ", TYPE_NAME"
                + ", COLUMN_SIZE"
                + ", NULL AS BUFFER_LENGTH"
                + ", NULL AS DECIMAL_DIGITS"
                + ", " + bestRowNotPseudo + " AS PSEUDO_COLUMN"
            + " FROM (" + inlineView.toString() + ")";
        return produceDetachedResultSet(sql);
    }

    /**
     * Not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schema ignored
     * @param table ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS SCOPE"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS DATA_TYPE"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS COLUMN_SIZE"
                    + ", NULL AS BUFFER_LENGTH"
                    + ", NULL AS DECIMAL_DIGITS"
                    + ", NULL AS PSEUDO_COLUMN"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param catalog ignored
     * @param schema ignored
     * @param table
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        final String tableName = table.replaceAll("'", "''");
        final List<TableInfo> tblInf = getTableInfo(tableName);
        String sql = null;
        if (tblInf.size() != 0) {
            final StringBuilder inlineView = new StringBuilder();
            int keySeq = 0;
            for (final TableInfo inf : tblInf) {
                if (inf.isPrimaryKey) {
                    inlineView.append("SELECT ");
                    inlineView.append("'").append(tableName).append("' AS TABLE_NAME");
                    inlineView.append(", '").append(inf.columnName).append("' AS COLUMN_NAME");
                    inlineView.append(", ").append(++keySeq).append(" AS KEY_SEQ");
                    inlineView.append(" UNION ALL ");
                }
            }
            if (keySeq == 0) {
                // primary key not found
                inlineView.append("SELECT '");
                inlineView.append(tableName);
                inlineView.append("' AS TABLE_NAME, '_ROWID_' AS COLUMN_NAME, 1 AS KEY_SEQ");
            } else {
                // delete last ' UNION ALL '
                inlineView.delete(inlineView.length() - 11, inlineView.length());
            }

           sql = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", TABLE_NAME"
                    + ", COLUMN_NAME"
                    + ", KEY_SEQ"
                    + ", NULL AS PK_NAME"
                + " FROM (" + inlineView.toString() + ")"
                + " ORDER BY COLUMN_NAME";
        } else {
            // table not found
            sql = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", NULL AS TABLE_NAME"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS KEY_SEQ"
                    + ", NULL AS PK_NAME"
                + " LIMIT 0";
        }
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param catalog ignored
     * @param schema ignored
     * @param table
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        if (table == null) {
            throw new NullPointerException("table is null.");
        }
        
        return getForeignKeys(null, table);
    }

    /**
     * 
     * @param catalog ignored
     * @param schema ignored
     * @param table
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        if (table == null) {
            throw new NullPointerException("table is null.");
        }

        String sql
                = "SELECT name FROM sqlite_master "
                    + "WHERE type = 'table' AND upper(name) <> upper(?1) "
                + "UNION ALL "
                + "SELECT name FROM sqlite_temp_master "
                    + "WHERE type = 'table' AND upper(name) <> upper(?1)";
        final JdbcPreparedStatement pstmt
                = (JdbcPreparedStatement) conn.prepareStatement(sql);
        pstmt.setString(1, table);
        final ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            // foreign table not found
            rs.close();
            pstmt.close();
            sql = "SELECT "
                    + "  NULL AS PKTABLE_CAT"
                    + ", NULL AS PKTABLE_SCHEM"
                    + ", NULL AS PKTABLE_NAME"
                    + ", NULL AS PKCOLUMN_NAME"
                    + ", NULL AS FKTABLE_CAT"
                    + ", NULL AS FKTABLE_SCHEM"
                    + ", NULL AS FKTABLE_NAME"
                    + ", NULL AS FKCOLUMN_NAME"
                    + ", NULL AS KEY_SEQ"
                    + ", NULL AS UPDATE_RULE"
                    + ", NULL AS DELETE_RULE"
                    + ", NULL AS FK_NAME"
                    + ", NULL AS PK_NAME"
                    + ", NULL AS DEFERRABILITY"
                + " LIMIT 0";
            return produceDetachedResultSet(sql);
        }
        
        final StringBuilder inlineView = new StringBuilder();
        do {
            final String foreignTable = rs.getString(1);
            final ResultSet rs2 = getForeignKeys(table, foreignTable);
            while (rs2.next()) {
                inlineView.append("SELECT ");
                inlineView.append(" '").append(rs2.getString("PKTABLE_NAME")).append("' AS PKTABLE_NAME");
                inlineView.append(", '").append(rs2.getString("PKCOLUMN_NAME")).append("' AS PKCOLUMN_NAME");
                inlineView.append(", '").append(rs2.getString("FKTABLE_NAME")).append("' AS FKTABLE_NAME");
                inlineView.append(", '").append(rs2.getString("FKCOLUMN_NAME")).append("' AS FKCOLUMN_NAME");
                inlineView.append(", ").append(rs2.getString("KEY_SEQ")).append(" AS KEY_SEQ");
                inlineView.append(", ").append(rs2.getString("UPDATE_RULE")).append(" AS UPDATE_RULE");
                inlineView.append(", ").append(rs2.getString("DELETE_RULE")).append(" AS DELETE_RULE");
                inlineView.append(", ").append(rs2.getString("DEFERRABILITY")).append(" AS DEFERRABILITY");
                inlineView.append(" UNION ALL ");
            }
            rs2.close();
        } while (rs.next());
        rs.close();
        pstmt.close();

        if (inlineView.length() == 0) {
            // foreign key not found
            sql = "SELECT "
                    + "  NULL AS PKTABLE_CAT"
                    + ", NULL AS PKTABLE_SCHEM"
                    + ", NULL AS PKTABLE_NAME"
                    + ", NULL AS PKCOLUMN_NAME"
                    + ", NULL AS FKTABLE_CAT"
                    + ", NULL AS FKTABLE_SCHEM"
                    + ", NULL AS FKTABLE_NAME"
                    + ", NULL AS FKCOLUMN_NAME"
                    + ", NULL AS KEY_SEQ"
                    + ", NULL AS UPDATE_RULE"
                    + ", NULL AS DELETE_RULE"
                    + ", NULL AS FK_NAME"
                    + ", NULL AS PK_NAME"
                    + ", NULL AS DEFERRABILITY"
                + " LIMIT 0";
        } else {
            sql = "SELECT "
                    + "  NULL AS PKTABLE_CAT"
                    + ", NULL AS PKTABLE_SCHEM"
                    + ", PKTABLE_NAME"
                    + ", PKCOLUMN_NAME"
                    + ", NULL AS FKTABLE_CAT"
                    + ", NULL AS FKTABLE_SCHEM"
                    + ", FKTABLE_NAME"
                    + ", FKCOLUMN_NAME"
                    + ", KEY_SEQ"
                    + ", UPDATE_RULE"
                    + ", DELETE_RULE"
                    + ", NULL AS FK_NAME"
                    + ", NULL AS PK_NAME"
                    + ", DEFERRABILITY"
                + " FROM (" + inlineView.substring(0, inlineView.length() - 11) + ")"
                + " ORDER BY FKTABLE_NAME, KEY_SEQ";
        }
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param parentCatalog ignored
     * @param parentSchema ignored
     * @param parentTable
     * @param foreignCatalog ignored
     * @param foreignSchema ignored
     * @param foreignTable
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        if (parentTable == null) {
            throw new NullPointerException("parentTable is null.");
        }
        if (foreignTable == null) {
            throw new NullPointerException("foreignTable is null.");
        }
        
        return getForeignKeys(parentTable, foreignTable);
    }

    public ResultSet getTypeInfo() throws SQLException {
        final String sql
                = "SELECT "
                    + "  TYPE_NAME"
                    + ", DATA_TYPE"
                    + ", PRECISION"
                    + ", LITERAL_PREFIX"
                    + ", LITERAL_SUFFIX"
                    + ", NULL AS CREATE_PARAMS"
                    + ", " + typeNullable + " AS NULLABLE"
                    + ", CASE_SENSITIVE"
                    + ", " + typeSearchable + " AS SEARCHABLE"
                    + ", UNSIGNED_ATTRIBUTE"
                    + ", 0 AS FIXED_PREC_SCALE"
                    + ", AUTO_INCREMENT"
                    + ", TYPE_NAME AS LOCAL_TYPE_NAME"
                    + ", 0 AS MINIMUM_SCALE"
                    + ", 0 AS MAXIMUM_SCALE"
                    + ", NULL AS SQL_DATA_TYPE"
                    + ", NULL AS SQL_DATETIME_SUB"
                    + ", NUM_PREC_RADIX"
                + " FROM ("
                        + "SELECT"
                            + "  'NULL' AS TYPE_NAME"
                            + ", " + Types.NULL + " AS DATA_TYPE"
                            + ", NULL AS PRECISION"
                            + ", NULL AS LITERAL_PREFIX"
                            + ", NULL AS LITERAL_SUFFIX"
                            + ", 0 AS CASE_SENSITIVE"
                            + ", 0 AS UNSIGNED_ATTRIBUTE"
                            + ", 0 AS AUTO_INCREMENT"
                            + ", NULL AS NUM_PREC_RADIX"
                        + " UNION ALL "
                        + "SELECT"
                            + "  'INTEGER' AS TYPE_NAME"
                            + ", " + Types.INTEGER + " AS DATA_TYPE"
                            + ", " + getPrecision(SQLITE_INTEGER) + " AS PRECISION"
                            + ", NULL AS LITERAL_PREFIX"
                            + ", NULL AS LITERAL_SUFFIX"
                            + ", 0 AS CASE_SENSITIVE"
                            + ", 1 AS UNSIGNED_ATTRIBUTE"
                            + ", 0 AS AUTO_INCREMENT"
                            + ", 10 AS NUM_PREC_RADIX"
                        + " UNION ALL "
                        + "SELECT"
                            + "  'REAL' AS TYPE_NAME"
                            + ", " + Types.REAL + " AS DATA_TYPE"
                            + ", " + getPrecision(SQLITE_FLOAT) + " AS PRECISION"    // 倍精度
                            + ", NULL AS LITERAL_PREFIX"
                            + ", NULL AS LITERAL_SUFFIX"
                            + ", 0 AS CASE_SENSITIVE"
                            + ", 1 AS UNSIGNED_ATTRIBUTE"
                            + ", 0 AS AUTO_INCREMENT"
                            + ", 10 AS NUM_PREC_RADIX"
                        + " UNION ALL "
                        + "SELECT"
                            + "  'TEXT' AS TYPE_NAME"
                            + ", " + Types.VARCHAR + " AS DATA_TYPE"
                            + ", NULL AS PRECISION"
                            + ", '''' AS LITERAL_PREFIX"
                            + ", '''' AS LITERAL_SUFFIX"
                            + ", 1 AS CASE_SENSITIVE"
                            + ", 0 AS UNSIGNED_ATTRIBUTE"
                            + ", 0 AS AUTO_INCREMENT"
                            + ", NULL AS NUM_PREC_RADIX"
                        + " UNION ALL "
                        + "SELECT"
                            + "  'BLOB' AS TYPE_NAME"
                            + ", " + Types.BLOB + " AS DATA_TYPE"
                            + ", NULL AS PRECISION"
                            + ", 'X''' AS LITERAL_PREFIX"
                            + ", '''' AS LITERAL_SUFFIX"
                            + ", 0 AS CASE_SENSITIVE"
                            + ", 0 AS UNSIGNED_ATTRIBUTE"
                            + ", 0 AS AUTO_INCREMENT"
                            + ", NULL AS NUM_PREC_RADIX"
                    + ") AS TYPE_INFO"
                + " ORDER BY TYPE_NAME";
        return produceDetachedResultSet(sql);
    }

    /**
     * 
     * @param catalog ignored
     * @param schema ignored
     * @param table
     * @param unique
     * @param approximate ignored
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        final String tableName = table.replaceAll("'", "''");
        final List<IndexList> ixl = getIndexList(tableName);
        String sql = null;
        if (ixl.size() != 0 && (!unique || containsUniqueIndex(ixl))) {
            final StringBuilder inlineView = new StringBuilder();
            for (final IndexList il : ixl) {
                final String nonUnique = (il.isUnique ? "1" : "0");
                final String indexName = il.indexName;
                final List<IndexInfo> ifl = getIndexInfo(indexName);
                for (final IndexInfo ix : ifl) {
                    inlineView.append("SELECT ");
                    inlineView.append(nonUnique).append(" AS NON_UNIQUE");
                    inlineView.append(", '").append(indexName).append("' AS INDEX_NAME");
                    inlineView.append(", ").append(ix.seqNo + 1).append(" AS ORDINAL_POSITION");
                    inlineView.append(", '").append(ix.columnName).append("' AS COLUMN_NAME");
                    inlineView.append(" UNION ALL ");
                }
            }

            sql = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", '" + tableName + "' AS TABLE_NAME"
                    + ", NON_UNIQUE"
                    + ", NULL AS INDEX_QUALIFIER"
                    + ", INDEX_NAME"
                    + ", " + tableIndexOther + " AS TYPE"
                    + ", ORDINAL_POSITION"
                    + ", COLUMN_NAME"
                    + ", 'A' AS ASC_OR_DESC"
                    + ", NULL CARDINALITY"
                    + ", NULL AS PAGES"
                    + ", NULL AS FILTER_CONDITION"
                + " FROM (" + inlineView.substring(0, inlineView.length() - 11) + ")"
                + " ORDER BY NON_UNIQUE, INDEX_NAME, ORDINAL_POSITION";            
        } else {
            // index not found or unique index not found
            sql = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", NULL AS TABLE_NAME"
                    + ", NULL AS NON_UNIQUE"
                    + ", NULL AS INDEX_QUALIFIER"
                    + ", NULL AS INDEX_NAME"
                    + ", NULL AS TYPE"
                    + ", NULL AS ORDINAL_POSITION"
                    + ", NULL AS COLUMN_NAME"
                    + ", NULL AS ASC_OR_DESC"
                    + ", NULL AS CARDINALITY"
                    + ", NULL AS PAGES"
                    + ", NULL AS FILTER_CONDITION"
                + " LIMIT 0";
        }
        return produceDetachedResultSet(sql);
    }

    /**
     * Supported type is ResultSet.TYPE_FORWARD_ONLY only.
     * @param type
     * @return true if type is java.sql.ResultSet.TYPE_FORWARD_ONLY.
     */
    public boolean supportsResultSetType(int type) {
        return (type == ResultSet.TYPE_FORWARD_ONLY);
    }

    /**
     * Supported type is ResultSet.TYPE_FORWARD_ONLY and concurrency is ResultSet.CONCUR_READ_ONLY only.
     * @param type
     * @param concurrency
     * @return true if type is java.sql.ResultSet.TYPE_FORWARD_ONLY and concurrency is java.sql.ResultSet.CONCUR_READ_ONLY.
     */
    public boolean supportsResultSetConcurrency(int type, int concurrency) {
        return (type == ResultSet.TYPE_FORWARD_ONLY
                && concurrency == ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean ownUpdatesAreVisible(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean ownDeletesAreVisible(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean ownInsertsAreVisible(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean othersUpdatesAreVisible(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean othersDeletesAreVisible(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean othersInsertsAreVisible(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean updatesAreDetected(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean deletesAreDetected(int type) {
        return false;
    }

    /**
     * It always returns false.
     * @param type ignored
     * @return false
     */
    public boolean insertsAreDetected(int type) {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsBatchUpdates() {
        return true;
    }

    /**
     * UDT is not supported yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param typeNamePattern ignored
     * @param types ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS TYPE_CAT"
                    + ", NULL AS TYPE_SCHEM"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS CLASS_NAME"
                    + ", NULL AS DATA_TYPE"
                    + ", NULL AS REMARKS"
                    + ", NULL AS BASE_TYPE"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    public Connection getConnection() {
        return conn;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsSavepoints() {
        return false;
    }

    /**
     * It always returns true.
     * @return true
     */
    public boolean supportsNamedParameters() {
        return true;
    }

    /**
     * CallableStatement is not supported yet.
     * It always returns false.
     * @return false
     */
    public boolean supportsMultipleOpenResults() {
        return false;
    }

    /**
     * Supported by sqlite3_last_insert_rowid() function.
     * It always returns true.
     * @return true
     * @see <a href="http://sqlite.org/c3ref/last_insert_rowid.html">Last Insert Rowid</a>
     */
    public boolean supportsGetGeneratedKeys() {
        // case of SQLite 3.3.5 or later
        return true;
    }

    /**
     * Not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param typeNamePattern ignored
     * @return empty ResultSe
     * @throws java.sql.SQLException
     */
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS TYPE_CAT"
                    + ", NULL AS TYPE_SCHEM"
                    + ", NULL AS TYPE_NAME"
                    + ", NULL AS SUPERTYPE_CAT"
                    + ", NULL AS SUPERTYPE_SCHEM"
                    + ", NULL AS SUPERTYPE_NAME"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * Not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param tableNamePattern ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS TABLE_CAT"
                    + ", NULL AS TABLE_SCHEM"
                    + ", NULL AS TABLE_NAME"
                    + ", NULL AS SUPERTABLE_NAME"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * Not suppoted yet.
     * It always returns empty ResultSet.
     * @param catalog ignored
     * @param schemaPattern ignored
     * @param typeNamePattern ignored
     * @param attributeNamePattern ignored
     * @return empty ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        final String sql
                = "SELECT "
                    + "  NULL AS TYPE_CAT"
                    + ", NULL AS TYPE_SCHEM"
                    + ", NULL AS ATTR_NAME"
                    + ", NULL AS DATA_TYPE"
                    + ", NULL AS ATTR_TYPE_NAME"
                    + ", NULL AS ATTR_SIZE"
                    + ", NULL AS DECIMAL_DIGITS"
                    + ", NULL AS NUM_PREC_RADIX"
                    + ", NULL AS NULLABLE"
                    + ", NULL AS REMARKS"
                    + ", NULL AS ATTR_DEF"
                    + ", NULL AS SQL_DATA_TYPE"
                    + ", NULL AS SQL_DATETIME_SUB"
                    + ", NULL AS CHAR_OCTET_LENGTH"
                    + ", NULL AS ORDINAL_POSITION"
                    + ", NULL AS IS_NULLABLE"
                    + ", NULL AS SCOPE_CATALOG"
                    + ", NULL AS SCOPE_SCHEMA"
                    + ", NULL AS SCOPE_TABLE"
                    + ", NULL AS SOURCE_DATA_TYPE"
                + " LIMIT 0";
        return produceDetachedResultSet(sql);
    }

    /**
     * Supported type is ResultSet.CLOSE_CURSORS_AT_COMMIT only.
     * @param holdability
     * @return true if holdability is java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT.
     */
    public boolean supportsResultSetHoldability(int holdability) {
        return (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     * It always returns ResultSet.CLOSE_CURSORS_AT_COMMIT.
     * @return java.sql.CLOSE_CURSORS_AT_COMMIT
     * @see org.sqlite.jdbc.JdbcStatement#getResultSetHoldability()
     */
    public int getResultSetHoldability() {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    /**
     * invoke sqlite3_libversion_number() function.
     * @return Returns the value of sqlite3_libversion_number() / 1000000
     */
    public int getDatabaseMajorVersion() {
        return (sqlite3_libversion_number() / 1000000);
    }

    /**
     * invoke sqlite3_libversion_number() function.
     * @return Returns the value of (sqlite3_libversion_number() % 1000000) / 1000
     */
    public int getDatabaseMinorVersion() {
        return ((sqlite3_libversion_number() % 1000000) / 1000);
    }

    /**
     * It always returns 3.
     * @return 3
     */
    public int getJDBCMajorVersion() {
        return 3;
    }

    /**
     * It always returns 0.
     * @return 0
     */
    public int getJDBCMinorVersion() {
        return 0;
    }

    /**
     * It always returns sqlStateSQL99.
     * @return java.sql.DatabaseMetaData.sqlStateSQL99
     */
    public int getSQLStateType() {
        return sqlStateSQL99;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean locatorsUpdateCopy() {
        return false;
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean supportsStatementPooling() {
        return false;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSet getSchemas(String s, String s1) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSet getFunctions(String s, String s1, String s2) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSet getFunctionColumns(String s, String s1, String s2, String s3) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    // END implements

    private ResultSet produceDetachedResultSet(String sql) throws SQLException {
        final Statement stmt = conn.createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        ((JdbcStatement) stmt).close(rs);
        return rs;
    }
    
    /**
     * execute 'PRAGMA database_list' query.
     * @return list of DatabaseList
     * @throws java.sql.SQLException
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_database_list">PRAGMA database_list;</a>
     */
    public List<DatabaseList> getDatabaseList() throws SQLException {
        final Statement stmt = conn.createStatement();
        try {
            if (!stmt.execute("PRAGMA database_list")) {
                // database not found
                return new ArrayList<DatabaseList>(0);
            }
            
            final ArrayList<DatabaseList> ret = new ArrayList<DatabaseList>();
            for (final ResultSet rs = stmt.getResultSet(); rs.next(); ) {
                int max = rs.getMetaData().getColumnCount();
                String name = rs.getMetaData().getColumnLabel(1);
                name = rs.getMetaData().getColumnLabel(2);
                name = rs.getMetaData().getColumnLabel(3);
                // seq, name, file
                ret.add(new DatabaseList(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            ret.trimToSize();
            return ret;
            
        } finally {
            stmt.close();
        }
    }
    
    /**
     * execute 'PRAGMA table_info(table-name)' query.
     * @param tableName
     * @return list of TableInfo
     * @throws java.sql.SQLException
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_table_info">PRAGMA table_info(table-name);</a>
     */
    public List<TableInfo> getTableInfo(String tableName) throws SQLException {
        final Statement stmt = conn.createStatement();
        try {
            if (!stmt.execute("PRAGMA table_info(" + tableName + ")")) {
                // table not found
//                throw new SQLException("Not found table '" + tableName + "'.", "42S02");
                return new ArrayList<TableInfo>(0);
            }
            
            final ArrayList<TableInfo> ret = new ArrayList<TableInfo>();
            for (final ResultSet rs = stmt.getResultSet(); rs.next(); ) {
                ret.add(new TableInfo(
                                // cin
                                rs.getInt(1),
                                // name
                                rs.getString(2),
                                // type
                                rs.getString(3),
                                // notnull
                                rs.getInt(4),
                                // dflt_value
                                rs.getString(5),
                                // pk
                                rs.getInt(6)
                            )
                        );
            }
            ret.trimToSize();
            return ret;
            
        } finally {
            stmt.close();
        }
    }
    
    /**
     * execute 'PRAGMA index_list(table-name)' query.
     * @param tableName
     * @return list of IndexList
     * @throws java.sql.SQLException
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_index_list">PRAGMA index_list(table-name);</a>
     */
    public List<IndexList> getIndexList(String tableName) throws SQLException {
        final Statement stmt = conn.createStatement();
        try {
            if (!stmt.execute("PRAGMA index_list(" + tableName + ")") ) {
                // index not found
                return new ArrayList<IndexList>(0);
            }
            
            final ArrayList<IndexList> ret = new ArrayList<IndexList>();
            for (final ResultSet rs = stmt.getResultSet(); rs.next(); ) {
                // seq, name, unique
                ret.add(new IndexList(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            ret.trimToSize();
            return ret;
        
        } finally {
            stmt.close();
        }
    }
    
    /**
     * execute 'PRAGMA index_info(index-name)' query.
     * @param indexName
     * @return list of IndexInfo
     * @throws java.sql.SQLException
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_index_info">PRAGMA index_info(index-name);</a>
     */
    public List<IndexInfo> getIndexInfo(String indexName) throws SQLException {
        final Statement stmt = conn.createStatement();
        try {
            if (!stmt.execute("PRAGMA index_info(" + indexName + ")") ) {
                // index not found
                return new ArrayList<IndexInfo>(0);
            }
            
            final ArrayList<IndexInfo> ret = new ArrayList<IndexInfo>();
            for (final ResultSet rs = stmt.getResultSet(); rs.next(); ) {
                // seqno, cid, name
                ret.add(new IndexInfo(rs.getInt(1), rs.getInt(2), rs.getString(3)));
            }
            ret.trimToSize();
            return ret;
            
        } finally {
            stmt.close();
        }
    }
    
    /**
     * execute 'PRAGMA foreign_key_list(table-name)' query.
     * @param tableName
     * @return list of ForeignKeyList
     * @throws java.sql.SQLException
     * @see <a href="http://www.sqlite.org/pragma.html#pragma_foreign_key_list">PRAGMA foreign_key_list(table-name);</a>
     */
    public List<ForeignKeyList> getForeignKeyList(String tableName) throws SQLException {
        final Statement stmt = conn.createStatement();
        try {
            if (!stmt.execute("PRAGMA foreign_key_list(" + tableName + ")") ) {
                // index not found
                return new ArrayList<ForeignKeyList>(0);
            }
            
            final ArrayList<ForeignKeyList> ret = new ArrayList<ForeignKeyList>();
            for (final ResultSet rs = stmt.getResultSet(); rs.next(); ) {
                // id, seq, table, from, to
                ret.add(new ForeignKeyList(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            ret.trimToSize();
            return ret;
        
        } finally {
            stmt.close();
        }
    }

    private ResultSet getForeignKeys(String parentTable, String foreignTable) throws SQLException {
        final String tableName = foreignTable.replaceAll("'", "''");
        final List<ForeignKeyList> fkl = getForeignKeyList(tableName);
        if (fkl.size() == 0) {
            // foreign key not found
            final String sql
                    = "SELECT "
                        + "  NULL AS PKTABLE_CAT"
                        + ", NULL AS PKTABLE_SCHEM"
                        + ", NULL AS PKTABLE_NAME"
                        + ", NULL AS PKCOLUMN_NAME"
                        + ", NULL AS FKTABLE_CAT"
                        + ", NULL AS FKTABLE_SCHEM"
                        + ", NULL AS FKTABLE_NAME"
                        + ", NULL AS FKCOLUMN_NAME"
                        + ", NULL AS KEY_SEQ"
                        + ", NULL AS UPDATE_RULE"
                        + ", NULL AS DELETE_RULE"
                        + ", NULL AS FK_NAME"
                        + ", NULL AS PK_NAME"
                        + ", NULL AS DEFERRABILITY"
                    + " LIMIT 0";
            return produceDetachedResultSet(sql);
        }
        
        final StringBuilder inlineView = new StringBuilder();
        for (final ForeignKeyList fk : fkl) {
            inlineView.append("SELECT ");
            inlineView.append(" '").append(fk.table).append("' AS PKTABLE_NAME");
            inlineView.append(", '").append(tableName).append("' AS FKTABLE_NAME");
            inlineView.append(", '").append(fk.to).append("' AS PKCOLUMN_NAME");
            inlineView.append(", '").append(fk.from).append("' AS FKCOLUMN_NAME");
            inlineView.append(", ").append(fk.seq + 1).append(" AS KEY_SEQ");
            inlineView.append(" UNION ALL ");
        }

        final String sql
                = "SELECT "
                    + "  NULL AS PKTABLE_CAT"
                    + ", NULL AS PKTABLE_SCHEM"
                    + ", PKTABLE_NAME"
                    + ", PKCOLUMN_NAME"
                    + ", NULL AS FKTABLE_CAT"
                    + ", NULL AS FKTABLE_SCHEM"
                    + ", FKTABLE_NAME"
                    + ", FKCOLUMN_NAME"
                    + ", KEY_SEQ"
                    + ", " + importedKeyNoAction + " AS UPDATE_RULE"
                    + ", " + importedKeyNoAction + " AS DELETE_RULE"
                    + ", NULL AS FK_NAME"
                    + ", NULL AS PK_NAME"
                    + ", " + importedKeyNotDeferrable + " AS DEFERRABILITY"
                + " FROM (" + inlineView.substring(0, inlineView.length() - 11) + ")"
                + " WHERE (?1 IS NULL OR upper(PKTABLE_NAME) = upper(?1))"
                + " ORDER BY "
                    + (parentTable == null ? "PKTABLE_NAME" : "FKTABLE_NAME")
                    + ", KEY_SEQ";
        JdbcPreparedStatement pstmt
                = (JdbcPreparedStatement) conn.prepareStatement(sql);
        pstmt.setString(1, parentTable);
        ResultSet rs = pstmt.executeQuery();
        pstmt.close(rs);
        return rs;
    }
    
    private static TableInfo searchTableInfo(List<TableInfo> tblInf, String columnName) {
        for (final TableInfo inf : tblInf) {
            if (inf.columnName.equalsIgnoreCase(columnName)) {
                return inf;
            }
        }
        return null;
    }
    
    private static boolean containsUniqueIndex(List<IndexList> ixl) {
        for (final IndexList ix : ixl) {
            if (ix.isUnique) {
                return true;
            }
        }
        return false;
    }
    
    public static int getColumnType(int columnType) {
        switch (columnType) {
            case SQLITE_INTEGER:
                return Types.INTEGER;

            case SQLITE_FLOAT:
                return Types.REAL;

            case SQLITE3_TEXT:
                return Types.VARCHAR;

            case SQLITE_BLOB:
                return Types.BLOB;

            case SQLITE_NULL:
                return Types.NULL;
        }
        Logger.getLogger(JdbcDatabaseMetaData.class.getName()).info("Unknown column type '" + columnType + "'.");
        return Types.OTHER;
    }
    
    public static String getColumnClassName(int columnType) {
        switch (columnType) {
            case SQLITE_INTEGER:
                return Integer.class.getName();
                
            case SQLITE_FLOAT:
                return Double.class.getName();
                
            case SQLITE3_TEXT:
                return String.class.getName();
                
            case SQLITE_BLOB:
                return Blob.class.getName();

            case SQLITE_NULL:
                return Object.class.getName();
        }
        Logger.getLogger(JdbcDatabaseMetaData.class.getName()).info("Unknown column type '" + columnType + "'.");
        return Object.class.getName();
    }
    
    public static int getPrecision(int columnType) {
        switch (columnType) {
            case SQLITE_INTEGER:
                return 10;

            case SQLITE_FLOAT:
                return 16;

//            case SQLite3Constants.SQLITE3_TEXT:
//            case SQLite3Constants.SQLITE_BLOB:
//            case SQLite3Constants.SQLITE_NULL:
//                return 0;
        }
        return 0;
    }
    
    public static int getScale(int columnType) {
        switch (columnType) {
            case SQLITE_FLOAT:
                // TODO REAL型の小数点以下桁数を返すようにすべきか？
//                return ?;
            case SQLITE_INTEGER:
            case SQLITE3_TEXT:
            case SQLITE_BLOB:
            case SQLITE_NULL:
                return 0;
        }
        Logger.getLogger(JdbcDatabaseMetaData.class.getName()).info("Unknown column type '" + columnType + "'.");
        return 0;
    }

    public static int getColumnDisplaySize(int columnType) {
        switch (columnType) {
            case SQLITE_INTEGER:
                return 11;  // precision + "-".length()
                
            case SQLITE_FLOAT:
                return 23;  // String.valueOf(-Double.MAX_VALUE).length()
                
            case SQLITE3_TEXT:
            case SQLITE_BLOB:
                return Integer.MAX_VALUE;

            case SQLITE_NULL:
                return 0;
        }
        Logger.getLogger(JdbcDatabaseMetaData.class.getName()).info("Unknown column type '" + columnType + "'.");
        return 0;
    }

    public static boolean isSigned(int columnType) {
        switch (columnType) {
            case SQLITE_INTEGER:
            case SQLITE_FLOAT:
                return true;
                
            case SQLITE3_TEXT:
            case SQLITE_BLOB:
            case SQLITE_NULL:
                return false;
        }
        Logger.getLogger(JdbcDatabaseMetaData.class.getName()).info("Unknown column type '" + columnType + "'.");
        return false;
    }

    public static int getRadix(int columnType) {
        switch (columnType) {
            case SQLITE_INTEGER:
            case SQLITE_FLOAT:
                return 10;
                
            case SQLITE3_TEXT:
            case SQLITE_BLOB:
            case SQLITE_NULL:
                return 0;
        }
        Logger.getLogger(JdbcDatabaseMetaData.class.getName()).info("Unknown column type '" + columnType + "'.");
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
