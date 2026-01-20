package com.example.fjnuserviceapp.ui.nav;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.fjnuserviceapp.MainActivity;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentNavBinding;
import com.example.fjnuserviceapp.ui.nav.adapter.PoiAdapter;
import com.example.fjnuserviceapp.ui.nav.model.LatLngPoint;
import com.example.fjnuserviceapp.ui.nav.model.PoiInfo;
import com.example.fjnuserviceapp.ui.nav.utils.TestDataUtil;
import com.example.fjnuserviceapp.ui.study.StudyFragment;

import java.util.List;

/**
 * 导航模块Fragment
 */
public class NavFragment extends Fragment implements AMapLocationListener {
    private static final String TAG = NavFragment.class.getSimpleName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    
    private FragmentNavBinding binding;
    private MapView mMapView;
    private AMap aMap;
    private final LatLng fjnuLatLng = new LatLng(26.0251, 119.2106); // 福师大旗山校区精确坐标
    
    private List<PoiInfo> poiList;
    private PoiAdapter poiAdapter;
    
    // 定位相关
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNavBinding.inflate(inflater, container, false);
        
        // 设置Fragment可以处理菜单事件
        setHasOptionsMenu(true);
        
        // 设置返回按钮
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // 高德地图隐私合规设置 - 必须在调用任何SDK接口前设置
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
        
        // 初始化地图
        mMapView = binding.mapView;
        mMapView.onCreate(savedInstanceState);
        
        if (aMap == null) {
            aMap = mMapView.getMap();
            
            // 地图性能优化配置
            if (aMap != null) {
                // 禁用3D建筑显示
                aMap.showBuildings(false);
                // 禁用室内地图
                aMap.showIndoorMap(false);
                // 简化地图显示（可以调整为更适合移动设备的显示模式）
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                // 禁用地图旋转和倾斜手势
                aMap.getUiSettings().setRotateGesturesEnabled(false);
                aMap.getUiSettings().setTiltGesturesEnabled(false);
                // 禁用定位蓝点
                aMap.setMyLocationEnabled(false);
                
                Log.d(TAG, "地图性能优化配置已应用");
            }
        }
        
        // 初始化RecyclerView
        initRecyclerView();
        
        // 加载校园POI数据
        loadCampusPOIs();
        
        // 检查并请求定位权限
        checkLocationPermission();
        
        return binding.getRoot();
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.poiRecyclerView.setLayoutManager(layoutManager);
        
        // 初始化适配器
        poiAdapter = new PoiAdapter();
        binding.poiRecyclerView.setAdapter(poiAdapter);
        
        // 设置点击监听器 - 优化版本，减少地图操作
        poiAdapter.setOnPoiClickListener(new PoiAdapter.OnPoiClickListener() {
            @Override
            public void onPoiClick(PoiInfo poiInfo) {
                if (poiInfo != null && aMap != null && getContext() != null) {
                    try {
                        // 移动相机到选中的POI位置
                        LatLngPoint coordinate = poiInfo.getCoordinate();
                        if (coordinate != null) {
                            LatLng poiLatLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                            
                            // 使用更平滑的动画方式移动相机，减少渲染压力
                            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(poiLatLng, 16f), 500, null);
                            
                            String poiName = poiInfo.getName() != null ? poiInfo.getName() : "未知地点";
                            Log.d(TAG, "POI点击: " + poiName + " 坐标: " + coordinate);
                            
                            Toast.makeText(getContext(), "已定位到 " + poiName, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "POI坐标为空");
                            Toast.makeText(getContext(), "该地点坐标信息缺失", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "POI定位失败: " + e.getMessage(), e);
                        Toast.makeText(getContext(), "定位失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    
    /**
     * 加载校园POI数据
     */
    private void loadCampusPOIs() {
        if (getContext() != null) {
            poiList = TestDataUtil.loadCampusPOIs(getContext());
            
            // 在列表中显示POI - 仅在列表显示，不在地图上自动显示所有标记
            if (poiList != null && !poiList.isEmpty() && poiAdapter != null) {
                poiAdapter.setPoiList(poiList);
                
                // 只显示校园中心点标记，不显示所有POI标记，减少地图负载
                if (aMap != null) {
                    aMap.addMarker(new MarkerOptions()
                            .position(fjnuLatLng)
                            .title("福建师范大学旗山校区")
                            .snippet("教学楼1号楼"));
                    
                    // 调整初始缩放级别，使其更适合查看单个校区
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fjnuLatLng, 15f));
                }
                
                Log.d(TAG, "成功加载并显示了 " + poiList.size() + " 个校园POI（仅列表显示）");
            } else {
                Log.d(TAG, "没有加载到校园POI数据");
                Toast.makeText(getContext(), "没有加载到校园POI数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 检查并请求定位权限
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(getActivity(), 
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // 权限已授予，开始定位
            startLocation();
        }
    }
    
    /**
     * 开始定位 - 简化版本，仅获取一次定位结果
     */
    private void startLocation() {
        // 暂时禁用实时定位服务，减少地图渲染压力
        // 如果需要定位功能，可以手动启用
        Log.d(TAG, "定位服务已禁用，以提高地图性能");
        
        /*
        if (aMap != null) {
            try {
                // 开启定位
                aMap.setMyLocationEnabled(false); // 禁用实时定位
                
                Log.d(TAG, "定位服务已配置为非实时模式");
            } catch (Exception e) {
                Log.e(TAG, "定位服务配置失败: " + e.getMessage(), e);
                Toast.makeText(getContext(), "定位初始化失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        */
    }
    
    /**
     * 权限请求结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功，开始定位
                startLocation();
            } else {
                // 权限授予失败
                Log.e(TAG, "定位权限授予失败");
                Toast.makeText(getContext(), "定位权限授予失败，无法使用定位功能", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 定位结果回调
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 定位成功
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                String address = aMapLocation.getAddress();
                
                Log.d(TAG, "定位成功: 纬度=" + latitude + ", 经度=" + longitude + ", 地址=" + address);
            } else {
                // 定位失败
                Log.e(TAG, "定位失败: " + aMapLocation.getErrorCode() + " " + aMapLocation.getErrorInfo());
            }
        }
    }
    
    // 地图生命周期管理
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        
        // 暂停定位
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // 确保地图资源已释放（如果onDestroyView未被调用）
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        
        // 销毁定位客户端
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        
        // 清除地图引用
        aMap = null;
        
        Log.d(TAG, "NavFragment完全销毁，所有资源已释放");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // 恢复ActionBar设置
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        
        // 释放地图资源 - 重要：确保地图视图被正确销毁
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        
        // 清除地图引用
        aMap = null;
        
        binding = null;
        
        Log.d(TAG, "地图资源已释放");
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 返回主界面（移除 Fragment，显示首页内容）
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .remove(this) // 移除当前 NavFragment
                        .commit();
                
                // 恢复主页元素的显示
                mainActivity.showMainPageElements();
                mainActivity.showAllFloatButtons();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}