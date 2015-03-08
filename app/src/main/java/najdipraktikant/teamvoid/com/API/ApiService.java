package najdipraktikant.teamvoid.com.API;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.Models.KompaniiModel;
import najdipraktikant.teamvoid.com.Models.LoginModel;
import najdipraktikant.teamvoid.com.Models.OglasCardModel;
import najdipraktikant.teamvoid.com.Models.TagoviModel;
import najdipraktikant.teamvoid.com.Models.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by lazarnikolov on 3/6/15.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/login")
    void Login(@Field("mail") String email, @Field("pass") String password, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/register")
    void Register(@Field("ime") String fullName, @Field("mail") String mail, @Field("pass") String pass, @Field("tip") int type, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/dodajOglas")
    void dodajOglas(@Field("idKompanija") int userid, @Field("naslov") String naslov, @Field("opis") String opis, @Field("tagovi") String tagovi, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/smeniSlika")
    void smeniSlika(@Field("userId") int userid, @Field("slika") String slikaString, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/zemiProfil")
    void zemiProfil(@Field("userId") int userid, Callback<User> response);

    @FormUrlEncoded
    @POST("/obnoviProfil")
    void obnoviProfil(@Field("user") String user, Callback<User> response);

    @FormUrlEncoded
    @POST("/zemiOglasi")
    void zemiOglasi(@Field("lokacija") String lokacija, @Field("profesija") String profesija, @Field("kompanija") String kompanija, Callback<ArrayList<OglasCardModel>> response);

    @POST("/zemiKompanii")
    void zemiKompanii(Callback<ArrayList<KompaniiModel>> response);

    @FormUrlEncoded
    @POST("/zemiOglas")
    void zemiOglas(@Field("oglasId") String id, Callback<OglasCardModel> response);

}