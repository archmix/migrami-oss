package migrami.sql.interfaces;

import migrami.core.interfaces.Migrami;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MigramiSQLEngineTest {
  static final String USER = "sa";
  static final String PASSWORD = "sa";
  static final Logger LOGGER = LoggerFactory.getLogger(MigramiSQLEngineTest.class);

  @AfterClass
  public static void init(){
    try {
      Files.delete(Paths.get("./test.mv.db"));
      Files.delete(Paths.get("./test-custom.mv.db"));
    } catch(Exception e) {
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
    String url = "jdbc:h2:./test";

    Migrami migrami = MigramiSQLEngineBuilder.create()
      .withDatasource(url, USER, PASSWORD)
      .withClasspathScriptLoader()
      .withTableSnapshotRepository()
      .build();

    migrami.migrate();

    MigramiSQLExecutor sqlExecutor = this.createSQLExecutor(url);

    Assert.assertTrue(sqlExecutor.exists("PERSON"));
    Assert.assertTrue(sqlExecutor.exists("CITY"));
    Assert.assertTrue(sqlExecutor.exists("MIGRAMI_SNAPSHOT"));

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