package net.goeasyway.uploadimage.model;
import net.goeasyway.uploadimage.OnFinishListener1;

import okhttp3.MultipartBody;

/**
 * Created by Lenovo on 2017/12/15.
 */

public interface UploadResultmodel {
    void getData(OnFinishListener1 listener, MultipartBody.Part part);
}
