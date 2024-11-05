package questionnaire;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class ShortAnswerTest {
  private ShortAnswer question;
  private static final int MAX_LENGTH = 280;

  @Before
  public void setUp() {
    question = new ShortAnswer("Explain your reasoning:", false);
  }

  @Test
  public void testConstructor() {
    assertEquals("Explain your reasoning:", question.getPrompt());
    assertFalse(question.isRequired());
    assertEquals("", question.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPrompt() {
    new ShortAnswer(null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyPrompt() {
    new ShortAnswer("", false);
  }

  @Test
  public void testValidAnswer() {
    String validAnswer = "This is a valid answer.";
    question.answer(validAnswer);
    assertEquals(validAnswer, question.getAnswer());
  }

  @Test
  public void testMaxLengthAnswer() {
    StringBuilder maxLength = new StringBuilder();
    for (int i = 0; i < MAX_LENGTH; i++) {
      maxLength.append("a");
    }
    question.answer(maxLength.toString());
    assertEquals(MAX_LENGTH, question.getAnswer().length());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooLongAnswer() {
    StringBuilder tooLong = new StringBuilder();
    for (int i = 0; i < MAX_LENGTH + 1; i++) {
      tooLong.append("a");
    }
    question.answer(tooLong.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnswer() {
    question.answer(null);
  }

  @Test
  public void testCopy() {
    question.answer("test answer");
    Question copy = question.copy();

    assertEquals(question.getPrompt(), copy.getPrompt());
    assertEquals(question.isRequired(), copy.isRequired());
    assertEquals(question.getAnswer(), copy.getAnswer());
    assertNotSame(question, copy);
  }
}