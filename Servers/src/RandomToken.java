import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomToken {


    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String total = upper + lower + digits;

    private final char[] buf;

    private final char[] symbols;

    public RandomToken() {
        this.symbols = total.toCharArray();
        this.buf = new char[5];
    }

    public String nextString() {
        for (int i = 0; i < buf.length; ++i)
            buf[i] = symbols[(int) (Math.random() *(symbols.length))];
        return new String(buf);
    }

}