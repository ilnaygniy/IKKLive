package com.jqh.kklive.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jqh.kklive.R;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.RoomInfo;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.net.IKKLiveListManager;
import com.jqh.kklive.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqianghua on 18/1/28.
 */

public class LiveListFragment extends BaseFragment {

    private ListView mLiveListView;
    private LiveListAdapter mLiveListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int page = 0 ;

    private int size = 10 ;

    @Override
    protected void initView() {

        mLiveListView = bindViewId(R.id.live_list);
        mSwipeRefreshLayout = bindViewId(R.id.swipe_refresh_layout_list);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initData() {
        mLiveListAdapter = new LiveListAdapter(getContext());
        mLiveListView.setAdapter(mLiveListAdapter);
        page = 0 ;
        requestLiveList();
    }

    @Override
    protected void initEvent() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestLiveList();
            }
        });
    }

    private void requestLiveList(){
        page++ ;
        IKKLiveListManager.getInstance().requestLiveList(page, size, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                List<RoomInfo> roomInfoList = (List<RoomInfo>)obj ;
                mSwipeRefreshLayout.setRefreshing(false);
                mLiveListAdapter.removeAllRoomInfos();
                mLiveListAdapter.addRoomInfos(roomInfoList);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast("拉取列表失败..."+errorInfo.getErrMsg());
            }
        });
    }

    private void Toast(String tip){
        Toast.makeText(getActivity(),tip,Toast.LENGTH_SHORT).show();
    }
    private class LiveListAdapter extends BaseAdapter {

        private Context mContext;
        private List<RoomInfo> liveRooms = new ArrayList<RoomInfo>();

        public LiveListAdapter(Context context) {
            this.mContext = context;
        }

        public void removeAllRoomInfos() {
            liveRooms.clear();
        }

        public void addRoomInfos(List<RoomInfo> roomInfos) {
            if (roomInfos != null) {
                liveRooms.clear();
                liveRooms.addAll(roomInfos);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return liveRooms.size();
        }

        @Override
        public RoomInfo getItem(int position) {
            return liveRooms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RoomHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_live_list, null);
                holder = new RoomHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RoomHolder) convertView.getTag();
            }

            holder.bindData(liveRooms.get(position));

            return convertView;
        }


        private class RoomHolder {

            View itemView;
            TextView liveTitle;
            ImageView liveCover;
            ImageView hostAvatar;
            TextView hostName;
            TextView watchNums;

            public RoomHolder(View view) {
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
                if (TextUtils.isEmpty(url)) {
                    ImgUtils.load(R.mipmap.default_cover, liveCover);
                } else {
                    ImgUtils.load(url, liveCover);
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
//                        Intent intent = new Intent();
//                        intent.setClass(mContext, WatcherActivity.class);
//                        intent.putExtra("roomId", roomInfo.roomId);
//                        intent.putExtra("hostId", roomInfo.userId);
//                        startActivity(intent);
                    }
                });
            }
        }
    }
}
