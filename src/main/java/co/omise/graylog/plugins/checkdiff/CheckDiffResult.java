/**
 * 
 */
package co.omise.graylog.plugins.checkdiff;

/**
 * A result of check diff function.
 * 
 * @author KongZ
 */
public class CheckDiffResult {
   private String expectedResult;
   private String actualResult;

   /**
    * @param expectedResult
    * @param actualReslt
    */
   public CheckDiffResult(String expectedResult, String actualReslt) {
      this.expectedResult = expectedResult;
      this.actualResult = actualReslt;
   }

   /**
    * Gets expectedResult.
    * 
    * @return the expectedResult
    */
   public String getExpectedResult() {
      return expectedResult;
   }

   /**
    * Gets actualResult.
    * 
    * @return the actualResult
    */
   public String getActualResult() {
      return actualResult;
   }

   /**
    * Return true if expected result matches with actual result.
    * 
    * @return true if expected result matches with actual result.
    */
   public boolean isMatches() {
      if (expectedResult == null) {
         if (actualResult == null)
            return true;
         return false;
      }
      return expectedResult.equals(actualResult);
   }

   /**
    * Return true if actual result exists and does not matches with the expected result. Always return false when actual result does not exists.
    * 
    * @return true if actual result exists and does not matches with the expected result
    */
   public boolean isConflict() {
      if (actualResult == null || "".equals(actualResult))
         return false;
      return !actualResult.equals(expectedResult);
   }

}
