package questionnaire;

/**
 * Represents a question in a questionnaire. Each question has a prompt (the question text),
 * can be marked as required or optional, can be answered with a string response, and can be copied.
 * Different types of questions (YesNo, ShortAnswer, Likert) implement this interface with
 * their specific answer validation rules.
 */
public interface Question {
  /**
   * Returns the text of this question (the prompt).
   *
   * @return the question prompt as a string, never null
   */
  String getPrompt();

  /**
   * Indicates whether this question must be answered.
   *
   * @return true if the question is required, false if it's optional
   */
  boolean isRequired();

  /**
   * Records an answer for this question. The format and validity of the response
   * depends on the specific type of question (YesNo, ShortAnswer, Likert).
   *
   * @param response the answer to record for this question
   * @throws IllegalArgumentException if the response is null or invalid for this question type
   */
  void answer(String response);

  /**
   * Returns the current answer to this question. If no answer has been recorded,
   * returns an empty string.
   *
   * @return the current answer as a string, or an empty string if no answer exists
   */
  String getAnswer();

  /**
   * Creates and returns a deep copy of this question, including the prompt,
   * required status, and any existing answer.
   *
   * @return a new Question instance that is a deep copy of this question
   */
  Question copy();
}