package net.goeasyway.uploadimage.retrofit;


import net.goeasyway.uploadimage.model.Photo;
import net.goeasyway.uploadimage.model.UploadResult;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by lan on 17/4/8.
 */

public interface PhotoApiService {

    @GET("/photos")
    Call<List<Photo>> getAllPhotos();
    @GET("/photos")
    Observable<List<Photo>> getAllPhotos1();
    @Multipart
    @POST("/upload")
    Call<UploadResult> uploadPhoto(@Part MultipartBody.Part file);
    @Multipart
    @POST("/upload")
    Observable<UploadResult> uploadPhoto1(@Part MultipartBody.Part file);
}
