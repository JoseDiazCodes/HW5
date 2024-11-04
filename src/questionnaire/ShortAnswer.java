package questionnaire;
class ShortAnswer extends AbstractQuestion {
  private static final int MAX_LENGTH = 280;

  public ShortAnswer(String prompt, boolean required) {
    super(prompt, required);
  }

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

  @Override
  public Question copy() {
    ShortAnswer copy = new ShortAnswer(this.prompt, this.required);
    copy.answer = this.answer;
    return copy;
  }
}