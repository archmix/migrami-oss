package migrami.sql.interfaces;

import migrami.sql.infra.DatabaseVendor;
import toolbox.validation.interfaces.CompositeValidation;

import static toolbox.validation.interfaces.ValueValidation.*;

class DatabaseConfiguration {
  private final String url;

  private final String user;

  private final String password;

  private final DatabaseVendor vendor;

  private DatabaseConfiguration(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
    this.vendor = DatabaseVendor.detectDatabase(url);
  }

  public static DatabaseConfiguration create(String url, String user, String password){
    return new DatabaseConfiguration(url, user, password);
  }

  public String password() {
    return password;
  }

  public String url() {
    return url;
  }

  public String user() {
    return user;
  }

  public DatabaseVendor vendor() {
    return vendor;
  }

  public void validate() {
    CompositeValidation.create().add(notEmpty(this.url).withMessage("Invalid database url"))
      .add(notEmpty(this.user).withMessage("Invalid database user"))
      .add(notNull(this.vendor).withMessage("Database is not supported yet")).validate();
  }
}
