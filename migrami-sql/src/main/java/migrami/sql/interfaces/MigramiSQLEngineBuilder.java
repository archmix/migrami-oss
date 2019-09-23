package migrami.sql.interfaces;

import lombok.NoArgsConstructor;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiBuilder;
import migrami.core.interfaces.MigramiSnapshotRepository;

@NoArgsConstructor(staticName = "create")
public class MigramiSQLEngineBuilder extends MigramiBuilder<MigramiSQLEngineBuilder> {
  private DatabaseConfiguration configuration;
  
  public MigramiSQLEngineBuilder withDatasource(String url, String username, String password) {
    this.configuration = new DatabaseConfiguration();
    this.configuration.setPassword(password);
    this.configuration.setUrl(url);
    this.configuration.setUser(username);
    return this;
  }
  
  public Migrami build() {
    this.configuration.validate();
    
    MigramiSnapshotRepository snapshotRepository = repository.orElseThrow(() -> new IllegalStateException("Snapshot repository cannot be null, it is mandatory. Do implement your own or use the built-in through Builder class"));
    
    return new MigramiSQLEngine(this.configuration, scriptLoaders, snapshotRepository);
  }
}
