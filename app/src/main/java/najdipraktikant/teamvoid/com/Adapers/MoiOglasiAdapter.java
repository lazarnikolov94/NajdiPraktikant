package najdipraktikant.teamvoid.com.Adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.Models.MoiOglasiModel;
import najdipraktikant.teamvoid.com.R;

/**
 * Created by lazarnikolov on 3/8/15.
 */
public class MoiOglasiAdapter extends ArrayAdapter<MoiOglasiModel> {

    public Context context;
    private final ArrayList<MoiOglasiModel> modelsArrayList;

    public MoiOglasiAdapter(Context context, ArrayList<MoiOglasiModel> modelsArrayList) {
        super(context, R.layout.tag_listitem, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.moj_oglas_item, parent, false);

        TextView tvPozicijaTip = (TextView) rowView.findViewById(R.id.tvPozicijaTip);

        tvPozicijaTip.setText(modelsArrayList.get(position).getOglasIme());

        ImageButton ibVidiMojOglas = (ImageButton) rowView.findViewById(R.id.ibVidiMojOglas);
        ImageButton ibIzbrisiMojOglas = (ImageButton) rowView.findViewById(R.id.ibIzbrisiMojOglas);

        ibVidiMojOglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Го гледаме огласот!", Toast.LENGTH_SHORT).show();
            }
        });
        ibIzbrisiMojOglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Го бришеме огласот!", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}