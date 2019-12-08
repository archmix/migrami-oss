package migrami.sql.interfaces;

import migrami.core.infra.Streams;
import migrami.core.interfaces.MigramiCategory;
import migrami.core.interfaces.MigramiCategory.MigramiCategoryAdapter;
import migrami.core.interfaces.MigramiChecksum;
import migrami.core.interfaces.MigramiScript;
import migrami.core.interfaces.MigramiScriptName;
import migrami.core.interfaces.MigramiSnapshot;
import migrami.core.interfaces.MigramiSnapshotRepository;

import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class TableSnapshotRepository implements MigramiSnapshotRepository {
  private static final String SCRIPTS_PATH =
    TableSnapshotRepository.class.getPackage().getName().replace(".", "/");
  private static final String CREATE_SNAPSHOT_TABLE = "create_snapshot_table.sql";
  private static final String SELECT_SNAPSHOT = "select_snapshot.sql";
  private static final String INSERT_SNAPSHOT = "insert_snapshot.sql";
  private MigramiSQLExecutor sqlExecutor;
  private String selectSQL;
  private String insertSQL;

  public void initialize(MigramiSQLExecutor sqlExecutor) {
    this.sqlExecutor = sqlExecutor;

    if (!sqlExecutor.exists("migrami_snapshot")) {
      String sql = this.loadScript(CREATE_SNAPSHOT_TABLE);
      sqlExecutor.execute(sql);
    }

    this.selectSQL = this.loadScript(SELECT_SNAPSHOT);
    this.insertSQL = this.loadScript(INSERT_SNAPSHOT);
  }

  @Override
  public MigramiSnapshot load(MigramiScript script) {

    MigramiSnapshot loaded = this.sqlExecutor.query(this.selectSQL, parameters -> {
      this.set(parameters, 1, script.category().name());
      this.set(parameters, 2, script.name().value());
    }, resultSet -> {
      MigramiCategory category = MigramiCategoryAdapter.create(this.value(1, resultSet), "/");
      MigramiScriptName scriptName = MigramiScriptName.create(this.value(2, resultSet));
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

  private String loadScript(String scriptName) {
    String resource = Paths.get(SCRIPTS_PATH, scriptName).toString();
    return Streams.read(resource);
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
