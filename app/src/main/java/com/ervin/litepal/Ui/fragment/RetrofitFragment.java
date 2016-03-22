package com.ervin.litepal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ervin.litepal.R;
import com.ervin.litepal.api.GetMoviesApi;
import com.ervin.litepal.api.LoginApi;
import com.ervin.litepal.model.LoginData;
import com.ervin.litepal.model.movie.MovieEntity;
import com.ervin.litepal.ui.RetrofitTest;
import com.ervin.litepal.utils.Md5;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Ervin on 2016/3/22.
 */
public class RetrofitFragment extends Fragment {

    @Bind(R.id.rv_douban)
    RecyclerView rvMovie;
    @Bind(R.id.loading)
    AVLoadingIndicatorView mLoading;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    MovieAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrofit,container,false);
        ButterKnife.bind(this,view);
        initView();

        return view;
    }

    private void initView() {
        mLoading.setVisibility(View.VISIBLE);
        mLoading.setBackgroundColor(getResources().getColor(R.color.material_amber_100));
        mFab.setOnClickListener(v->Login());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvMovie.setLayoutManager(llm);
        initData();
    }

    private void initData() {
        GetMoviesApi.request(0, 15, new Callback<MovieEntity>() {
            @Override
            public void onResponse(Response<MovieEntity> response, Retrofit retrofit) {
                if(response != null){
                    if(response.body() != null){
                        MovieEntity movies = response.body();
                        mLoading.setVisibility(View.GONE);
                        adapter = new MovieAdapter(movies);
                        rvMovie.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ervin",t.getMessage());
            }
        });
    }

    class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
        MovieEntity movies;
        MovieAdapter(MovieEntity movies){
            this.movies = movies;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.adapter_conversation_friend_interact,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picasso.with(getActivity()).load(movies.getSubjects().get(position).getCasts().get(0).getAvatars().getMedium()).into(holder.icon);
            holder.name.setText(movies.getSubjects().get(position).getTitle());
            holder.name.setTextColor(getResources().getColor(R.color.material_red_100));
        }

        @Override
        public int getItemCount() {
            return movies.getSubjects().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @Bind(R.id.iv_avatar)
            ImageView icon;
            @Bind(R.id.tv_name)
            TextView name;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    private void Login() {
        Map<String,Object> params = new HashMap<>();
        Md5 md5 = new Md5();
        String psw = md5.getMD5Str("123456");
        params.put("phoneNumber", "15820789114");
        params.put("password", psw);
        params.put("mode", "0");

        LoginApi.request(params, new Callback<LoginData>() {
            @Override
            public void onResponse(Response<LoginData> response, Retrofit retrofit) {
                Log.i("ervin:onResponse",response.toString());
                try {
                    Log.i("ervin:onResponse", response.errorBody().string().toString());
                    Log.i("ervin:onResponse", response.body().phoneNumber);

                }catch (Exception e){

                }
                //Log.i("ervin:onResponse",response.body().phoneNumber);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("ervin:onFailure",t.toString());
            }
        });


        Intent intent = new Intent(getActivity(), RetrofitTest.class);
        startActivity(intent);

    }
}
