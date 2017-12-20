package net.goeasyway.uploadimage.model;

import android.util.Log;

import net.goeasyway.uploadimage.OnFinishListener;
import net.goeasyway.uploadimage.OnFinishListener1;
import net.goeasyway.uploadimage.retrofit.ApiService1;
import net.goeasyway.uploadimage.retrofit.PhotoApiService;
import net.goeasyway.uploadimage.utils.RetrofitUtils;

import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 2017/12/15.
 */

public class UploadResultmodelister implements UploadResultmodel{

    @Override
    public void getData(final OnFinishListener1 listener, MultipartBody.Part part) {
        PhotoApiService apiService = RetrofitUtils.getInstance().getApiService(ApiService1.apiService, PhotoApiService.class);
        Observable<UploadResult> params = apiService.uploadPhoto1(part);
        params.subscribeOn(Schedulers.io())//指定IO做耗时操作
                .observeOn(AndroidSchedulers.mainThread())//指定更新UI在主线程
                .subscribe(new Observer<UploadResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UploadResult uploadResult) {
                       if (listener!=null){
                           listener.onSuccess1(uploadResult);
                       }
                    }
                });
    }

}
