package com.example.leidong.ldmart.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.leidong.ldmart.R;
import com.example.leidong.ldmart.fragments.AddUserFragment;
import com.example.leidong.ldmart.fragments.BuyerListFragment;
import com.example.leidong.ldmart.fragments.SellerListFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 管理员主界面
 */
public class MainRootActivity extends Activity implements OnTabSelectListener {
    private static final String TAG = "MainRootActivity";

    @BindView(R.id.content_container)
    FrameLayout mContentContainer;

    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;

    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_root_main);

        ButterKnife.bind(this);

        initWidgets();

        initActions();
    }

    /**
     * 初始化动作
     */
    private void initActions() {
        mBottomBar.setOnTabSelectListener(this);
    }

    /**
     * 初始化组件
     */
    private void initWidgets() {
        mFragmentManager = getFragmentManager();
        BuyerListFragment buyerListFragment = new BuyerListFragment();
        mFragmentManager.beginTransaction().replace(R.id.content_container, buyerListFragment).commit();
    }

    /**
     * 底部导航栏的点击
     * @param tabId
     */
    @Override
    public void onTabSelected(int tabId) {
        switch (tabId){
            case R.id.tab_buyer:
                mFragmentManager = getFragmentManager();
                BuyerListFragment buyerListFragment = new BuyerListFragment();
                mFragmentManager.beginTransaction().replace(R.id.content_container, buyerListFragment).commit();
                break;
            case R.id.tab_seller:
                mFragmentManager = getFragmentManager();
                SellerListFragment sellerListFragment = new SellerListFragment();
                mFragmentManager.beginTransaction().replace(R.id.content_container, sellerListFragment).commit();
                break;
            case R.id.tab_add_user:
                mFragmentManager = getFragmentManager();
                AddUserFragment addUserFragment = new AddUserFragment();
                mFragmentManager.beginTransaction().replace(R.id.content_container, addUserFragment).commit();
                break;
        }
    }
}
