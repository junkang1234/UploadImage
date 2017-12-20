package net.goeasyway.uploadimage.persenter;

import net.goeasyway.uploadimage.OnFinishListener;
import net.goeasyway.uploadimage.model.Photo;
import net.goeasyway.uploadimage.model.UploadResult;
import net.goeasyway.uploadimage.model.photomodelister;
import net.goeasyway.uploadimage.view.Photoview;

import java.util.List;

/**
 * Created by Lenovo on 2017/12/15.
 */

public class photopersenterinter implements photopersenter,OnFinishListener{
    private final photomodelister photomondelister;
    Photoview Photoview;

    //初始化
    public photopersenterinter(Photoview Photoview){
        this.Photoview  = Photoview;
        //多态
        photomondelister = new photomodelister();
    }
    @Override
    public void relevance() {
        //p跟m关联
        photomondelister.getData(this);
    }
    @Override
    public void onSuccess(List<Photo> photos ) {
        //关联view
        Photoview.showData( photos);
    }

}
