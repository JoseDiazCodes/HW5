package questionnaire;

/**
 * Represents a Yes/No question that can only be answered with "yes" or "no".
 * This class extends AbstractQuestion and implements specific validation for
 * binary responses. The responses are case-insensitive, meaning "YES", "yes",
 * or any combination of case is valid.
 */
public class YesNo extends AbstractQuestion {

  /**
   * Constructs a new Yes/No question.
   *
   * @param prompt the text of the question to be asked
   * @param required true if this question must be answered, false if it's optional
   * @throws IllegalArgumentException if the prompt is null or empty
   */
  public YesNo(String prompt, boolean required) {
    super(prompt, required);
  }

  /**
   * Validates that the response is either "yes" or "no" (case-insensitive).
   * For example, "YES", "yes", "No", "NO" are all valid responses.
   *
   * @param response the response to validate
   * @throws IllegalArgumentException if the response is null or is not "yes" or "no"
   *         (case-insensitive)
   */
  @Override
  protected void validateResponse(String response) {
    if (response == null) {
      throw new IllegalArgumentException("Response cannot be null");
    }
    if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
      throw new IllegalArgumentException("Response must be yes or no");
    }
  }

  /**
   * Creates and returns a deep copy of this Yes/No question.
   * The copy includes the prompt, required status, and any existing answer.
   *
   * @return a new Question instance that is a deep copy of this question
   */
  @Override
  public Question copy() {
    YesNo copy = new YesNo(this.prompt, this.required);
    copy.answer = this.answer;
    return copy;
  }
}