package migrami.core.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigramiLogger {
  private static final Logger logger = LoggerFactory.getLogger(MigramiLogger.class);

  public static Logger logger() {
    return logger;
  }
}