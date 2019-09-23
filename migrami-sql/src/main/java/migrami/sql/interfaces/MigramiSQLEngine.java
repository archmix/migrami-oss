package migrami.sql.interfaces;

import migrami.core.interfaces.MigramiCategoryScriptLoader;
import migrami.core.interfaces.MigramiEngine;
import migrami.core.interfaces.MigramiScript;
import migrami.core.interfaces.MigramiSnapshotRepository;

class MigramiSQLEngine extends MigramiEngine {
  private final MigramiSQLExecutor migramiExecutor;
  
  
  MigramiSQLEngine(DatabaseConfiguration databaseConfiguration, MigramiCategoryScriptLoader loader,
      MigramiSnapshotRepository repository) {
    super(loader, repository);
    this.migramiExecutor = MigramiSQLExecutor.create(databaseConfiguration);
  }
  
  @Override
  protected void before() {
    this.migramiExecutor.openConnection();
  }

  @Override
  protected void migrate(MigramiScript script) {
    this.migramiExecutor.execute(script);
  }
  
  @Override
  protected void after() {
    this.migramiExecutor.closeConnection();
  }
}
