package migrami.core.interfaces;

import java.io.InputStream;
import java.security.MessageDigest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
class MD5Checksum implements MigramiChecksum {
  private final String value;

  static MD5Checksum create(InputStream content) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] buffer = new byte[content.available()];
      int bytesRead;
      while ((bytesRead = content.read(buffer)) != -1) {
        md.update(buffer, 0, bytesRead);
      }

      StringBuilder result = new StringBuilder();
      for (byte b : md.digest()) {
        result.append(String.format("%02x", b));
      }

      return new MD5Checksum(result.toString());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String value() {
    return value;
  }
}