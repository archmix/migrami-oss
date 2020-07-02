package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static toolbox.resources.interfaces.ResourceFactory.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
public class MigramiScriptBody {
  private final byte[] bytes;

  public static MigramiScriptBody create(InputStream inputStream) {
    return MigramiScriptBody.create(readBytes(inputStream));
  }

  public String toString() {
    return new String(bytes);
  }

  public ByteArrayInputStream toInputStream() {
    return new ByteArrayInputStream(this.bytes);
  }

  public Reader toReader() {
    return new InputStreamReader(this.toInputStream());
  }
}
