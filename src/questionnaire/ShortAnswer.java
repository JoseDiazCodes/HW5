package questionnaire;

/**
 * Represents a short answer question that accepts text responses up to 280 characters.
 * This class extends AbstractQuestion and implements specific validation for
 * short text responses. The response length is strictly enforced to ensure
 * answers remain concise.
 */
public class ShortAnswer extends AbstractQuestion {
  /**
   * The maximum number of characters allowed in a response.
   * Chosen to match common short-form text limits (e.g., Twitter's original limit).
   */
  private static final int MAX_LENGTH = 280;

  /**
   * Constructs a new short answer question.
   *
   * @param prompt the text of the question to be asked
   * @param required true if this question must be answered, false if it's optional
   * @throws IllegalArgumentException if the prompt is null or empty
   */
  public ShortAnswer(String prompt, boolean required) {
    super(prompt, required);
  }

  /**
   * Validates that the response meets the short answer requirements.
   * The response must not be null and must not exceed MAX_LENGTH characters.
   *
   * @param response the response to validate
   * @throws IllegalArgumentException if the response is null or exceeds MAX_LENGTH characters
   */
  @Override
  protected void validateResponse(String response) {
    if (response == null) {
      throw new IllegalArgumentException("Response cannot be null");
    }
    if (response.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
              "Response cannot be longer than " + MAX_LENGTH + " characters");
    }
  }

  /**
   * Creates and returns a deep copy of this short answer question.
   * The copy includes the prompt, required status, and any existing answer.
   *
   * @return a new Question instance that is a deep copy of this question
   */
  @Override
  public Question copy() {
    ShortAnswer copy = new ShortAnswer(this.prompt, this.required);
    copy.answer = this.answer;
    return copy;
  }
}