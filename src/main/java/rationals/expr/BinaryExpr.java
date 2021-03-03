package rationals.expr;

public abstract class BinaryExpr
extends RationalExpr {
    private RationalExpr left;
    private RationalExpr right;

    public BinaryExpr(RationalExpr e, RationalExpr f) {
        this.left = e;
        this.right = f;
    }

    public RationalExpr getLeft() {
        return this.left;
    }

    public void setLeft(RationalExpr left) {
        this.left = left;
    }

    public RationalExpr getRight() {
        return this.right;
    }

    public void setRight(RationalExpr right) {
        this.right = right;
    }
}

