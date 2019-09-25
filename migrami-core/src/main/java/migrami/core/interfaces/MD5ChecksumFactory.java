package migrami.core.interfaces;

class MD5ChecksumFactory implements MigramiChecksumFactory {
  private static final MD5ChecksumFactory valueOf = new MD5ChecksumFactory();
  
  public static MD5ChecksumFactory valueOf() {
    return valueOf;
  }
  
  @Override
  public MigramiChecksum create(String content) {
    return MD5Checksum.create(content);
  }
}
