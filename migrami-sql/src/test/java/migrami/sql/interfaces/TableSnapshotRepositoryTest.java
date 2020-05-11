package migrami.sql.interfaces;

import migrami.core.interfaces.ResourceName;
import org.junit.Assert;
import org.junit.Test;

public class TableSnapshotRepositoryTest {

    @Test
    public void givenSQLWithDefaultSnapshotTableName() {
        final ResourceName resourceName = ResourceName.create("create_snapshot_table.sql");
        final TableSnapshotRepository repository = new TableSnapshotRepository();

        final String expected = "CREATE TABLE migrami_snapshot(\n" +
            "  category    VARCHAR(100) NOT NULL,\n" +
            "  script_name  VARCHAR(100) NOT NULL,\n" +
            "  checksum    CHAR(32) NOT NULL,\n" +
            "  PRIMARY KEY (category, script_name)\n" +
            ")";

        Assert.assertEquals(expected, repository.loadScriptWithTableName(resourceName));
    }

    @Test
    public void givenSQLWithCustomSnapshotTableName() {
        final ResourceName resourceName = ResourceName.create("create_snapshot_table.sql");
        final TableSnapshotRepository repository = new TableSnapshotRepository("customer");

        final String expected = "CREATE TABLE customer_migrami_snapshot(\n" +
            "  category    VARCHAR(100) NOT NULL,\n" +
            "  script_name  VARCHAR(100) NOT NULL,\n" +
            "  checksum    CHAR(32) NOT NULL,\n" +
            "  PRIMARY KEY (category, script_name)\n" +
            ")";

        Assert.assertEquals(expected, repository.loadScriptWithTableName(resourceName));
    }

}