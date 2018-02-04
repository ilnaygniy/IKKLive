package com.jqh.kklive.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jqh.kklive.R;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.RoomInfo;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.net.IKKLiveListManager;
import com.jqh.kklive.utils.ImgUtils;
import com.jqh.kklive.view.PullLoadRecyclerView;
import com.jqh.kklive.widget.WatcherActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqianghua on 18/1/28.
 */

public class LiveListFragment extends BaseFragment {

    private LiveListAdapter mLiveListAdapter;
    private PullLoadRecyclerView mSwipeRefreshLayout;
    public static final int REFRESH_DURATION = 1500;//刷新时长，毫秒
    public static final int LOADMORE_DURATION = 3000;//刷新时长，毫秒
    private int page = 0 ;

    private int size = 8 ;

    private int colums = 1 ;
    private Handler mHandler = new Handler(Looper.getMainLooper());// 在主线程
    @Override
    protected void initView() {

        mSwipeRefreshLayout = bindViewId(R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setGridLayout(colums);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initData() {

        reOnRresh();
    }

    @Override
    protected void initEvent() {

        mSwipeRefreshLayout.setOnPullLoadMoreListener(new PullLoadRecyclerView.OnPullLoadMoreListener() {
            @Override
            public void reRresh() {
                //刷新数据

                reOnRresh();
            }

            @Override
            public void loadMore() {
                // 加载数据
                loadOnMore();
            }
        });
    }

    private void reOnRresh(){
        page = 0 ;
        mLiveListAdapter = null;
        mLiveListAdapter = new LiveListAdapter(getContext());
        mSwipeRefreshLayout.setAdapter(mLiveListAdapter);
        mLiveListAdapter.setColums(colums);
        loadOnMore();
    }

    private void loadOnMore(){
        page++ ;
        IKKLiveListManager.getInstance().requestLiveList(page, size, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {

                List<RoomInfo> roomInfoList = (List<RoomInfo>)obj ;
                for(RoomInfo roomInfo:roomInfoList){
                    mLiveListAdapter.setData(roomInfo);
                }
                mLiveListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshCompleted();
                mSwipeRefreshLayout.setLoadMoreCompleted();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("拉取列表失败..."+errorInfo.getErrMsg());
            }
        });
    }

    private void Toast(String tip){
        Toast.makeText(getActivity(),tip,Toast.LENGTH_SHORT).show();
    }


    private class LiveListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_live_list,null);
            RoomHolder itemViewHolder = new RoomHolder(view);
            view.setTag(itemViewHolder);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            RoomInfo roomInfo = getItem(position);
            if(holder instanceof RoomHolder){
                RoomHolder roomHolder = (RoomHolder)holder;
                roomHolder.bindData(roomInfo);
            }
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            if(liveRooms == null || liveRooms.size() == 0)
                return 0 ;
            return liveRooms.size() ;
        }

        private RoomInfo getItem(int pos){
            return liveRooms.get(pos);
        }
        private Context mContext;
        private List<RoomInfo> liveRooms = new ArrayList<RoomInfo>();
        private int columns ;

        public void setColums(int columns)
        {
            this.columns = columns ;
        }

        public LiveListAdapter(Context context) {
            this.mContext = context;
        }

        public void setData(RoomInfo roomInfo){
            liveRooms.add(roomInfo);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }



        private class RoomHolder extends RecyclerView.ViewHolder{

            View itemView;
            TextView liveTitle;
            ImageView liveCover;
            ImageView hostAvatar;
            TextView hostName;
            TextView watchNums;


            public RoomHolder(View view) {
                super(view);
                itemView = view;
                liveTitle = (TextView) view.findViewById(R.id.live_title);
                liveCover = (ImageView) view.findViewById(R.id.live_cover);
                hostName = (TextView) view.findViewById(R.id.host_name);
                hostAvatar = (ImageView) view.findViewById(R.id.host_avatar);
                watchNums = (TextView) view.findViewById(R.id.watch_nums);
            }

            public void bindData(final RoomInfo roomInfo) {

                String userName = roomInfo.getUserName();
                if (TextUtils.isEmpty(userName)) {
                    userName = roomInfo.getUserId();
                }
                hostName.setText(userName);

                String liveTitleStr = roomInfo.getLiveTitle();
                if (TextUtils.isEmpty(liveTitleStr)) {
                    this.liveTitle.setText(userName + "的直播");
                } else {
                    this.liveTitle.setText(liveTitleStr);
                }
                String url = roomInfo.getLiveCover();

                Point point = null;
                point = ImgUtils.getHorPostSize(mContext,columns);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x,point.y);
                liveCover.setLayoutParams(params);

                if (TextUtils.isEmpty(url)) {
                    ImgUtils.load(R.mipmap.default_cover, liveCover);
                } else {
                    ImgUtils.disPlayImage(url, liveCover,point.x,point.y);
                }

                String avatar = roomInfo.getUserAvatar();
                if (TextUtils.isEmpty(avatar)) {
                    ImgUtils.loadRound(R.mipmap.default_avatar, hostAvatar);
                } else {
                    ImgUtils.loadRound(avatar, hostAvatar);
                }

                int watchers = roomInfo.getWatcherNums();
                String watchText = watchers + "人\r\n正在看";
                watchNums.setText(watchText);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, WatcherActivity.class);
                        intent.putExtra("roomId", roomInfo.getRoomId()+"");
                        intent.putExtra("title", roomInfo.getLiveTitle());
                        intent.putExtra("hostId",roomInfo.getUserId());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
