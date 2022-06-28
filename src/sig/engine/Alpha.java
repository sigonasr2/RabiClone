package sig.engine;

public enum Alpha {
    ALPHA0(1,1),
    ALPHA32(7,8), //7/8
    ALPHA64(3,4), //3/4
    ALPHA96(5,8), //5/8
    ALPHA128(1,2), //1/2
    ALPHA160(3,8), //3/8
    ALPHA192(1,4), //1/4
    ALPHA224(1,8), //1/8
    ALPHA256(0,8); //0/8

    int A,B;

    /** For every B pixels, A are visible.*/
     Alpha(int A,int B) {
        this.A=A;
        this.B=B;
     }

    public int getA() {
        return A;
    }

    public int getB() {
        return B;
    }
     
}
