package questionnaire;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class LikertTest {
  private Likert question;

  @Before
  public void setUp() {
    question = new Likert("The software is easy to use.", true);
  }

  @Test
  public void testConstructor() {
    assertEquals("The software is easy to use.", question.getPrompt());
    assertTrue(question.isRequired());
    assertEquals("", question.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPrompt() {
    new Likert(null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyPrompt() {
    new Likert("", true);
  }

  @Test
  public void testAllValidAnswers() {
    String[] validResponses = {
            "Strongly Agree",
            "Agree",
            "Neither Agree nor Disagree",
            "Disagree",
            "Strongly Disagree"
    };

    for (String response : validResponses) {
      question.answer(response);
      assertEquals(response, question.getAnswer());
    }
  }

  @Test
  public void testCaseInsensitiveAnswers() {
    // Test lowercase
    question.answer("strongly agree");
    assertEquals("strongly agree", question.getAnswer());

    // Test uppercase
    question.answer("STRONGLY DISAGREE");
    assertEquals("STRONGLY DISAGREE", question.getAnswer());

    // Test mixed case
    question.answer("nEiThEr AgReE nOr DiSaGrEe");
    assertEquals("nEiThEr AgReE nOr DiSaGrEe", question.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnswer() {
    question.answer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidAnswer() {
    question.answer("Kind of agree");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPartialValidAnswer() {
    question.answer("Agree nor Disagree");  // Part of a valid answer but not complete
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyAnswer() {
    question.answer("");
  }

  @Test
  public void testCopy() {
    question.answer("Agree");
    Question copy = question.copy();

    assertEquals(question.getPrompt(), copy.getPrompt());
    assertEquals(question.isRequired(), copy.isRequired());
    assertEquals(question.getAnswer(), copy.getAnswer());
    assertNotSame(question, copy);

    // Verify changes to copy don't affect original
    ((Likert)copy).answer("Disagree");
    assertEquals("Agree", question.getAnswer());
    assertEquals("Disagree", copy.getAnswer());
  }
}