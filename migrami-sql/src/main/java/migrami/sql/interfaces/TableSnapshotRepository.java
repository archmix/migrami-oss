package migrami.sql.interfaces;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import migrami.core.infra.ResourceStream;
import migrami.core.interfaces.MigramiCategory;
import migrami.core.interfaces.MigramiCategory.MigramiCategoryAdapter;
import migrami.core.interfaces.MigramiChecksum;
import migrami.core.interfaces.MigramiScript;
import migrami.core.interfaces.MigramiScriptName;
import migrami.core.interfaces.MigramiSnapshot;
import migrami.core.interfaces.MigramiSnapshotRepository;
import migrami.core.interfaces.ResourceName;

class TableSnapshotRepository implements MigramiSnapshotRepository {
  private static final String SCRIPTS_PATH =
    TableSnapshotRepository.class.getPackage().getName().replace(".", "/");
  private static final ResourceName CREATE_SNAPSHOT_TABLE = ResourceName.create("create_snapshot_table.sql");
  private static final ResourceName SELECT_SNAPSHOT = ResourceName.create("select_snapshot.sql");
  private static final ResourceName INSERT_SNAPSHOT = ResourceName.create("insert_snapshot.sql");
  private static final String DEFAULT_SNAPSHOT_TABLE_NAME = "migrami_snapshot";
  private MigramiSQLExecutor sqlExecutor;
  private String selectSQL;
  private String insertSQL;

  private final String snapshotTableName;

  public TableSnapshotRepository() {
    this.snapshotTableName = DEFAULT_SNAPSHOT_TABLE_NAME;
  }

  public TableSnapshotRepository(final String customTableName) {
    this.snapshotTableName = customTableName + "_" + DEFAULT_SNAPSHOT_TABLE_NAME;
  }

  public void initialize(MigramiSQLExecutor sqlExecutor) {
    this.sqlExecutor = sqlExecutor;

    if (!sqlExecutor.exists(this.snapshotTableName) && !sqlExecutor.exists(this.snapshotTableName.toUpperCase())) {
      String sql = loadScriptWithTableName(CREATE_SNAPSHOT_TABLE);
      sqlExecutor.execute(sql);
    }

    this.selectSQL = loadScriptWithTableName(SELECT_SNAPSHOT);
    this.insertSQL = loadScriptWithTableName(INSERT_SNAPSHOT);
  }

  String loadScriptWithTableName(final ResourceName resourceName) {
      final String sql = this.loadScript(resourceName);

      if(DEFAULT_SNAPSHOT_TABLE_NAME.equals(this.snapshotTableName)) {
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

  private String loadScript(ResourceName resourceName) {
    Path path = Paths.get(SCRIPTS_PATH);
    return ResourceStream.read(path, resourceName);
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
