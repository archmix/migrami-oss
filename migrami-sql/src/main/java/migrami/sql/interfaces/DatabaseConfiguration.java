package migrami.sql.interfaces;

import static walled.api.interfaces.ValueValidation.notEmpty;
import static walled.api.interfaces.ValueValidation.notNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import migrami.sql.infra.DatabaseVendor;
import walled.api.interfaces.CompositeValidation;

@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
class DatabaseConfiguration {
  private String url;

  private String user;

  private String password;

  private DatabaseVendor vendor;

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
