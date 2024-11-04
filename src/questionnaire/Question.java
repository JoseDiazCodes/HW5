package questionnaire;

public interface Question {
   String getPrompt();
   boolean isRequired();
   void answer(String response);
   String getAnswer();
   Question copy();
}
