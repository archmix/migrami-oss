package migrami.core.infra;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import migrami.core.interfaces.ResourceName;

import java.net.URL;

@RequiredArgsConstructor(staticName = "create", access = AccessLevel.PACKAGE)
public class ResourceResolverDecorator {
  private final URL location;
  private final ResourceResolver resolver;

  public Iterable<ResourceName> resolve() {
    return this.resolver.resolve(this.location);
  }
}
