package migrami.sql.interfaces;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import migrami.core.interfaces.MigramiChecksum;
import migrami.core.interfaces.MigramiChecksumFactory;
import migrami.core.interfaces.MigramiScriptBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@RequiredArgsConstructor(staticName = "create", access = AccessLevel.PRIVATE)
public class SQLStatements {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator","\n");

  private static final String DELIMITER = ";";

  public static void process(MigramiScriptBody body, Consumer<String> consumer) {
    StringBuilder statement = new StringBuilder();

    try {
      BufferedReader lineReader = new BufferedReader(body.toReader());
      String line = null;
      while ((line = lineReader.readLine()) != null) {
        if(readyForExecution(statement, line)) {
          consumer.accept(statement.toString());
          statement.setLength(0);
          line = null;
        }
      }

      if(line != null) {
        consumer.accept(line);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error on migration file parsing", e);
    }
  }

  private static boolean readyForExecution(StringBuilder command, String line) {
    String trimmedLine = line.trim();
    if (lineIsComment(trimmedLine)) {
      return false;
    }

    if (trimmedLine.endsWith(DELIMITER)) {
      command.append(line.substring(0, line.lastIndexOf(DELIMITER)));
      return true;
    }

    command.append(line);
    command.append(LINE_SEPARATOR);
    return false;
  }

  private static boolean lineIsComment(String trimmedLine) {
    return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
  }
}
