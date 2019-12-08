package migrami.sql.interfaces;

import migrami.core.interfaces.MigramiCategoryScriptLoader;
import migrami.core.interfaces.MigramiChecksumFactory;
import migrami.core.interfaces.MigramiEngine;
import migrami.core.interfaces.MigramiScript;
import migrami.core.interfaces.MigramiSnapshotRepository;

class MigramiSQLEngine extends MigramiEngine {
  private final MigramiSQLExecutor sqlExecutor;

  MigramiSQLEngine(DatabaseConfiguration databaseConfiguration, MigramiCategoryScriptLoader loader,
                   MigramiChecksumFactory checksumFactory, MigramiSnapshotRepository repository) {
    super(repository, loader, checksumFactory);
    this.sqlExecutor = MigramiSQLExecutor.create(databaseConfiguration);
  }

  @Override
  protected void before() {
    this.sqlExecutor.openConnection();
    if (this.repository instanceof TableSnapshotRepository) {
      ((TableSnapshotRepository) this.repository).initialize(this.sqlExecutor);
    }
  }

  @Override
  protected void migrate(MigramiScript script) {
    this.sqlExecutor.execute(script);
  }

  @Override
  protected void after() {
    this.sqlExecutor.closeConnection();
  }
}
