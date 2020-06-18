package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import migrami.core.infra.ResourceStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
public class MigramiScriptBody {
  private final byte[] bytes;

  public static MigramiScriptBody create(InputStream inputStream) {
    return MigramiScriptBody.create(ResourceStream.readBytes(inputStream));
  }

  public String toString() {
    return new String(bytes);
  }

  public ByteArrayInputStream toInputStream() {
    return new ByteArrayInputStream(this.bytes);
  }

  public Reader toReader(){
    return new InputStreamReader(this.toInputStream());
  }
}
