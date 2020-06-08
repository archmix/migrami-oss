package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"category", "name"})
public class MigramiScript {
  private final MigramiCategory category;

  private final MigramiScriptName name;

  private final List<String> statements;

  private final MigramiChecksum checksum;

  public static MigramiScript create(MigramiCategory category, MigramiChecksumFactory checksumFactory, ResourceName resourceName, String content) {
    MigramiChecksum checksum = checksumFactory.create(content);
    MigramiScriptName name = MigramiScriptName.create(resourceName);

    return new MigramiScript(category, name, extractStatements(content), checksum);
  }

  private static List<String> extractStatements(String content) {
    List<String> statements = new ArrayList<>();

    Scanner s = new Scanner(content);
    s.useDelimiter("(;(\r)?\n)|((\r)?\n)?(--)?.*(--(\r)?\n)");
    while (s.hasNext()) {
      String line = s.next();
      if (line.startsWith("/*!") && line.endsWith("*/")) {
        int i = line.indexOf(' ');
        line = line.substring(i + 1, line.length() - " */".length());
      }

      if(line.endsWith(";")) {
        line = line.substring(0, line.length() -1);
      }

      if (line.trim().length() > 0) {
        statements.add(line);
      }
    }

    return statements;
  }

  public MigramiCategory category() {
    return category;
  }

  public List<String> statements() {
    return statements;
  }

  public MigramiChecksum checksum() {
    return checksum;
  }

  public String description() {
    return name.description();
  }

  public MigramiVersion version() {
    return name.version();
  }

  public MigramiScriptName name() {
    return name;
  }

  @Override
  public String toString() {
    return this.name().toString();
  }
}
