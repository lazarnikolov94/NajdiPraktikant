package najdipraktikant.teamvoid.com.Models;

/**
 * Created by lazarnikolov on 3/7/15.
 */
public class TagoviModel {

    public String tagName;
    public boolean selected;

    public TagoviModel(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
