package questionnaire;

abstract class AbstractQuestion implements Question {
  protected final String prompt;
  protected final boolean required;
  protected String answer;

  protected AbstractQuestion(String prompt, boolean required)
  throws IllegalArgumentException {
    if (prompt == null || prompt.isEmpty()) {
      throw new IllegalArgumentException("Prompt cannot be null or empty");
    }
    this.prompt = prompt;
    this.required = required;
    this.answer = "";
  }

  @Override
  public String getPrompt() {
    return prompt;
  }

  @Override
  public boolean isRequired() {
    return required;
  }

  @Override
  public String getAnswer() {
    return answer;
  }

  // Template method for answer validation
  @Override
  public void answer(String response) {
    validateResponse(response);
    this.answer = response;
  }

  // Abstract method for specific validation logic
  protected abstract void validateResponse(String response);
}
