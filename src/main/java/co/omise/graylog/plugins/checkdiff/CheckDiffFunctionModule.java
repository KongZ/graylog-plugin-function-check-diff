package co.omise.graylog.plugins.checkdiff;

import java.util.Collections;
import java.util.Set;

import org.graylog.plugins.pipelineprocessor.ast.functions.Function;
import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

public class CheckDiffFunctionModule extends PluginModule {

   @Override
   public Set<? extends PluginConfigBean> getConfigBeans() {
      return Collections.emptySet();
   }

   @Override
   protected void configure() {
      addMessageProcessorFunction(CheckDiffFunction.NAME, CheckDiffFunction.class);
   }

   protected void addMessageProcessorFunction(String name, Class<? extends Function<?>> functionClass) {
      addMessageProcessorFunction(binder(), name, functionClass);
   }

   public static void addMessageProcessorFunction(Binder binder, String name,
         Class<? extends Function<?>> functionClass) {
      processorFunctionBinder(binder).addBinding(name).to(functionClass);

   }

   public static MapBinder<String, Function<?>> processorFunctionBinder(Binder binder) {
      return MapBinder.newMapBinder(binder, TypeLiteral.get(String.class), new TypeLiteral<Function<?>>() {
      });
   }

}
