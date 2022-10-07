package migrami.sql.interfaces;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import toolbox.resources.interfaces.ResourceName;
import toolbox.resources.interfaces.ResourceStream;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class TableSnapshotRepositoryTest {

  @AfterClass
  public static void cleanUp() {
    try {
      Files.delete(Paths.get("./test.mv.db"));
      Files.delete(Paths.get("./test.trace.db"));
    } catch (Exception ignored) {
    }
  }

  @Test
  public void givenSQLWithDefaultSnapshotTableName() {
    final ResourceName resourceName = ResourceName.create("create_snapshot_table.sql");
    final ResourceStream stream = ResourceStream.create();
    final TableSnapshotRepository repository = new TableSnapshotRepository();

    final String expected = "CREATE TABLE migrami_snapshot(\n" +
      "  category    VARCHAR(100) NOT NULL,\n" +
      "  script_name  VARCHAR(100) NOT NULL,\n" +
      "  checksum    CHAR(32) NOT NULL,\n" +
      "  PRIMARY KEY (category, script_name)\n" +
      ")";

    Assert.assertEquals(expected, repository.loadScriptWithTableName(stream, resourceName));
  }

  @Test
  public void givenSQLWithCustomSnapshotTableName() {
    final ResourceName resourceName = ResourceName.create("create_snapshot_table.sql");
    final ResourceStream stream = ResourceStream.create();
    final TableSnapshotRepository repository = new TableSnapshotRepository("custom");

    final String expected = "CREATE TABLE custom_migrami_snapshot(\n" +
      "  category    VARCHAR(100) NOT NULL,\n" +
      "  script_name  VARCHAR(100) NOT NULL,\n" +
      "  checksum    CHAR(32) NOT NULL,\n" +
      "  PRIMARY KEY (category, script_name)\n" +
      ")";

    Assert.assertEquals(expected, repository.loadScriptWithTableName(stream, resourceName));
  }

  @Test
  public void shouldLockSnapshotTable() {
    final MigramiSQLExecutor sqlExecutor = this.createSQLExecutor();
    dropSnapshotTable(sqlExecutor);

    final TableSnapshotRepository repository = new TableSnapshotRepository();

    repository.initialize(sqlExecutor);

    Assert.assertTrue(sqlExecutor.exists("MIGRAMI_SNAPSHOT"));

    final int countRowLock = countLock(sqlExecutor);

    Assert.assertEquals(1, countRowLock);

    sqlExecutor.closeConnection();
  }

  @Test
  public void shouldUnlockLockSnapshotTable() {
    final MigramiSQLExecutor sqlExecutor = this.createSQLExecutor();
    dropSnapshotTable(sqlExecutor);

    final TableSnapshotRepository repository = new TableSnapshotRepository();

    repository.initialize(sqlExecutor);

    Assert.assertTrue(sqlExecutor.exists("MIGRAMI_SNAPSHOT"));

    int countRowLock = countLock(sqlExecutor);

    Assert.assertEquals(1, countRowLock);

    repository.unlockSnapshotTable();

    countRowLock = countLock(sqlExecutor);

    Assert.assertEquals(0, countRowLock);

    sqlExecutor.closeConnection();
  }

  @Test
  public void shouldThrowExceptionWhenCantLockSnapshotTable() {
    final MigramiSQLExecutor sqlExecutor = this.createSQLExecutor();
    dropSnapshotTable(sqlExecutor);

    final TableSnapshotRepository repositoryWithLock = new TableSnapshotRepository();

    repositoryWithLock.initialize(sqlExecutor);

    Assert.assertTrue(sqlExecutor.exists("MIGRAMI_SNAPSHOT"));

    final TableSnapshotRepository repository = new TableSnapshotRepository();

    try {
      repository.initialize(sqlExecutor);
      Assert.fail();
    } catch (IllegalStateException e) {
      Assert.assertEquals("Unable to lock table migrami_snapshot after 10 attempts. Stopping migration.", e.getMessage());
    }
  }

  private MigramiSQLExecutor createSQLExecutor() {
    final DatabaseConfiguration configuration = DatabaseConfiguration.create("jdbc:h2:./test", "sa", "sa");
    final MigramiSQLExecutor sqlExecutor = MigramiSQLExecutor.create(configuration);
    sqlExecutor.openConnection();
    return sqlExecutor;
  }

  private void dropSnapshotTable(MigramiSQLExecutor sqlExecutor) {
    try {
      sqlExecutor.execute("drop table MIGRAMI_SNAPSHOT");
    } catch (Exception ignored) {
    }
  }

  private Integer countLock(MigramiSQLExecutor sqlExecutor) {
    return sqlExecutor.query("select count(*) from MIGRAMI_SNAPSHOT where category = ? and script_name = ?",
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
  }
}