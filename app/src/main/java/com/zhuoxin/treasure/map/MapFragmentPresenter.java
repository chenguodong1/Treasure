package com.zhuoxin.treasure.map;

import com.zhuoxin.treasure.net.NetClient;
import com.zhuoxin.treasure.treasure.Area;
import com.zhuoxin.treasure.treasure.Treasure;
import com.zhuoxin.treasure.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/7.
 */

public class MapFragmentPresenter {
    private MapFragmentView mMapFragmentView;

    public MapFragmentPresenter(MapFragmentView mapFragmentView) {
        mMapFragmentView = mapFragmentView;
    }

    public void getTreasureInArea(final Area area){
        NetClient.getInstance().getNetRequest().getTreasure(area).enqueue(new Callback<List<Treasure>>() {
            @Override
            public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
                if (response.isSuccessful()){
                    List<Treasure> treasureList = response.body();
                    if (treasureList==null) {
                        mMapFragmentView.showMessage("未知错误");
                        return;
                    }
                    TreasureRepo.getInstance().cache(area);
                    TreasureRepo.getInstance().addTreasure(treasureList);
                    mMapFragmentView.setTreasureData(treasureList);
                }
            }

            @Override
            public void onFailure(Call<List<Treasure>> call, Throwable t) {
                mMapFragmentView.showMessage("请求失败");
            }
        });
    }
}
