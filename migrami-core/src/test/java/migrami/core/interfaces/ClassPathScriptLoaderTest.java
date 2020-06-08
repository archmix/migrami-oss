package migrami.core.interfaces;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ClassPathScriptLoaderTest {
  @Test
  public void givenPathAndCategoryWhenLoadThenGetScript() {
    MigramiScriptLoader loader = new ClassPathScriptLoader();
    Iterable<MigramiScript> scripts = loader.load(Category.DEFAULT, MD5ChecksumFactory.valueOf());

    AtomicInteger processed = new AtomicInteger(0);

    scripts.forEach(script -> {
      Integer index = processed.incrementAndGet();
      String sqlTemplate = "SELECT %s FROM TABLE";
      Assert.assertEquals(String.format(sqlTemplate, index), script.statements().get(0));
    });

    Assert.assertEquals(2, processed.get());
  }
}
