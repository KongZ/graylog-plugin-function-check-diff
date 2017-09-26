package co.omise.graylog.plugins.checkdiff;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.graylog.plugins.pipelineprocessor.EvaluationContext;
import org.graylog.plugins.pipelineprocessor.ast.expressions.Expression;
import org.graylog.plugins.pipelineprocessor.ast.functions.Function;
import org.graylog.plugins.pipelineprocessor.ast.functions.FunctionArgs;
import org.graylog.plugins.pipelineprocessor.ast.functions.FunctionDescriptor;
import org.graylog.plugins.pipelineprocessor.ast.functions.ParameterDescriptor;
import org.graylog2.indexer.results.ResultMessage;
import org.graylog2.indexer.results.SearchResult;
import org.graylog2.indexer.searches.Searches;
import org.graylog2.indexer.searches.SearchesConfig;
import org.graylog2.indexer.searches.Sorting;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.indexer.searches.timeranges.AbsoluteRange;
import org.graylog2.plugin.indexer.searches.timeranges.TimeRange;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * {@literal check_diff} function.
 * 
 * @author KongZ
 */
public class CheckDiffFunction implements Function<CheckDiffResult> {

   public static final String NAME = "check_diff";
   public static Logger logger = LoggerFactory.getLogger(CheckDiffFunction.class);

   private Searches searches;

   private final ParameterDescriptor<String, String> streamIdParam = ParameterDescriptor.string("stream_id")
         .description("Query will be build based on this stream id").build();

   private final ParameterDescriptor<String, String> queryParam = ParameterDescriptor.string("query")
         .description("The query keyword to retreive a message to compare").build();

   private final ParameterDescriptor<String, String> expectedResultParam = ParameterDescriptor.string("expected_result")
         .description("The expected result of query").build();

   private final ParameterDescriptor<DateTime, DateTime> fromParam = ParameterDescriptor.type("from", DateTime.class)
         .description("Filter results from date").build();

   private final ParameterDescriptor<DateTime, DateTime> toParam = ParameterDescriptor.type("to", DateTime.class)
         .description("Filter results until date").build();

   @Inject
   public CheckDiffFunction(Searches searches) {
      this.searches = searches;
   }

   @Override
   public Object preComputeConstantArgument(FunctionArgs functionArgs, String s, Expression expression) {
      return expression.evaluateUnsafe(EvaluationContext.emptyContext());
   }

   @Override
   public CheckDiffResult evaluate(FunctionArgs functionArgs, EvaluationContext evaluationContext) {
      String streamId = streamIdParam.required(functionArgs, evaluationContext);
      String query = queryParam.required(functionArgs, evaluationContext);
      String expectedResult = expectedResultParam.required(functionArgs, evaluationContext);
      DateTime from = fromParam.required(functionArgs, evaluationContext);
      DateTime to = toParam.required(functionArgs, evaluationContext);
      String actualResult = "";
      if (query != null) {
         final TimeRange timeRange = AbsoluteRange.create(from, to);
         final SearchesConfig searchesConfig = SearchesConfig.builder() //
               .query(query) //
               .filter("streams:" + streamId) //
               .fields(Arrays.stream(new String[] { Message.FIELD_TIMESTAMP, Message.FIELD_MESSAGE })
                     .collect(Collectors.toList())) //
               .range(timeRange) //
               .limit(1) //
               .offset(0) //
               .sorting(new Sorting(Message.FIELD_TIMESTAMP, Sorting.Direction.DESC)).build();
         SearchResult searchResult = searches.search(searchesConfig);
         if (searchResult.getTotalResults() > 0) {
            ResultMessage resultMessage = searchResult.getResults().get(0);
            Message message = resultMessage.getMessage();
            actualResult = message.getMessage();
         }
      }
      return new CheckDiffResult(expectedResult, actualResult);
   }

   @Override
   public FunctionDescriptor<CheckDiffResult> descriptor() {
      return FunctionDescriptor.<CheckDiffResult>builder().name(NAME)
            .description("Returns <return>.matches = true if actual result matches with expected result")
            .params(streamIdParam, queryParam, expectedResultParam, fromParam, toParam)
            .returnType(CheckDiffResult.class).build();
   }

}
