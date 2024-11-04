package questionnaire;

public class Likert extends AbstractQuestion {
  public Likert(String prompt, boolean required) {
    super(prompt, required);
  }

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

  @Override
  public Question copy() {
    Likert copy = new Likert(this.prompt, this.required);
    copy.answer = this.answer;
    return copy;
  }
}