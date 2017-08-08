package com.zhuoxin.treasure.map;

import com.zhuoxin.treasure.treasure.Treasure;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public interface MapFragmentView {
    void showMessage(String message);

    void setTreasureData(List<Treasure> treasureList);
}
