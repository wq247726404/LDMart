package com.example.leidong.ldmart.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.leidong.ldmart.MyApplication;
import com.example.leidong.ldmart.R;
import com.example.leidong.ldmart.beans.Seller;
import com.example.leidong.ldmart.constants.Constants;
import com.example.leidong.ldmart.greendao.SellerDao;
import com.example.leidong.ldmart.secure.SecureUtils;
import com.example.leidong.ldmart.storage.MySharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 卖家个人信息Fragment
 */
public class MySellerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MySellerFragment";

    @BindView(R.id.seller_name)
    EditText mSellerNameEt;

    @BindView(R.id.password1_layout)
    LinearLayout mPassword1Layout;

    @BindView(R.id.seller_password1)
    EditText mSellerPassword1Et;

    @BindView(R.id.password2_layout)
    LinearLayout mPassword2Layout;

    @BindView(R.id.seller_password2)
    EditText mSellerPassword2Et;

    @BindView(R.id.seller_phone)
    EditText mSellerPhoneEt;

    @BindView(R.id.seller_address)
    EditText mSellerAddressEt;

    @BindView(R.id.btn_change)
    Button mBtnChange;

    @BindView(R.id.btn_save)
    Button mBtnSave;

    private int mUserMode;
    private Long mSellerId;

    private MySharedPreferences mMySharedPreferences;

    private SellerDao mSellerDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my_seller, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        initWidgets();

        initActions();
    }

    /**
     * 初始化动作
     */
    private void initActions() {
        mBtnSave.setOnClickListener(this);
        mBtnChange.setOnClickListener(this);
    }

    /**
     * 初始化组件
     */
    private void initWidgets() {
//        Bundle bundle = getArguments();
//        mUserMode = bundle.getInt(Constants.USER_MODE);
//        mSellerId = bundle.getLong(Constants.SELLER_ID);
        mMySharedPreferences = MySharedPreferences.getMySharedPreferences(MyApplication.getsContext());
        mUserMode = mMySharedPreferences.load(Constants.USER_MODE, 0);
        mSellerId = mMySharedPreferences.load(Constants.SELLER_ID, 0L);

        mSellerDao = MyApplication.getInstance().getDaoSession().getSellerDao();

        mSellerNameEt.setFocusableInTouchMode(false);
        mSellerNameEt.setEnabled(false);
        mSellerPhoneEt.setFocusableInTouchMode(false);
        mSellerPhoneEt.setEnabled(false);
        mSellerAddressEt.setFocusableInTouchMode(false);
        mSellerAddressEt.setEnabled(false);

        Seller seller = mSellerDao.queryBuilder().where(SellerDao.Properties.Id.eq(mSellerId)).unique();
        if(seller != null){
            String sellerName = seller.getUsername();
            String sellerPassword = seller.getPassword();
            String sellerPhone = seller.getPhone();
            String sellerAddress = seller.getAddress();

            mSellerNameEt.setText(sellerName);
            mSellerPhoneEt.setText(sellerPhone);
            mSellerAddressEt.setText(sellerAddress);
        }
    }

    /**
     * 按钮点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_change:
                clickChangeBtn();
                break;
            case R.id.btn_save:
                clickSaveBtn();
                break;
            default:
                break;
        }
    }

    /**
     * 点击保存按钮
     */
    @SuppressLint("ShowToast")
    private void clickSaveBtn() {
        String sellerName = mSellerNameEt.getText().toString().trim();
        String sellerPassword1 = mSellerPassword1Et.getText().toString().trim();
        String sellerPassword2 = mSellerPassword2Et.getText().toString().trim();
        String sellerPhone = mSellerPhoneEt.getText().toString().trim();
        String sellerAddress = mSellerAddressEt.getText().toString().trim();

        if(sellerName.length() > 0
                && sellerPassword1.length() > 0
                && sellerPassword2.length() > 0
                && sellerPhone.length() > 0
                && sellerAddress.length() > 0
                && SecureUtils.isPasswordLegal(sellerPassword1, sellerPassword2)){
            //修改卖家信息
            Seller seller = mSellerDao.queryBuilder().where(SellerDao.Properties.Id.eq(mSellerId)).unique();
            seller.setUsername(sellerName);
            seller.setPassword(sellerPassword1);
            seller.setPhone(sellerPhone);
            seller.setAddress(sellerAddress);
            mSellerDao.update(seller);

            Toast.makeText(MyApplication.getsContext(), "您的信息更改完毕！", Toast.LENGTH_LONG).cancel();

            mPassword1Layout.setVisibility(View.GONE);
            mPassword2Layout.setVisibility(View.GONE);
            mSellerNameEt.setFocusableInTouchMode(false);
            mSellerNameEt.setEnabled(false);
            mSellerPhoneEt.setFocusableInTouchMode(false);
            mSellerPhoneEt.setEnabled(false);
            mSellerAddressEt.setFocusableInTouchMode(false);
            mSellerAddressEt.setEnabled(false);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.warning);
            builder.setMessage(R.string.warning_format_error);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mSellerNameEt.setText(null);
                    mSellerPassword1Et.setText(null);
                    mSellerPassword2Et.setText(null);
                    mSellerPhoneEt.setText(null);
                    mSellerAddressEt.setText(null);
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.create().show();
        }
    }

    /**
     * 点击更改信息按钮
     */
    @SuppressLint("ShowToast")
    private void clickChangeBtn() {
        Toast.makeText(MyApplication.getsContext(), "请更改您的信息", Toast.LENGTH_LONG).cancel();

        mSellerNameEt.setFocusableInTouchMode(true);
        mSellerNameEt.setEnabled(true);
        mSellerAddressEt.setFocusableInTouchMode(true);
        mSellerPhoneEt.setEnabled(true);
        mSellerPhoneEt.setFocusableInTouchMode(true);
        mSellerAddressEt.setEnabled(true);


        mPassword1Layout.setVisibility(View.VISIBLE);
        mPassword2Layout.setVisibility(View.VISIBLE);

        mSellerNameEt.setText(null);
        mSellerPhoneEt.setText(null);
        mSellerAddressEt.setText(null);
    }
}
