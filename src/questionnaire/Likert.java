package questionnaire;

/**
 * Represents a Likert scale question that can be answered using predefined response options:
 * Strongly Agree, Agree, Neither Agree nor Disagree, Disagree, or Strongly Disagree.
 * This class extends AbstractQuestion and provides specific validation for Likert scale responses.
 */
public class Likert extends AbstractQuestion {

  /**
   * Constructs a new Likert scale question.
   *
   * @param prompt the text of the question to be asked
   * @param required true if this question must be answered, false if it's optional
   * @throws IllegalArgumentException if the prompt is null or empty
   */
  public Likert(String prompt, boolean required) {
    super(prompt, required);
  }

  /**
   * Validates that the response is one of the valid Likert scale options.
   * Valid options are case-insensitive and must match exactly one of:
   * "Strongly Agree", "Agree", "Neither Agree nor Disagree", "Disagree", or "Strongly Disagree".
   *
   * @param response the response to validate
   * @throws IllegalArgumentException if the response is null or not a valid Likert scale option
   */
  @Override
  protected void validateResponse(String response) {
    if (response == null) {
      throw new IllegalArgumentException("Response cannot be null");
    }

    boolean validResponse = false;
    for (LikertResponseOption option : LikertResponseOption.values()) {
      if (option.getText().equalsIgnoreCase(response)) {
        validResponse = true;
        break;
      }
    }

    if (!validResponse) {
      throw new IllegalArgumentException("Invalid Likert answer");
    }
  }

  /**
   * Creates and returns a deep copy of this Likert question.
   * The copy includes the prompt, required status, and any existing answer.
   *
   * @return a new Question instance with the same state as this question
   */
  @Override
  public Question copy() {
    Likert copy = new Likert(this.prompt, this.required);
    copy.answer = this.answer;
    return copy;
  }
}