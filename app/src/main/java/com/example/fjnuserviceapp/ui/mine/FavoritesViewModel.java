package com.example.fjnuserviceapp.ui.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.fjnuserviceapp.model.FavoriteItem;
import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private final MutableLiveData<List<FavoriteItem>> favorites = new MutableLiveData<>();

    public FavoritesViewModel() {
        List<FavoriteItem> list = new ArrayList<>();
        list.add(new FavoriteItem("关于2025年寒假放假的通知", "2025-01-10", "通知"));
        list.add(new FavoriteItem("计算机网络课程调课通知", "2025-01-12", "课程"));
        list.add(new FavoriteItem("失物招领：黑色水杯", "2025-01-14", "失物"));
        favorites.setValue(list);
    }

    public LiveData<List<FavoriteItem>> getFavorites() {
        return favorites;
    }
}
