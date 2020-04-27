package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ConfigurationReaderTest {

   ConfigurationReader configurationReader = new ConfigurationReader();
   Properties props = new Properties();

   @Before
   public void init() {
      props.setProperty("connectingMessage", "mock-connecting-message");
      props.setProperty("connectRetryTimeoutSec", "-99");
      props.setProperty("pollPeriodMs", "-42");
   }

   @Test
   public void shouldSetStringPropertyWhenAnnotationHasNoName() {
      Configuration config = configurationReader.parseProperies(props);
      assertEquals("mock-connecting-message" ,config.getConnectingMessage());
   }

   @Test
   public void shouldSetNonStringValueIfStringSetterExists() {
      Configuration config = configurationReader.parseProperies(props);
      assertEquals(-99, config.getConnectRetryTimeoutSec());
   }

}
