package questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for AbstractQuestion functionality.
 * Uses a concrete implementation (TestQuestion) to verify the behavior
 * of the abstract class's methods and constructors.
 */
public class AbstractQuestionTest {
  /**
   * A concrete implementation of AbstractQuestion used for testing.
   * Provides minimal implementations of abstract methods to verify
   * base class functionality.
   */
  private class TestQuestion extends AbstractQuestion {
    /**
     * Constructs a TestQuestion with the given prompt and required status.
     *
     * @param prompt the question text
     * @param required whether the question is required
     */
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

  /**
   * Tests that a question can be constructed with valid parameters.
   */
  @Test
  public void testValidConstruction() {
    Question q = new TestQuestion("Test prompt", true);
    assertEquals("Test prompt", q.getPrompt());
    assertTrue(q.isRequired());
    assertEquals("", q.getAnswer());
  }

  /**
   * Tests that construction fails when prompt is null.
   * @throws IllegalArgumentException expected when prompt is null
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullPrompt() {
    new TestQuestion(null, true);
  }

  /**
   * Tests that construction fails when prompt is empty.
   * @throws IllegalArgumentException expected when prompt is empty
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyPrompt() {
    new TestQuestion("", false);
  }

  /**
   * Tests that isRequired returns false when question is optional.
   */
  @Test
  public void testRequiredFalse() {
    Question q = new TestQuestion("Test prompt", false);
    assertFalse(q.isRequired());
  }

  /**
   * Tests that getPrompt returns the correct prompt string.
   */
  @Test
  public void testGetPrompt() {
    Question q = new TestQuestion("Test prompt", true);
    assertEquals("Test prompt", q.getPrompt());
  }

  /**
   * Tests that initial answer is empty string.
   */
  @Test
  public void testInitialAnswer() {
    Question q = new TestQuestion("Test prompt", true);
    assertEquals("", q.getAnswer());
  }

  /**
   * Tests setting and getting an answer.
   */
  @Test
  public void testAnswerAndGetAnswer() {
    Question q = new TestQuestion("Test prompt", true);
    q.answer("test answer");
    assertEquals("test answer", q.getAnswer());
  }

  /**
   * Tests that answering with null throws exception.
   * @throws IllegalArgumentException expected when answer is null
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullAnswer() {
    Question q = new TestQuestion("Test prompt", true);
    q.answer(null);
  }

  /**
   * Tests deep copy functionality including independence of copies.
   */
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

  /**
   * Tests that multiple answers can be set sequentially.
   */
  @Test
  public void testMultipleAnswers() {
    Question q = new TestQuestion("Test prompt", true);

    q.answer("first answer");
    assertEquals("first answer", q.getAnswer());

    q.answer("second answer");
    assertEquals("second answer", q.getAnswer());
  }

  /**
   * Tests that prompt remains unchanged when original string is modified.
   */
  @Test
  public void testPromptImmutable() {
    String prompt = "Test prompt";
    Question q = new TestQuestion(prompt, true);

    // Verify changing original string doesn't affect question's prompt
    prompt = "Changed prompt";
    assertEquals("Test prompt", q.getPrompt());
  }

  /**
   * Tests that required status remains unchanged when original boolean is modified.
   */
  @Test
  public void testRequiredImmutable() {
    boolean required = true;
    Question q = new TestQuestion("Test prompt", required);

    // Verify changing original boolean doesn't affect question's required status
    required = false;
    assertTrue(q.isRequired());
  }
}