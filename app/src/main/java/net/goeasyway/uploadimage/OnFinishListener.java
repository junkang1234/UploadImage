package net.goeasyway.uploadimage;

import net.goeasyway.uploadimage.model.Photo;
import net.goeasyway.uploadimage.model.UploadResult;

import java.util.List;

/**
 * Created by Lenovo on 2017/12/15.
 */

public interface OnFinishListener {
    void onSuccess(List<Photo> photos);
}