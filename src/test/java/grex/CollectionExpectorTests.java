package grex;

import org.junit.jupiter.api.Test;

import java.util.List;

import static grex.Expector.expect;

public class CollectionExpectorTests {

  @Test
  void toHaveSize() {
    List<String> list = List.of("a", "b", "c");
    expect(list)
      .not().toBeNull()
      .toHaveSize(3)
      .and()
      .not().toBeEmpty()
      .and()
      .not().toHaveSize(2);
  }

  @Test
  void toBeEmpty() {
    List<Integer> list = List.of();
    expect(list)
      .not().toBeNull()
      .toBeEmpty()
      .and()
      .toHaveSize(0)
      .and()
      .not().toHaveSize(42);
  }

  @Test
  void nullToBeEmpty() {
    List<String> list = null;
    expect(list)
      .toBeNull()
      .toBeEmpty()
      .toHaveSize(0);
  }
}
