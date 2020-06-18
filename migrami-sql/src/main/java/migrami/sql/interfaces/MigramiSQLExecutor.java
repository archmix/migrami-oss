package migrami.sql.interfaces;

import lombok.RequiredArgsConstructor;
import migrami.core.interfaces.MigramiScript;
import migrami.sql.infra.DatabaseVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "create")
class MigramiSQLExecutor {
  private final DatabaseConfiguration configuration;

  private Optional<Connection> connection;

  private Logger logger = LoggerFactory.getLogger(MigramiSQLExecutor.class);

  public void openConnection() {
    try {
      DatabaseVendor vendor = this.configuration.vendor();
      Class.forName(vendor.driver());

      Connection connection = DriverManager.getConnection(configuration.url(), configuration.user(),
        configuration.password());
      this.connection = Optional.of(connection);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public boolean exists(String table) {
    try {
      ResultSet tables =
        this.connection().getMetaData().getTables(null, null, table, new String[]{"TABLE"});
      return tables.next();
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  public void execute(MigramiScript script) {
    logger.info("Applying migration file {}", script.name());

    connection.ifPresent(connection -> {
      SQLStatements.process(script.body(), statement ->{
        this.execute(connection, statement);
      });
    });
  }

  void execute(String script) {
    connection.ifPresent(connection -> {
      this.execute(connection, script);
    });
  }

  <R> R query(String sql, Consumer<PreparedStatement> parameters,
              Function<ResultSet, R> converter) {
    Connection connection = connection();
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      parameters.accept(ps);

      ResultSet rs = ps.executeQuery();
      R value = null;
      while (rs.next()) {
        value = converter.apply(rs);
      }
      QuietCloseable.close(rs);
      return value;
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  void insert(String sql, Consumer<PreparedStatement> values) {
    Connection connection = connection();
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      values.accept(ps);

      Integer rowsAffected = ps.executeUpdate();
      if (rowsAffected != 1) {
        String message = String.format("Query %s affected %s rows", sql, rowsAffected);
        throw new IllegalStateException(message);
      }
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private void execute(Connection connection, String script) {
    Optional<Statement> statement = Optional.empty();

    try {
      statement = Optional.of(connection.createStatement());
      statement.ifPresent(it -> this.execute(it, script));
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    } finally {
      statement.ifPresent(QuietCloseable::close);
    }
  }

  private void execute(Statement statement, String script) {
    try {
      statement.setEscapeProcessing(true);

      boolean hasResults = statement.execute(script);
      while (!(!hasResults && statement.getUpdateCount() == -1)) {
        hasResults = statement.getMoreResults();
      }
    } catch (SQLException e) {
      logger.error("Error on statement execution {}", script);
      throw new IllegalStateException(e);
    }
  }

  private Connection connection() {
    return this.connection
      .orElseThrow(() -> new IllegalStateException("Connection was not opened with database"));
  }

  public void closeConnection() {
    this.connection.ifPresent(QuietCloseable::close);
  }
}
