package questionnaire;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class YesNoTest {
  private YesNo question;

  @Before
  public void setUp() {
    question = new YesNo("Do you like testing?", true);
  }

  @Test
  public void testConstructor() {
    assertEquals("Do you like testing?", question.getPrompt());
    assertTrue(question.isRequired());
    assertEquals("", question.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPrompt() {
    new YesNo(null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyPrompt() {
    new YesNo("", true);
  }

  @Test
  public void testValidAnswers() {
    // Test "yes"
    question.answer("yes");
    assertEquals("yes", question.getAnswer());

    // Test "no"
    question.answer("no");
    assertEquals("no", question.getAnswer());

    // Test case insensitivity
    question.answer("YES");
    assertEquals("YES", question.getAnswer());
    question.answer("No");
    assertEquals("No", question.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnswer() {
    question.answer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidAnswer() {
    question.answer("maybe");
  }

  @Test
  public void testCopy() {
    question.answer("yes");
    Question copy = question.copy();

    assertEquals(question.getPrompt(), copy.getPrompt());
    assertEquals(question.isRequired(), copy.isRequired());
    assertEquals(question.getAnswer(), copy.getAnswer());
    assertNotSame(question, copy);
  }
}