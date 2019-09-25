package migrami.sql.interfaces;

import lombok.NoArgsConstructor;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiBuilder;
import migrami.core.interfaces.MigramiChecksumFactory;
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
  
  @Override
  protected void validate() {
    this.configuration.validate();
    super.validate();
  }
  
  public Migrami build() {
    this.validate();
    MigramiSnapshotRepository snapshotRepository = repository.get();
    MigramiChecksumFactory checksumFactory = this.checksumFactory.get();
    
    return new MigramiSQLEngine(this.configuration, this.scriptLoaders, checksumFactory, snapshotRepository);
  }
}
