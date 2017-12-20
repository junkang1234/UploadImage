package net.goeasyway.uploadimage.model;

import android.util.Log;

import net.goeasyway.uploadimage.MainActivity;
import net.goeasyway.uploadimage.OnFinishListener;
import net.goeasyway.uploadimage.retrofit.ApiService1;
import net.goeasyway.uploadimage.retrofit.PhotoApiService;
import net.goeasyway.uploadimage.utils.RetrofitUtils;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 2017/12/15.
 */

public class photomodelister implements Photomodel{
    @Override
    public void getData(final OnFinishListener listener) {
        PhotoApiService apiService = RetrofitUtils.getInstance().getApiService(ApiService1.apiService, PhotoApiService.class);
        Observable<List<Photo>> params = apiService.getAllPhotos1();
        params.subscribeOn(Schedulers.io())//指定IO做耗时操作
                .observeOn(AndroidSchedulers.mainThread())//指定更新UI在主线程
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onCompleted() {//完成

                    }

                    @Override
                    public void onError(Throwable e) {//失败
                        Log.i("x", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Photo> photos) {//消费事件
                       if (listener!=null){
                           listener.onSuccess(photos);
                       }
                    }
                });
    }
}
