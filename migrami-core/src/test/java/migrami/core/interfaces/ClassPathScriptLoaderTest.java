package migrami.core.interfaces;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;

public class ClassPathScriptLoaderTest {
  @Test
  public void givenPathAndCategoryWhenLoadThenGetScript() {
    MigramiScriptLoader loader = new ClassPathScriptLoader();
    Iterable<MigramiScript> scripts = loader.load(Category.DEFAULT, MD5ChecksumFactory.valueOf());
    
    AtomicInteger processed = new AtomicInteger(0);
    
    scripts.forEach(script ->{
      Integer index = processed.incrementAndGet();
      String sqlTemplate = "SELECT %s FROM TABLE;";
      Assert.assertEquals(String.format(sqlTemplate, index), script.content());
    });
    
    Assert.assertEquals(2, processed.get());
  }
}
