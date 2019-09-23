package migrami.sql.interfaces;

public class QuietCloseable {
  public static void close(AutoCloseable closeable) {
    try {
      closeable.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
