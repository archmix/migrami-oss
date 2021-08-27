package migrami.sql.interfaces;

import migrami.core.interfaces.Migrami;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class MigramiSQLEngineTest {
  static final String USER = "sa";
  static final String PASSWORD = "sa";
  static final Logger LOGGER = LoggerFactory.getLogger(MigramiSQLEngineTest.class);

  @AfterClass
  public static void init() {
    try {
      Files.delete(Paths.get("./test.mv.db"));
      Files.delete(Paths.get("./test-custom.mv.db"));
    } catch (Exception e) {
      LOGGER.warn("File deletion error", e);
    }
  }

  private MigramiSQLExecutor createSQLExecutor(String url) {
    DatabaseConfiguration configuration = DatabaseConfiguration.create(url, USER, PASSWORD);
    MigramiSQLExecutor sqlExecutor = MigramiSQLExecutor.create(configuration);
    sqlExecutor.openConnection();
    return sqlExecutor;
  }

  @Test
  public void givenSQLEngineWhenMigrateThenApplyScripts() {
    final String url = "jdbc:h2:./test";

    final Migrami migrami = MigramiSQLEngineBuilder.create()
      .withDatasource(url, USER, PASSWORD)
      .withClasspathScriptLoader()
      .withTableSnapshotRepository()
      .build();

    migrami.migrate();

    final MigramiSQLExecutor sqlExecutor = this.createSQLExecutor(url);

    Assert.assertTrue(sqlExecutor.exists("PERSON"));
    Assert.assertTrue(sqlExecutor.exists("CITY"));
    Assert.assertTrue(sqlExecutor.exists("MIGRAMI_SNAPSHOT"));

    final int countRowLock = sqlExecutor.query("select count(*) from MIGRAMI_SNAPSHOT where category = ? and script_name = ?",
      parameters -> {
        try {
          parameters.setString(1, "lock");
          parameters.setString(2, "MIGRATION_LOCK");
        } catch (SQLException ignored) {
        }
      },
      resultSet -> {
        try {
          return resultSet.getInt(1);
        } catch (SQLException ignored) {
          return 0;
        }
      });

    Assert.assertEquals(0, countRowLock);

    sqlExecutor.closeConnection();
  }

  @Test
  public void givenSQLEngineAsCustomTableNameWhenMigrateThenApplyScripts() {
    String url = "jdbc:h2:./test-custom";

    Migrami migrami = MigramiSQLEngineBuilder.create()
      .withDatasource(url, USER, PASSWORD)
      .withClasspathScriptLoader()
      .withTableSnapshotRepository("custom")
      .build();

    migrami.migrate();

    MigramiSQLExecutor sqlExecutor = this.createSQLExecutor(url);

    Assert.assertTrue(sqlExecutor.exists("PERSON"));
    Assert.assertTrue(sqlExecutor.exists("CITY"));
    Assert.assertTrue(sqlExecutor.exists("CUSTOM_MIGRAMI_SNAPSHOT"));

    sqlExecutor.closeConnection();
  }
}