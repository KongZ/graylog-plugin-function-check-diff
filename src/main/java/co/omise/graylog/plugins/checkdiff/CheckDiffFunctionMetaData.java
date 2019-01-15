package co.omise.graylog.plugins.checkdiff;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

public class CheckDiffFunctionMetaData implements PluginMetaData {
   private static final String PLUGIN_PROPERTIES = "co.omise.graylog.plugins.graylog-plugin-function-check-diff/graylog-plugin.properties";

   @Override
   public String getUniqueId() {
      return "co.omise.graylog.plugins.checkdiff.CheckDiffFunctionPlugin";
   }

   @Override
   public String getName() {
      return "Compare actual result with expected result from query";
   }

   @Override
   public String getAuthor() {
      return "Omise";
   }

   @Override
   public URI getURL() {
      return URI.create("https://github.com/omise/graylog-plugin-function-check-diff");
   }

   @Override
   public Version getVersion() {
      return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(1, 0, 1, "unknown"));
   }

   @Override
   public String getDescription() {
      return "Pipeline function that returns a comparison result from query";
   }

   @Override
   public Version getRequiredVersion() {
      return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(2, 1, 1));
   }

   @Override
   public Set<ServerStatus.Capability> getRequiredCapabilities() {
      return Collections.emptySet();
   }
}
