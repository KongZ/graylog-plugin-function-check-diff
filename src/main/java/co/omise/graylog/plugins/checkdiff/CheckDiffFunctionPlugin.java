package co.omise.graylog.plugins.checkdiff;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

public class CheckDiffFunctionPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new CheckDiffFunctionMetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new CheckDiffFunctionModule());
    }
}
