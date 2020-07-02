package migrami.sql.interfaces;

import migrami.core.interfaces.MigramiCategory;
import migrami.core.interfaces.MigramiCategory.MigramiCategoryAdapter;
import migrami.core.interfaces.MigramiChecksum;
import migrami.core.interfaces.MigramiScript;
import migrami.core.interfaces.MigramiScriptName;
import migrami.core.interfaces.MigramiSnapshot;
import migrami.core.interfaces.MigramiSnapshotRepository;
import toolbox.resources.interfaces.ResourceName;
import toolbox.resources.interfaces.ResourcePath;
import toolbox.resources.interfaces.ResourceStream;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class TableSnapshotRepository implements MigramiSnapshotRepository {
  private static ResourcePath SCRIPTS_PATH = ResourcePath.create(TableSnapshotRepository.class.getPackage().getName().replace(".", "/"));
  private static ResourceName CREATE_SNAPSHOT_TABLE = ResourceName.create("create_snapshot_table.sql");
  private static ResourceName SELECT_SNAPSHOT = ResourceName.create("select_snapshot.sql");
  private static ResourceName INSERT_SNAPSHOT = ResourceName.create("insert_snapshot.sql");
  private static String DEFAULT_SNAPSHOT_TABLE_NAME = "migrami_snapshot";
  private MigramiSQLExecutor sqlExecutor;
  private String selectSQL;
  private String insertSQL;

  private String snapshotTableName;

  public TableSnapshotRepository() {
    this.snapshotTableName = DEFAULT_SNAPSHOT_TABLE_NAME;
  }

  public TableSnapshotRepository(String customTableName) {
    this.snapshotTableName = customTableName + "_" + DEFAULT_SNAPSHOT_TABLE_NAME;
  }

  public void initialize(MigramiSQLExecutor sqlExecutor) {
    this.sqlExecutor = sqlExecutor;
    ResourceStream stream = ResourceStream.create();

    if (!sqlExecutor.exists(this.snapshotTableName) && !sqlExecutor.exists(this.snapshotTableName.toUpperCase())) {
      String sql = loadScriptWithTableName(stream, CREATE_SNAPSHOT_TABLE);
      sqlExecutor.execute(sql);
    }

    this.selectSQL = loadScriptWithTableName(stream, SELECT_SNAPSHOT);
    this.insertSQL = loadScriptWithTableName(stream, INSERT_SNAPSHOT);
  }

  String loadScriptWithTableName(ResourceStream stream, ResourceName resourceName) {
    String sql = stream.read(SCRIPTS_PATH, resourceName).get();

    if (DEFAULT_SNAPSHOT_TABLE_NAME.equals(this.snapshotTableName)) {
      return sql;
    }

    return sql.replace(DEFAULT_SNAPSHOT_TABLE_NAME, this.snapshotTableName);
  }

  @Override
  public MigramiSnapshot load(MigramiScript script) {

    MigramiSnapshot loaded = this.sqlExecutor.query(this.selectSQL, parameters -> {
      this.set(parameters, 1, script.category().name());
      this.set(parameters, 2, script.name().value());
    }, resultSet -> {
      MigramiCategory category = MigramiCategoryAdapter.create(this.value(1, resultSet), "/");
      MigramiScriptName scriptName = MigramiScriptName.create(ResourceName.create(this.value(2, resultSet)));
      MigramiChecksum checksum = this.adapt(this.value(3, resultSet));

      return MigramiSnapshot.valueOf(category, checksum, scriptName);
    });

    if (loaded == null) {
      return MigramiSnapshot.create(script);
    }

    return loaded;
  }

  @Override
  public void save(MigramiSnapshot snapshot) {
    snapshot.visit(script -> {
      this.sqlExecutor.insert(this.insertSQL, values -> {
        this.set(values, 1, script.category().name());
        this.set(values, 2, script.name().value());
        this.set(values, 3, snapshot.checksum().value());
      });
    });
  }

  private void set(PreparedStatement ps, Integer index, String value) {
    try {
      ps.setString(index, value);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private String value(Integer index, ResultSet resultSet) {
    try {
      return resultSet.getString(index);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private MigramiChecksum adapt(String value) {
    return () -> value;
  }
}
