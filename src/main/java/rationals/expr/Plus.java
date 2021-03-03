package rationals.expr;

public class Plus
extends BinaryExpr {
    public Plus(RationalExpr e, RationalExpr f) {
        super(e, f);
    }

    public String toString() {
        return this.getLeft() + "+" + this.getRight();
    }
}

