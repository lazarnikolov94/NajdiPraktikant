package najdipraktikant.teamvoid.com.Models;

import android.graphics.Bitmap;

/**
 * Created by lazarnikolov on 3/7/15.
 */
public class OglasCardModel {

    public String naslov;
    public String opis;
    public int idKompanija;
    public int id;

    public OglasCardModel(String naslov, String opis, int idKompanija, int id) {
        super();
        this.naslov = naslov;
        this.opis = opis;
        this.idKompanija = idKompanija;
        this.id = id;
    }

    public OglasCardModel(){}

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getIdKompanija() {
        return idKompanija;
    }

    public void setIdKompanija(int idKompanija) {
        this.idKompanija = idKompanija;
    }


}
