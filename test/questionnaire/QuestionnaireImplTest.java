package questionnaire;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import java.util.NoSuchElementException;

public class QuestionnaireImplTest {
  private QuestionnaireImpl questionnaire;
  private YesNo yesNo;
  private ShortAnswer shortAnswer;
  private Likert likert;

  @Before
  public void setUp() {
    questionnaire = new QuestionnaireImpl();
    yesNo = new YesNo("Question 1?", true);
    shortAnswer = new ShortAnswer("Question 2?", false);
    likert = new Likert("Question 3?", true);
  }

  // AddQuestion tests
  @Test
  public void testAddQuestion() {
    questionnaire.addQuestion("q1", yesNo);
    assertEquals(yesNo, questionnaire.getQuestion("q1"));
    assertEquals(yesNo, questionnaire.getQuestion(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddQuestionNullId() {
    questionnaire.addQuestion(null, yesNo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddQuestionEmptyId() {
    questionnaire.addQuestion("", yesNo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateId() {
    questionnaire.addQuestion("q1", yesNo);
    questionnaire.addQuestion("q1", shortAnswer);
  }

  // RemoveQuestion tests
  @Test
  public void testRemoveQuestion() {
    questionnaire.addQuestion("q1", yesNo);
    questionnaire.addQuestion("q2", shortAnswer);
    questionnaire.addQuestion("q3", likert);

    questionnaire.removeQuestion("q2");

    // Verify q2 is removed
    assertThrows(NoSuchElementException.class, () -> questionnaire.getQuestion("q2"));

    // Verify order is maintained
    assertEquals(yesNo, questionnaire.getQuestion(1));
    assertEquals(likert, questionnaire.getQuestion(2));

    // Verify size is updated
    assertThrows(IndexOutOfBoundsException.class, () -> questionnaire.getQuestion(3));
  }

  @Test(expected = NoSuchElementException.class)
  public void testRemoveNonexistentQuestion() {
    questionnaire.removeQuestion("nonexistent");
  }

  // GetQuestion tests
  @Test
  public void testGetQuestionByNumber() {
    questionnaire.addQuestion("q1", yesNo);
    questionnaire.addQuestion("q2", shortAnswer);

    assertEquals(yesNo, questionnaire.getQuestion(1));
    assertEquals(shortAnswer, questionnaire.getQuestion(2));
  }

  @Test
  public void testGetQuestionById() {
    questionnaire.addQuestion("q1", yesNo);
    assertEquals(yesNo, questionnaire.getQuestion("q1"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetQuestionInvalidNumber() {
    questionnaire.getQuestion(1); // Empty questionnaire
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetQuestionInvalidId() {
    questionnaire.getQuestion("nonexistent");
  }

  // Required/Optional Questions tests
  @Test
  public void testGetRequiredQuestions() {
    questionnaire.addQuestion("q1", yesNo);      // required
    questionnaire.addQuestion("q2", shortAnswer); // optional
    questionnaire.addQuestion("q3", likert);      // required

    List<Question> required = questionnaire.getRequiredQuestions();
    assertEquals(2, required.size());
    assertTrue(required.contains(yesNo));
    assertTrue(required.contains(likert));
  }

  @Test
  public void testGetOptionalQuestions() {
    questionnaire.addQuestion("q1", yesNo);      // required
    questionnaire.addQuestion("q2", shortAnswer); // optional
    questionnaire.addQuestion("q3", likert);      // required

    List<Question> optional = questionnaire.getOptionalQuestions();
    assertEquals(1, optional.size());
    assertTrue(optional.contains(shortAnswer));
  }

  // IsComplete tests
  @Test
  public void testIsComplete() {
    questionnaire.addQuestion("q1", yesNo);      // required
    questionnaire.addQuestion("q2", shortAnswer); // optional

    // Initially not complete
    assertFalse(questionnaire.isComplete());

    // Answer required question
    yesNo.answer("yes");
    assertTrue(questionnaire.isComplete());

    // Optional question doesn't affect completeness
    shortAnswer.answer("answer");
    assertTrue(questionnaire.isComplete());
  }

  // Filter tests
  @Test
  public void testFilter() {
    questionnaire.addQuestion("q1", yesNo);
    questionnaire.addQuestion("q2", shortAnswer);

    Questionnaire filtered = questionnaire.filter(Question::isRequired);
    assertEquals(1, filtered.getRequiredQuestions().size());
    assertEquals(yesNo.getPrompt(), filtered.getQuestion(1).getPrompt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFilterNull() {
    questionnaire.filter(null);
  }

  // Sort tests
  @Test
  public void testSort() {
    questionnaire.addQuestion("q3", new YesNo("Z question?", true));
    questionnaire.addQuestion("q1", new YesNo("A question?", true));
    questionnaire.addQuestion("q2", new YesNo("M question?", true));

    questionnaire.sort((q1, q2) -> q1.getPrompt().compareTo(q2.getPrompt()));

    assertEquals("A question?", questionnaire.getQuestion(1).getPrompt());
    assertEquals("M question?", questionnaire.getQuestion(2).getPrompt());
    assertEquals("Z question?", questionnaire.getQuestion(3).getPrompt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortNull() {
    questionnaire.sort(null);
  }

  // ToString tests
  @Test
  public void testToStringEmpty() {
    assertEquals("", questionnaire.toString());
  }

  @Test
  public void testToStringSingleQuestion() {
    questionnaire.addQuestion("q1", yesNo);
    yesNo.answer("yes");

    String expected = "Question: Question 1?\n\nAnswer: yes";
    assertEquals(expected, questionnaire.toString());
  }

  @Test
  public void testToStringMultipleQuestions() {
    questionnaire.addQuestion("q1", yesNo);
    questionnaire.addQuestion("q2", shortAnswer);
    yesNo.answer("yes");
    shortAnswer.answer("test");

    String expected = "Question: Question 1?\n\nAnswer: yes\n\n"
            + "Question: Question 2?\n\nAnswer: test";
    assertEquals(expected, questionnaire.toString());
  }
}