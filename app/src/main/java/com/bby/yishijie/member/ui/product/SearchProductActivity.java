package com.bby.yishijie.member.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bby.yishijie.member.ui.MainActivity;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.ProductClassifyChildAdapter;
import com.bby.yishijie.member.adapter.ProductClassifyParentAdapter;
import com.bby.yishijie.member.entity.ProductClassify;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by 刘涛 on 2017/5/15.
 */

public class SearchProductActivity extends BaseActivity {


    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.edit_query)
    EditText editQuery;
    @Bind(R.id.product_category)
    ListView productCategory;
    @Bind(R.id.product_category_name)
    TextView productCategoryName;
    @Bind(R.id.product_category_list)
    GridView productCategoryList;


    private List<ProductClassify> parentList=new ArrayList<>();
    private List<ProductClassify> childList=new ArrayList<>();
    private ProductClassifyParentAdapter parentAdapter;
    private ProductClassifyChildAdapter childAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        ButterKnife.bind(this);
        initAdapters();
        getParentCategory();
    }

    @OnClick(R.id.edit_query)
    void search(){
        if (MainActivity.isShop){
            intent=new Intent(mContext, com.bby.yishijie.shop.ui.SearchProductListActivity.class);
        }
        else {
            intent=new Intent(mContext, SearchProductListActivity.class);
        }
        startActivity(intent);
    }

    @OnClick(R.id.btn_back)
    void onBack(){
        onBackPressed();
    }

    private void initAdapters(){
        parentAdapter=new ProductClassifyParentAdapter(mContext,parentList);
        productCategory.setAdapter(parentAdapter);
        childAdapter=new ProductClassifyChildAdapter(mContext,childList);
        productCategoryList.setAdapter(childAdapter);
        productCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parentAdapter.setSelectPosition(position);
                parentId=parentList.get(position).getId();
                productCategoryName.setText(parentList.get(position).getName());
                parentAdapter.notifyDataSetChanged();
                getChildProductCategory();

            }
        });
        productCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductClassify productBrand= (ProductClassify) parent.getAdapter().getItem(position);
                if (MainActivity.isShop){
                    intent=new Intent(mContext, com.bby.yishijie.shop.ui.ProductListActivity.class);
                }
                else {
                    intent=new Intent(mContext, ProductListActivity.class);
                }
                intent.putExtra("categoryName",productBrand.getName());
                intent.putExtra("catId",productBrand.getId());
                startActivity(intent);
            }
        });
    }

    private long parentId;
    private void getParentCategory(){
        Call<ResultDO<List<ProductClassify>>> call= ApiClient.getApiAdapter().getProCat();
        call.enqueue(new Callback<ResultDO<List<ProductClassify>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductClassify>>> call, Response<ResultDO<List<ProductClassify>>> response) {
                if (isFinish){return;}
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    parentList.clear();
                    parentList.addAll(response.body().getResult());
                    parentAdapter.notifyDataSetChanged();
                    if (parentList!=null&&parentList.size()>0)
                        parentId=parentList.get(0).getId();
                    productCategoryName.setText(parentList.get(0).getName());
                    getChildProductCategory();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ProductClassify>>> call, Throwable t) {

            }
        });
    }


    private void getChildProductCategory(){
        Call<ResultDO<List<ProductClassify>>> call= ApiClient.getApiAdapter().getSubCat(parentId);
        call.enqueue(new Callback<ResultDO<List<ProductClassify>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductClassify>>> call, Response<ResultDO<List<ProductClassify>>> response) {
                if (isFinish){return;}
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    childList.clear();
                    childList.addAll(response.body().getResult());
                    childAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ProductClassify>>> call, Throwable t) {

            }
        });
    }
}
