package com.sports.yue.UI.UI.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


//import com.sports.yue.UI.UI.Adapter.VideoAdapter;

import com.jaren.lib.view.LikeView;
import com.sports.yue.R;
import com.sports.yue.UI.UI.Adapter.VideoAdapter;
import com.sports.yue.UI.UI.Database_operation.Db_operation;
import com.sports.yue.UI.UI.activity.LoginActivity;
import com.sports.yue.UI.UI.activity.MainActivity;
import com.sports.yue.UI.UI.activity.SendCommunity;
import com.sports.yue.UI.UI.local_db.DbManager;
import com.sports.yue.UI.UI.models.Community;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {


    public CommunityFragment() {
        // Required empty public constructor
    }


    ListView videoList;
    private ArrayList<Map<String, Object>> datas;
    private ArrayList<String> string_data;
    private String[] videoUrl = new String[]{
        "http://bmob-cdn-22097.b0.upaiyun.com/2018/12/25/4202bc3e40d4ba828020e00ccb776f07.mp4",
        "http://bmob-cdn-22097.b0.upaiyun.com/2018/12/25/23e7c032406b3ace80a7a914e2afc153.mp4"
    };
    private String[] imageUrl = new String[]{
            "http://bmob-cdn-22097.b0.upaiyun.com/2018/12/25/1501ea9840e7180d80500617c8ae6884.jpg",
            "http://bmob-cdn-22097.b0.upaiyun.com/2018/12/25/0e599d9640872aee800cf3899cddbba8.jpg"
    };
//    private String imageUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";
    private VideoAdapter adapter;
    private AbsListView.OnScrollListener onScrollListener;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private LikeView mLikeView;
    private LikeView mLikeViewSet;


    interface Callback {

        void onSuccess();

        void onFail();
    }
    private void useSet() {
        requestPraise(new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(),"toggle Success ",Toast.LENGTH_SHORT).show();
                if (mLikeViewSet.isChecked()) {
                    mLikeViewSet.setChecked(false);
//                  mLikeViewSet.setCheckedWithoutAnimator(false);
                } else {
                    mLikeViewSet.setChecked(true);
                }
            }
            @Override
            public void onFail() {
                Toast.makeText(getContext(),"toggle Fail ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void useToggle() {
        mLikeView.toggle();
        requestPraise(new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(),"toggle Success ",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail() {
                mLikeView.toggleWithoutAnimator();
                Toast.makeText(getContext(),"toggle Fail ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void requestPraise(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();
                            if (random.nextInt() % 2 == 0) {
                                callback.onSuccess();
                            } else {
                                callback.onFail();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private class Task extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Communitty");
        // Inflate the layout for this fragment
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout)view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.

                //更新数据
                Db_operation.getDb_op().searchCommunity();
                Toast.makeText(getActivity(), "更新完成",Toast.LENGTH_LONG).show();
                new Task().execute();

                //更新界面
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_content, new CommunityFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });


//        mLikeView = view.findViewById(R.id.lv);
//        mLikeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                useToggle();
//            }
//        });
//
//        mLikeViewSet = view.findViewById(R.id.lv_set);
//        mLikeViewSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                useSet();
//            }
//        });

        Button findButton = view.findViewById(R.id.add_community);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SendCommunity.class);
                startActivity(intent);
            }
        });


        videoList = view.findViewById(R.id.message_list);

        initDatas();
        initListener();
//        jcVideoPlayerStandard = view.findViewById(R.id.videoplayer);
//        jcVideoPlayerStandard.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
//        //jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
//
//        Uri imageUri = Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
//        Glide.with(getContext()).load(imageUri).into(jcVideoPlayerStandard.thumbImageView);


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    //填充数据列表
    private List<Map<String, Object>> DataList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("author_photo", R.drawable.position);
        map.put("author_name", "Sun Yang");
        map.put("coach_introduction", "champion coach");
        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "Zhang Yongping");
//        map.put("coach_introduction", "beauty coach");
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "Li Xingyuan");
//        map.put("coach_introduction", "exercise coach");
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "He Yalun");
//        map.put("coach_introduction", "basketball coach");
//        list.add(map);

        return list;
    }

    private void initDatas() {
        datas = new ArrayList<>();
        string_data = new ArrayList<>();
        Community[] communities = DbManager.getDbManager().selectCommunity(null,null,(Date) null);

//        f String[] name = new String[]{"Sun Yang","Zhu ","Zhu Y","Zhu Ya","Zhu Yan"};
////        for (int i = 0; i < 5; i++) {
////            Map<String, Object> map = new HashMap<String, Object>();
////            map.put("video_url", videoUrl);
////            map.put("author_photo", R.drawable.position);
////            map.put("author_name", name[i]);
////            map.put("image_url", imageUrl);
////            string_data.add(videoUrl);
////            datas.add(map);
////        }

        for (int i = 0;i < communities.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("video_url", communities[i].getVideo());
            map.put("author_photo", R.drawable.position);
            map.put("author_name", communities[i].getUserName());
            map.put("image_url", imageUrl[i]);
            map.put("communityMsg", communities[i].getEmail());
            string_data.add(videoUrl[i]);
            datas.add(map);
        }


//        map = new HashMap<>();
//        map.put("video_url", videoUrl);
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "Zhu ");
//        map.put("image_url", imageUrl);
//        string_data.add(videoUrl);
//        datas.add(map);
//
//        map = new HashMap<>();
//        map.put("video_url", videoUrl);
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "Zhu Y");
//        map.put("image_url", imageUrl);
//        string_data.add(videoUrl);
//        datas.add(map);
//
//        map = new HashMap<>();
//        map.put("video_url", videoUrl);
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "Zhu Ya");
//        map.put("image_url", imageUrl);
//        string_data.add(videoUrl);
//        datas.add(map);
//
//        map = new HashMap<>();
//        map.put("video_url", videoUrl);
//        map.put("author_photo", R.drawable.position);
//        map.put("author_name", "Zhu Yan");
//        map.put("image_url", imageUrl);
//        string_data.add(videoUrl);
//        datas.add(map);


        adapter = new VideoAdapter(getActivity(), string_data, R.layout.message_item, datas);
        videoList.setAdapter(adapter);
    }

    private void initListener() {
        onScrollListener = new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        JCVideoPlayer.releaseAllVideos();
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        };

        videoList.setOnScrollListener(onScrollListener);

    }
    @Override
    public void onResume() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Communitty");
        super.onResume();
    }
}
