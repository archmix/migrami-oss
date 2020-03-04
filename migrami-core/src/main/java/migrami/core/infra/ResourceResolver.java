package migrami.core.infra;

import migrami.core.interfaces.ResourceName;

import java.net.URL;

public interface ResourceResolver {
  String FILE = "file";
  String JAR_EXTENSION = ".jar";

  boolean accept(URL location);

  Iterable<ResourceName> resolve(URL location);

  public static ResourceResolverDecorator resolver(URL location){
    ResourceResolver resolver = RegularResourceResolver.valueOf();
    if(JarResourceResolver.valueOf().accept(location)){
      resolver = JarResourceResolver.valueOf();
    }
    return ResourceResolverDecorator.create(location, resolver);
  }
}

