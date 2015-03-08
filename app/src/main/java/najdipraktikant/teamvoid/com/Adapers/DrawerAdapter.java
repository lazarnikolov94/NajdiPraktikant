package najdipraktikant.teamvoid.com.Adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.Models.DrawerModel;
import najdipraktikant.teamvoid.com.R;

/**
 * Created by lazarnikolov on 2/28/15.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerModel> {

    public Context context;
    private final ArrayList<DrawerModel> modelsArrayList;

    public DrawerAdapter(Context context, ArrayList<DrawerModel> modelsArrayList) {
        super(context, R.layout.drawer_header, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        Context context = getContext();
        if (position == 0) {
            rowView = inflater.inflate(R.layout.drawer_header, parent, false);
            TextView tvStudentIme = (TextView) rowView.findViewById(R.id.tvStudentIme);
            tvStudentIme.setText(modelsArrayList.get(position).getTitle());
        } else {
            rowView = inflater.inflate(R.layout.drawer_listitem, parent, false);
            ImageView imgView = (ImageView) rowView.findViewById(R.id.ivListImage);
            imgView.setImageResource(modelsArrayList.get(position).getIcon());
            TextView titleView = (TextView) rowView.findViewById(R.id.tvListItemTitle);
            titleView.setText(modelsArrayList.get(position).getTitle());
        }

        // 5. return rowView
        return rowView;
    }
}
