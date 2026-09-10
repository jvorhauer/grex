package grex.collection;

import grex.control.Mappable;
import grex.control.Option;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public class List<T> implements Mappable<T> {
  private static final int GROW_BY = 9;
  private T[] elements;
  private int size;

  public static <T> List<T> of() {
    return new List<>();
  }

  @SuppressWarnings("unchecked")
  public List() {
    size = 0;
    elements = (T[]) new Object[GROW_BY];
  }

  public static <T> List<T> of(final Collection<T> c) {
    final List<T> l = new List<>();
    if (c != null) {
      c.forEach(l::append);
    }
    return l;
  }

  @SafeVarargs
  public static <T> List<T> of(final T... ts) {
    final List<T> l = new List<>();
    for (final T t : ts) {
      l.append(t);
    }
    return l;
  }

  public List<T> append(final T t) {
    if (size > elements.length) {
      elements = Arrays.copyOf(elements, size + GROW_BY);
    }
    if (t != null) {
      elements[size] = t;
      size += 1;
    }
    return this;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public Option<T> get(final int index) {
    return index < 0 || index > size ? Option.none() : Option.some(elements[index]);
  }

  public boolean exists(final int index) {
    return get(index).isDefined();
  }

  public Option<T> head() {
    return get(0);
  }

  public List<T> tail() {
    return of(Arrays.copyOfRange(elements, 1, size));
  }

  public List<T> filter(final Predicate<T> pred) {
    final List<T> l = new List<>();
    for (final T element : elements) {
      if (element != null && pred.test(element)) {
        l.append(element);
      }
    }
    return l;
  }

  @Override
  public <U> List<U> map(final Function<? super T, ? extends U> mapper) {
    final List<U> l = new List<>();
    for (final T t : elements) {
      if (t != null) {
        l.append(mapper.apply(t));
      }
    }
    return l;
  }

  @SuppressWarnings("unchecked")
  public <U> List<U> flatten() {
    final List<U> result = new List<>();
    for (int i = 0; i < size; i++) {
      final Option<?> maybeSublist = get(i);
      if (maybeSublist.isDefined()) {
        final Object sublist = maybeSublist.get();
        if (sublist instanceof List) {
          final List<U> typedSublist = (List<U>) sublist;
          for (int j = 0; j < typedSublist.size(); j++) {
            final Option<U> maybeElement = typedSublist.get(j);
            if (maybeElement.isDefined()) {
              result.append(maybeElement.get());
            }
          }
        }
      }
    }
    return result;
  }

  @Override
  public <U> List<U> flatMap(final Function<? super T, ? extends Mappable<U>> mapper) {
    final List<U> result = new List<>();
    for (final T t : elements) {
      if (t != null) {
        final List<U> list = (List<U>) mapper.apply(t);
        for (int i = 0; i < list.size; i++) {
          list.get(i).forEach(result::append);
        }
      }
    }
    return result;
  }
}
