package questionnaire;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class QuestionnaireImpl implements Questionnaire {
  /**
   * Add a question to the questionnaire.
   *
   * @param identifier a name for the question <b>unique</b> within this
   *                   questionnaire. Not null
   *                   or empty.
   * @param q          the {@link Question} to be added to the questionnaire
   */
  private final List<Question> questions;
  private final Map<String, Integer> questionMap;

  public QuestionnaireImpl() {
    this.questions = new ArrayList<>();
    this.questionMap = new HashMap<>();
  }
  @Override
  public void addQuestion(String identifier, Question q) {
    if (identifier == null || identifier.isEmpty()) {
      throw new IllegalArgumentException("please enter a valid identifier");
    }
    if (questionMap.containsKey(identifier)) {
      throw new IllegalArgumentException("trying to override questions with the same identifier");
    }
    // this maps the identifier for a specific to the index of where that question exists.
    // took  me a lot of googling to understand this, but it makes it for fast loop up on
    // map and list of questions even after removing a question.
    questionMap.put(identifier, questions.size());
    questions.add(q);
  }

  /**
   * Remove the question with the given identifier from the questionnaire.
   *
   * @param identifier the identifier of the question to be removed.
   * @throws NoSuchElementException if there is no question with the given
   *                                identifier.
   */
  @Override
  public void removeQuestion(String identifier) {
    Integer index = questionMap.get(identifier);

    if (index == null) {
      throw new NoSuchElementException("No question found with identifier: " + identifier);
    }

    questions.remove(index.intValue());
    questionMap.remove(identifier);

    // update the indexes that are out of wack after removing question from map.
    // basically it's going to look at every element that goes after the one we removed
    // and subtract the value by 1 to update all the indexes after the one that was removed.
    for (Map.Entry<String, Integer> mapEntry : questionMap.entrySet()) {
      if (mapEntry.getValue() > index) {
          questionMap.put(mapEntry.getKey(), mapEntry.getValue()-1);
      }
    }
  }

  /**
   * Get the question with the given number, based on the order in which it was
   * added to the
   * questionnaire, or the sorted order if the {@code sort()} method is called. The
   * first question
   * is 1, second 2, etc.
   *
   * @param num the number of the question, counting from 1
   * @return the question
   * @throws IndexOutOfBoundsException if there is no such question num
   */
  @Override
  public Question getQuestion(int num) {
    if (num < 1 || num > questions.size()) {
      throw new IndexOutOfBoundsException("No valid question with that index");
    }
    // account for zero based index
    return questions.get(num-1);
  }

  /**
   * Get the question with the given identifier (question having been previously
   * added to the
   * questionnaire).
   *
   * @param identifier the identifier of the question
   * @return the question
   * @throws NoSuchElementException if there is no question with the identifier
   */
  @Override
  public Question getQuestion(String identifier) {
    Integer index = questionMap.get(identifier);

    if (index == null) {
      throw new NoSuchElementException("No questions found with that Identifier");
    }
    return questions.get(index);
  }

  /**
   * Return a list of all required questions in the questionnaire.
   *
   * @return the required questions.
   */
  @Override
  public List<Question> getRequiredQuestions() {
    List<Question> required = new ArrayList<>();
    for (Question question : questions) {
      if (question.isRequired()) {
        required.add(question);
      }
    }
    return required;
  }

  /**
   * Return a list of all optional questions in the questionnaire.
   *
   * @return the optional questions.
   */
  @Override
  public List<Question> getOptionalQuestions() {
    List<Question> isOptional = new ArrayList<>();
    for (Question question : questions) {
      if (!question.isRequired()) {
        isOptional.add(question);
      }
    }
    return isOptional;
  }

  /**
   * Report if all required questions have some non-empty answer.
   *
   * @return true if all required questions have responses, false otherwise.
   */
  @Override
  public boolean isComplete() {
    for (Question question : questions) {
      if (question.isRequired() && question.getAnswer().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return a list of just the responses to all the questions in the questionnaire.
   *
   * @return the responses
   */
  @Override
  public List<String> getResponses() {
    List<String> responses = new ArrayList<>();
    for (Question question : questions) {
      responses.add(question.getAnswer());
    }
    return responses;
  }

  /**
   * Produce a new questionnaire containing just the questions where the given
   * predicate returns
   * true. The returned questionnaire is completely independent of this
   * questionnaire. That is,
   * the questions in the returned questionnaire are <b>copies</b> of the original
   * questions.
   *
   * @param pq the predicate
   * @return the new questionnaire
   */
  @Override
  public Questionnaire filter(Predicate<Question> pq) {
    Questionnaire filtered = new QuestionnaireImpl();

    // loop through each entry (key-value pair) in our map
    // each entry contains an identifier (String) and its position (Integer) in our questions list
    for (Map.Entry<String, Integer> mapEntry : questionMap.entrySet()) {
      String identifier = mapEntry.getKey();
      // gets the question at the index in the questions List.
      Question question = questions.get(mapEntry.getValue());

      // test if this question matches our filter condition
      // pq.test() returns true if the question meets the condition
      // (like isRequired(), or has a specific word in prompt, etc.)
      if (pq.test(question)) {
        // If the question passes the test:
        //  Make a copy of the question
        //  Add it to our new questionnaire with its original identifier
        filtered.addQuestion(identifier, question.copy());
      }
    }
    return filtered;
  }

  /**
   * Sort the questions according to the given comparator. Return values from
   * {@code getQuestion(int)} should reflect the new sorted order following sort.
   *
   * @param comp a comparator for Question
   */
  @Override
  public void sort(Comparator<Question> comp) {
    // first sort the questions list by comparator
    questions.sort(comp);

    // for each ID in our map, update its position after sorting due to it being out of wack
    for (Map.Entry<String, Integer> mapEntry : questionMap.entrySet()) {
      // get the question's ID (like "name" or "age")
      String questionId = mapEntry.getKey();

      // find this question in our sorted list
      Question currentQuestion = getQuestion(questionId);

      // get its new position after sorting
      int newSortedPosition = questions.indexOf(currentQuestion);

      // update the map with the question's new position
      questionMap.put(questionId, newSortedPosition);
    }
  }

  /**
   * Produce a single summary value based on the given folding function and
   * seed value.
   *
   * @param bf   the folding function
   * @param seed the seed value
   * @return the summary value
   */
  @Override
  public <R> R fold(BiFunction<Question, R, R> bf, R seed) {
    R result = seed;
    for (Question question : questions) {
      result = bf.apply(question, result);
    }
    return result;
  }

  @Override
  public String toString() {
    if (questions.isEmpty()) {
      return "";
    }

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < questions.size(); i++) {
      Question question = questions.get(i);

      // add question
      stringBuilder.append("Question: ").append(question.getPrompt()).append("\n\n");

      // add answer
      stringBuilder.append("Answer: ").append(question.getAnswer());

      // add double newline ONLY between questions, not after last one
      if (i < questions.size() - 1) {
        stringBuilder.append("\n\n");
      }
    }
    return stringBuilder.toString();
  }
}
