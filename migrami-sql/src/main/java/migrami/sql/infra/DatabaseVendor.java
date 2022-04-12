package migrami.sql.infra;

public enum DatabaseVendor {
  CONTAINER_DATABASE("jdbc:tc:", "org.testcontainers.jdbc.ContainerDatabaseDriver"),
  DB2("jdbc:db2:", "com.ibm.db2.jcc.DB2Driver"),
  DERBY_CLIENT("jdbc:derby://", "org.apache.derby.jdbc.ClientDriver"),
  DERBY_EMBEDDED("jdbc:derby:", "org.apache.derby.jdbc.EmbeddedDriver"),
  H2("jdbc:h2:", "org.h2.Driver"),
  HSQL("jdbc:hsqldb:", "org.hsqldb.jdbcDriver"),
  SQL_LITE("jdbc:sqlite:", "org.sqlite.JDBC"),
  SQL_DROID("jdbc:sqldroid:", "org.sqldroid.SQLDroidDriver"),
  MYSQL("jdbc:mysql:", "com.mysql.cj.jdbc.Driver"),
  MARIADB("jdbc:mariadb:", "org.mariadb.jdbc.Driver"),
  MYSQL_GOOGLE("jdbc:google:", "com.mysql.jdbc.GoogleDriver"),
  ORACLE("jdbc:oracle:", "oracle.jdbc.OracleDriver"),
  POSTGRESQL("jdbc:postgresql:", "org.postgresql.Driver"),
  REDSHIFT("jdbc:redshift:", "com.amazon.redshift.jdbc41.Driver"),
  JTDS("jdbc:jtds:", "net.sourceforge.jtds.jdbc.Driver"),
  SYBASE("jdbc:sybase:", "com.sybase.jdbc4.jdbc.SybDriver"),
  SQLSERVER("jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
  SAP_HANA("jdbc:sap:", "com.sap.db.jdbc.Driver"),
  INFORMIX("jdbc:informix-sqli:", "com.informix.jdbc.IfxDriver"),
  IGNITE("jdbc:ignite", "org.apache.ignite.IgniteJdbcThinDriver");

  private String prefix;
  private String driver;

  private DatabaseVendor(String prefix, String driver) {
    this.prefix = prefix;
    this.driver = driver;
  }

  public static DatabaseVendor detectDatabase(String url) {
    for (DatabaseVendor vendor : DatabaseVendor.values()) {
      if (url.startsWith(vendor.prefix)) {
        return vendor;
      }
    }

    return null;
  }

  public String driver() {
    return driver;
  }
}
