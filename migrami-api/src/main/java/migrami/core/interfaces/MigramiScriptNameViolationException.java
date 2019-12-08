package migrami.core.interfaces;

public class MigramiScriptNameViolationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private static final String templateMessage =
    "Migrami scripts should follow the template \"V{VersionNumber}_{Description}\". ";

  public MigramiScriptNameViolationException(String message) {
    super(templateMessage + message);
  }
}
