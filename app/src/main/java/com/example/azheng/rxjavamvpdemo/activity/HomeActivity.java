package com.example.azheng.rxjavamvpdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.azheng.rxjavamvpdemo.R;
import com.example.azheng.rxjavamvpdemo.base.BaseMvpActivity;
import com.example.azheng.rxjavamvpdemo.bean.BaseObjectBean;
import com.example.azheng.rxjavamvpdemo.bean.BeanPhone;
import com.example.azheng.rxjavamvpdemo.bean.BeanZqin;
import com.example.azheng.rxjavamvpdemo.contract.HoneContract;
import com.example.azheng.rxjavamvpdemo.presenter.HomePresenter;
import com.example.azheng.rxjavamvpdemo.util.AppValue;
import com.example.azheng.rxjavamvpdemo.util.ProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 创 建 人 PeaceJay
 * 创建时间 2019/9/9
 * 类 描 述：
 */
public class HomeActivity extends BaseMvpActivity<HomePresenter> implements HoneContract.View {

    @BindView(R.id.edit_phone)
    TextInputEditText edit_phone;
    @BindView(R.id.edit_name)
    TextInputEditText edit_name;
    @BindView(R.id.edit_out_name)
    TextInputEditText edit_out_name;
    @BindView(R.id.edit_out_depict)
    TextInputEditText edit_out_depict;
    @BindView(R.id.iv_herd)
    ImageView iv_herd;

    private JSONArray jsonArrayNew;
    private Activity activity;
    private List<String> imgsPath = new ArrayList<>();
    private String name;
    private String precept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_item;
    }

    @Override
    public void initView() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onSuccess(BaseObjectBean<BeanPhone> bean) {
        if (bean.getData().isRegister()){
            Toast.makeText(this, "已注册", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "未注册", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess1(BaseObjectBean<BeanZqin> bean) {
        if (bean.getData().getUsers().size()==0){
            Toast.makeText(this, "未找到", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "" + bean.getData().getUsers().get(0).getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess2(BaseObjectBean bean) {
        Toast.makeText(this, "上传json" + bean.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess3(BaseObjectBean bean) {
        Toast.makeText(this, "单图上传" + bean.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess4(BaseObjectBean bean) {
        Toast.makeText(this, "单图带参上传" + bean.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this,"onFailed:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        ProgressDialog.getInstance().show(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialog.getInstance().dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, "onError:" +throwable.toString(), Toast.LENGTH_SHORT).show();
        Log.e("--------", throwable.toString());
    }

    /*检查手机号*/
    @OnClick(R.id.but_phone)
    public void but_phone() {
        mPresenter.phone(edit_phone.getText().toString());
    }

    /*查找宗亲*/
    @OnClick(R.id.but_name)
    public void but_name() {
        mPresenter.name(edit_name.getText().toString());
    }

    /*上传json*/
    @OnClick(R.id.but_json)
    public void but_json() {
        jsonArrayNew = new JSONArray();
        jsonArrayNew.add("4325630894");
        jsonArrayNew.add("4287102044");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonArrayNew.toJSONString());
        mPresenter.getAds(requestBody);
    }

    /*单图上传*/
    @OnClick(R.id.but_form)
    public void but_form() {
        imgsPath.clear();
        //选取图片后直接上传  IsMulti判断是不带参传图  还是带参
        mPresenter.getFrom(activity,false,1);
    }

    /*图片+参数上传*/
    @OnClick(R.id.iv_herd)
    public void iv_herd() {
        inMore();
    }

    /*图片+参数上传*/
    @OnClick(R.id.but_create)
    public void but_create() {
        inMore();
    }

    private void inMore() {
        //isEmpty是否有元素  true为没有
        if (TextUtils.isEmpty(edit_out_name.getText())){
            Toast.makeText(activity, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edit_out_depict.getText())){
            Toast.makeText(activity, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        imgsPath.clear();
        name = edit_out_name.getText().toString();
        precept = edit_out_depict.getText().toString();
        mPresenter.getFrom(activity,true,3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == AppValue.REQUEST_CODE_CHOOSE){
                    //知乎选择图片后 直接上传
                    mPresenter.getzhImag(data,activity);
                } else if (requestCode == AppValue.CROP_PICTURE) {
                    //截图后的操作 直接上传
                    mPresenter.getjtImag(data,activity,iv_herd,imgsPath,name,precept);
                }
            }
        }
    }








}