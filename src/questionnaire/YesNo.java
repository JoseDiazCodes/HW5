package questionnaire;

public class YesNo extends AbstractQuestion {
  public YesNo(String prompt, boolean required) {
    super(prompt, required);
  }

  @Override
  protected void validateResponse(String response) {
    if (response == null) {
      throw new IllegalArgumentException("Response cannot be null");
    }
    if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
      throw new IllegalArgumentException("Response must be yes or no");
    }
  }

  @Override
  public Question copy() {
    YesNo copy = new YesNo(this.prompt, this.required);
    copy.answer = this.answer;
    return copy;
  }
}