package najdipraktikant.teamvoid.com.Models;

/**
 * Created by lazarnikolov on 3/8/15.
 */
public class Filter {

    public static final int LOKACIJA = 1 << 0;
    public static final int PROFESIJA = 1 << 1;
    public static final int KOMPANIJA = 1 << 2;
    public int tipFilter = 0;

    Filter(){}
}
