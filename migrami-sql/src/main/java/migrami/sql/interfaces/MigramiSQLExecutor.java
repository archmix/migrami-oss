package migrami.sql.interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import migrami.core.interfaces.MigramiScript;
import migrami.sql.infra.DatabaseVendor;

@RequiredArgsConstructor(staticName = "create")
class MigramiSQLExecutor {
  private final DatabaseConfiguration configuration;
  
  private Optional<Connection> connection;
  
  public void openConnection() {
    try {
      DatabaseVendor vendor = this.configuration.vendor();
      Class.forName(vendor.driver());
      
      Connection connection = DriverManager.getConnection(configuration.url(), configuration.user(), configuration.password());
      this.connection = Optional.of(connection);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public void execute(MigramiScript script) {
    connection.ifPresent(connection ->{
      this.execute(connection, script.content());
    });
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
    } catch(SQLException e) {
      throw new IllegalStateException(e);
    }
  }
  
  public void closeConnection() {
    this.connection.ifPresent(QuietCloseable::close);
  }
}
