package rationals.expr;

public class Product
extends BinaryExpr {
    public Product(RationalExpr e, RationalExpr f) {
        super(e, f);
    }

    public String toString() {
        return this.getLeft() + "" + this.getRight();
    }
}

