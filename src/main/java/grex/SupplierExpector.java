package grex;

import java.util.function.Supplier;

public final class SupplierExpector extends Expector<Supplier<?>, SupplierExpector> {

  private Throwable t;

  public SupplierExpector(final Supplier<?> supplier) {
    super(supplier);
    try {
      supplier.get();
    } catch (final Throwable t){
      this.t = t;
    }
    isNull = t == null;
    isNotNull = !isNull;
  }

  public SupplierExpector not() {
    throw new UnsupportedOperationException("for suppliers the not operator is not (yet) supported.");
  }

  private String getMessage() {
    return t != null ? t.getMessage() : "not thrown";
  }

  public SupplierExpector toHaveThrown(final Class<? extends Throwable> clazz, final String msgtxt) {
    if (t == null) {
      disappoint(clazz.getCanonicalName() + " with " + msgtxt, "nothing thrown");
    }
    if (t.getClass().equals(clazz)) {
      if (msgtxt != null && !t.getMessage().contains(msgtxt)) {
        disappoint(clazz.getCanonicalName() + " with " + msgtxt, t.getMessage());
      }
    } else {
      disappoint(clazz.getCanonicalName(), t.getClass().getCanonicalName());
    }
    return self();
  }

  public SupplierExpector toDisappoint(final String msgtxt) {
    return toHaveThrown(Disappointment.class, msgtxt);
  }

  public SupplierExpector toDisappoint() {
    return toHaveThrown(Disappointment.class, null);
  }

  public SupplierExpector toContain(final String msgtxt) {
    return getMessage().contains(msgtxt) ? self() : disappoint("to contain " + msgtxt, getMessage()) ;
  }
}
