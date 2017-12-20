package net.goeasyway.uploadimage;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.goeasyway.uploadimage.model.Photo;
import net.goeasyway.uploadimage.model.Photomodel;
import net.goeasyway.uploadimage.persenter.photopersenterinter;
import net.goeasyway.uploadimage.retrofit.ApiService;
import net.goeasyway.uploadimage.retrofit.ApiService1;
import net.goeasyway.uploadimage.retrofit.PhotoApiService;
import net.goeasyway.uploadimage.utils.RetrofitUtils;
import net.goeasyway.uploadimage.view.Photoview;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Photoview{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //p关联v
        final photopersenterinter presenter = new photopersenterinter(this);
        //p关联m 做网络请求
        presenter.relevance();
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.actionBtn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadPhotos();
//                loadPhotos1();
                presenter.relevance();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        loadPhotos();

    }

    private void loadPhotos1(List<Photo> photos) {
//        PhotoApiService apiService = RetrofitUtils.getInstance().getApiService(ApiService1.apiService, PhotoApiService.class);
//        Observable<List<Photo>> params = apiService.getAllPhotos1();
//        params.subscribeOn(Schedulers.io())//指定IO做耗时操作
//                .observeOn(AndroidSchedulers.mainThread())//指定更新UI在主线程
//                .subscribe(new Observer<List<Photo>>() {
//                    @Override
//                    public void onCompleted() {//完成
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {//失败
//                        Log.i("x", e.getMessage());
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onNext(List<Photo> photos) {//消费事件
//                        swipeRefreshLayout.setRefreshing(false);
//                        if (photos != null) {
//                            recyclerView.setAdapter(new PhotoAdapter(photos));
//                        }
//                    }
//                });
                        if (photos != null) {
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.setAdapter(new PhotoAdapter(photos));

                        }

    }

    private void loadPhotos() {
        Call<List<Photo>> call = ApiService.getInstance().getAllPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {

                swipeRefreshLayout.setRefreshing(false);

                List<Photo> photos = response.body();
                if (photos != null) {
                    recyclerView.setAdapter(new PhotoAdapter(photos));
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showData(List<Photo> photos) {
        loadPhotos1(photos);
        swipeRefreshLayout.setRefreshing(false);
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

        private List<Photo> photos;

        public PhotoAdapter(List<Photo> photos) {
            this.photos = photos;
            if (this.photos == null) {
                this.photos = new ArrayList<Photo>();
            }
        }


        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            Photo photo = photos.get(position);
            Glide.with(MainActivity.this).load(photo.getUrl()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


    }



}
