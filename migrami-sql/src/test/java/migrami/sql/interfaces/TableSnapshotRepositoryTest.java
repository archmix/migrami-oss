package migrami.sql.interfaces;

import org.junit.Assert;
import org.junit.Test;
import toolbox.resources.interfaces.ResourceName;
import toolbox.resources.interfaces.ResourceStream;

public class TableSnapshotRepositoryTest {

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
}