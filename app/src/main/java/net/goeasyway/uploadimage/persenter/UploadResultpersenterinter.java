package net.goeasyway.uploadimage.persenter;

import net.goeasyway.uploadimage.OnFinishListener;
import net.goeasyway.uploadimage.OnFinishListener1;
import net.goeasyway.uploadimage.model.Photo;
import net.goeasyway.uploadimage.model.UploadResult;
import net.goeasyway.uploadimage.model.UploadResultmodelister;
import net.goeasyway.uploadimage.model.photomodelister;
import net.goeasyway.uploadimage.view.Photoview;
import net.goeasyway.uploadimage.view.UploadResultview;

import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by Lenovo on 2017/12/15.
 */

public class UploadResultpersenterinter implements UploadResultpersenter,OnFinishListener1{
    private final UploadResultmodelister UploadResultmondelister;
    UploadResultview uploadResultview;

    //初始化
    public UploadResultpersenterinter(UploadResultview uploadResultview){
        this.uploadResultview  = uploadResultview;
        //多态
        UploadResultmondelister = new UploadResultmodelister();
    }


    @Override
    public void relevance(MultipartBody.Part part) {
        UploadResultmondelister.getData(this,part);
    }

    @Override
    public void onSuccess1(UploadResult uploadResult) {
        uploadResultview.showData(uploadResult);
    }
}