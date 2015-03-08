package najdipraktikant.teamvoid.com.Adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.Models.TagoviModel;
import najdipraktikant.teamvoid.com.R;

/**
 * Created by lazarnikolov on 3/7/15.
 */
public class TagoviAdapter extends ArrayAdapter<TagoviModel> {

    public Context context;
    private final ArrayList<TagoviModel> modelsArrayList;

    public TagoviAdapter(Context context, ArrayList<TagoviModel> modelsArrayList) {
        super(context, R.layout.tag_listitem, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tag_listitem, parent, false);

        final CheckBox cbTagBox = (CheckBox) rowView.findViewById(R.id.cbTagBox);
        cbTagBox.setText(modelsArrayList.get(position).getTagName());

        cbTagBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbTagBox.setChecked(isChecked);
                modelsArrayList.get(position).setSelected(isChecked);
            }
        });

        return rowView;
    }

    public ArrayList<TagoviModel> getModelsArrayList() {
        return modelsArrayList;
    }
}
