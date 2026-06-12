package grex;

import grex.control.Option;
import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

public class OptionExpectorTests {

  @Test
  void toBeEmpty() {
    Option<Integer> option = Option.none();
    expect(option).toBeEmpty();

    option = Option.of(5);
    expect(option).not().toBeEmpty().and().not().toBeNull();
  }

  @Test
  void toBeDefined() {
    Option<Integer> option = Option.none();
    expect(option).not().toBeDefined();

    option = Option.of(5);
    expect(option).toBeDefined();
  }
}
