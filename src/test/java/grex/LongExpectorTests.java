package grex;

import grex.control.Either;
import org.junit.jupiter.api.Test;

import static grex.Expector.expect;


class LongExpectorTests {

  @Test
  void toBe() {
    expect(0L).toBe(0L).toBeZero();
  }

  @Test
  void toBePositive() {
    expect(1L).toBePositive();
  }

  @SuppressWarnings("ConstantValue")
  @Test
  void toBeNull() {
    Long l = null;
    expect(l).toBeNull();
  }

  @Test
  void toBeZero() {
    expect(0L).toBeZero();
  }

  @Test
  void attempts() {
    Either<String, LongExpector> att = Either.attempt(() -> expect(1L).toBeZero(), e -> "Failed: " + e.getMessage());
    expect(att).toBeLeft();
    expect(att.getLeft()).toContain("Failed: Expected 1L");
  }
}
