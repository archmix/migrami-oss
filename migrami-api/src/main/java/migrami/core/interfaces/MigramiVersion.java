package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MigramiVersion implements Comparable<MigramiVersion> {
  private final String value;

  public static MigramiVersion create(String version) {
    if (version.length() < 2) {
      throw new MigramiScriptNameViolationException("Invalid {VersionNumber} value. At least one number is required.");
    }

    return new MigramiVersion(version);
  }

  public String value() {
    return value;
  }

  @Override
  public int compareTo(MigramiVersion version) {
    return this.value.compareTo(version.value);
  }

  @Override
  public String toString() {
    return this.value;
  }
}
