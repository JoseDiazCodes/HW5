package questionnaire;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class AbstractQuestionTest {
  // Concrete implementation for testing abstract class
  private class TestQuestion extends AbstractQuestion {
    public TestQuestion(String prompt, boolean required) {
      super(prompt, required);
    }

    @Override
    protected void validateResponse(String response) {
      if (response == null) {
        throw new IllegalArgumentException("Response cannot be null");
      }
    }

    @Override
    public Question copy() {
      TestQuestion copy = new TestQuestion(this.prompt, this.required);
      copy.answer = this.answer;
      return copy;
    }
  }

  @Test
  public void testValidConstruction() {
    Question q = new TestQuestion("Test prompt", true);
    assertEquals("Test prompt", q.getPrompt());
    assertTrue(q.isRequired());
    assertEquals("", q.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPrompt() {
    new TestQuestion(null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyPrompt() {
    new TestQuestion("", false);
  }

  @Test
  public void testRequiredFalse() {
    Question q = new TestQuestion("Test prompt", false);
    assertFalse(q.isRequired());
  }

  @Test
  public void testGetPrompt() {
    Question q = new TestQuestion("Test prompt", true);
    assertEquals("Test prompt", q.getPrompt());
  }

  @Test
  public void testInitialAnswer() {
    Question q = new TestQuestion("Test prompt", true);
    assertEquals("", q.getAnswer());
  }

  @Test
  public void testAnswerAndGetAnswer() {
    Question q = new TestQuestion("Test prompt", true);
    q.answer("test answer");
    assertEquals("test answer", q.getAnswer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnswer() {
    Question q = new TestQuestion("Test prompt", true);
    q.answer(null);
  }

  @Test
  public void testCopy() {
    TestQuestion original = new TestQuestion("Test prompt", true);
    original.answer("test answer");

    Question copy = original.copy();

    // Verify all properties are copied
    assertEquals(original.getPrompt(), copy.getPrompt());
    assertEquals(original.isRequired(), copy.isRequired());
    assertEquals(original.getAnswer(), copy.getAnswer());

    // Verify it's a different instance
    assertNotSame(original, copy);

    // Verify changing copy doesn't affect original
    copy.answer("new answer");
    assertEquals("test answer", original.getAnswer());
    assertEquals("new answer", copy.getAnswer());
  }

  @Test
  public void testMultipleAnswers() {
    Question q = new TestQuestion("Test prompt", true);

    q.answer("first answer");
    assertEquals("first answer", q.getAnswer());

    q.answer("second answer");
    assertEquals("second answer", q.getAnswer());
  }

  @Test
  public void testPromptImmutable() {
    String prompt = "Test prompt";
    Question q = new TestQuestion(prompt, true);

    // Verify changing original string doesn't affect question's prompt
    prompt = "Changed prompt";
    assertEquals("Test prompt", q.getPrompt());
  }

  @Test
  public void testRequiredImmutable() {
    boolean required = true;
    Question q = new TestQuestion("Test prompt", required);

    // Verify changing original boolean doesn't affect question's required status
    required = false;
    assertTrue(q.isRequired());
  }
}