package migrami.core.infra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import migrami.core.interfaces.ResourceName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class RegularResourceResolver implements ResourceResolver {
  private static final RegularResourceResolver valueOf = new RegularResourceResolver();

  public static RegularResourceResolver valueOf() {
    return valueOf;
  }

  @Override
  public boolean accept(URL location) {
    if(location == null){
      return false;
    }

    return FILE.equals(location.getProtocol())
      && !location.getPath().endsWith(JAR_EXTENSION);
  }

  @Override
  public Iterable<ResourceName> resolve(URL location) {
    List<ResourceName> names = new ArrayList<>();
    try (InputStream input = location.openConnection().getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

      String resourceName = null;
      while ((resourceName = reader.readLine()) != null) {
        names.add(ResourceName.create(resourceName));
      }
    } catch (IOException e){
      throw new IllegalStateException(e);
    }

    return names;
  }
}
