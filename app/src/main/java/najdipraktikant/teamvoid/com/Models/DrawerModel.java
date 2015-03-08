package najdipraktikant.teamvoid.com.Models;

/**
 * Created by lazarnikolov on 2/28/15.
 */
public class DrawerModel {
    private int icon;
    private String title;

    public DrawerModel(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }

    public DrawerModel(String title) {
        super();
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
