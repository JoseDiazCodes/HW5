package questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Likert question implementation.
 * Tests validate the specific behavior of Likert scale questions,
 * including valid responses, case insensitivity, and error conditions.
 */
public class LikertTest {
  private Likert question;

  /**
   * Sets up a Likert question for testing.
   * Creates a required question with a standard prompt.
   */
  @Before
  public void setUp() {
    question = new Likert("The software is easy to use.", true);
  }

  /**
   * Tests that a Likert question is properly constructed with
   * the correct prompt, required status, and initial empty answer.
   */
  @Test
  public void testConstructor() {
    assertEquals("The software is easy to use.", question.getPrompt());
    assertTrue(question.isRequired());
    assertEquals("", question.getAnswer());
  }

  /**
   * Tests that construction fails when prompt is null.
   *
   * @throws IllegalArgumentException expected when prompt is null
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullPrompt() {
    new Likert(null, true);
  }

  /**
   * Tests that construction fails when prompt is empty.
   *
   * @throws IllegalArgumentException expected when prompt is empty
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyPrompt() {
    new Likert("", true);
  }

  /**
   * Tests that all valid Likert scale responses are accepted.
   * Verifies each standard response option can be set and retrieved.
   */
  @Test
  public void testAllValidAnswers() {
    String[] validResponses = { "Strongly Agree", "Agree", "Neither Agree nor Disagree",
                                "Disagree",
                                "Strongly Disagree"
    };

    for (String response : validResponses) {
      question.answer(response);
      assertEquals(response, question.getAnswer());
    }
  }

  /**
   * Tests that Likert responses are case-insensitive.
   * Verifies that responses in different cases are accepted
   * and preserved in their original form.
   */
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

  /**
   * Tests that answering with null throws an exception.
   *
   * @throws IllegalArgumentException expected when answer is null
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullAnswer() {
    question.answer(null);
  }

  /**
   * Tests that invalid answer text throws an exception.
   *
   * @throws IllegalArgumentException expected when answer is not a valid Likert response
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidAnswer() {
    question.answer("Kind of agree");
  }

  /**
   * Tests that partial matches of valid answers are rejected.
   *
   * @throws IllegalArgumentException expected when answer is only part of a valid response
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPartialValidAnswer() {
    question.answer("Agree nor Disagree");  // Part of a valid answer but not complete
  }

  /**
   * Tests that empty answer string throws an exception.
   *
   * @throws IllegalArgumentException expected when answer is empty
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyAnswer() {
    question.answer("");
  }

  /**
   * Tests deep copy functionality of Likert questions.
   * Verifies that copies are independent and maintain all properties.
   */
  @Test
  public void testCopy() {
    question.answer("Agree");
    Question copy = question.copy();

    assertEquals(question.getPrompt(), copy.getPrompt());
    assertEquals(question.isRequired(), copy.isRequired());
    assertEquals(question.getAnswer(), copy.getAnswer());
    assertNotSame(question, copy);

    // Verify changes to copy don't affect original
    ((Likert) copy).answer("Disagree");
    assertEquals("Agree", question.getAnswer());
    assertEquals("Disagree", copy.getAnswer());
  }
}