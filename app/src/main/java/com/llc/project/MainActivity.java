package com.llc.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.llc.project.adapters.CountryAdapter;
import com.llc.project.model.CommonModel;
import com.llc.project.model.RetrofitModel;
import com.llc.project.model.ViewModel;
import com.llc.project.util.Config;
import com.llc.project.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //View Elements
    RecyclerView mRvCountry;
    ArrayList<ViewModel> mAlCountry;
    CountryAdapter mAdCountry;
    ProgressDialog mProgressDialog;
    Util mUtil;
    TextView mTxtTitle;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView mImgMenu;
    public String title="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        initialization();
        initializationViews(rootView);
        setClickEvents();
        setAdapter();
        getCountryDetails(true);
        return rootView;

    }

    //Initialized element
    void initialization() {

        mAlCountry = new ArrayList<>();
        mUtil = new Util(getActivity());

    }

    //Initialized view
    void initializationViews(View rootView) {

        mRvCountry = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mTxtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        mImgMenu = (ImageView) rootView.findViewById(R.id.img_back);

    }

    void setClickEvents() {

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mImgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) getActivity()
                        .findViewById(R.id.drawer_layout);
                LinearLayout drawerList = (LinearLayout) getActivity()
                        .findViewById(R.id.testing);
                drawerLayout.openDrawer(drawerList);
            }
        });

    }

    //Recycler view set in adapter class
    void setAdapter() {

        mAdCountry = new CountryAdapter(getActivity(), mAlCountry);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRvCountry.setLayoutManager(mLayoutManager);
        mRvCountry.setItemAnimator(new DefaultItemAnimator());
        mRvCountry.setAdapter(mAdCountry);

    }

    //Api call initializing
    void getCountryDetails(Boolean isLoad) {

        if (Util.isInternetConnected(getActivity())) {
            setParams("COUNTRY_DETAILS", isLoad);
        }

    }

    @Override
    public void onRefresh() {
        getCountryDetails(false);
    }

    /**
     * Call back method for api call
     * isload is true = progressDialogue visible isload is false progressDialogue is hidden
     * @param methodName
     * @param isLoad
     */
    void setParams(String methodName, final Boolean isLoad) {

        if (isLoad) {

            if (mProgressDialog == null)
                mProgressDialog = mUtil.getProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }
        mUtil.ExecuteRetroFit(getActivity(), methodName, new Util.ResponseCallback() {
            @Override
            public void onResponseSuccess(RetrofitModel response) {

                try {
                    //check array size
                    if (mAlCountry.size() > 0) {
                        mAlCountry.clear();
                    }
                    String mResponse = response.getResponse();
                    JSONObject mJsonObject = new JSONObject(mResponse);
                    if (mJsonObject != null) {

                        if (mJsonObject.has("title")) {
                            title = mJsonObject.getString("title");
                            mTxtTitle.setText(title);
                        }
                        //two way binding data
                        if (mJsonObject.has("rows")) {
                            JSONArray mJsonArray = mJsonObject.getJSONArray("rows");
                            if (mJsonArray.length() > 0) {
                                for (int index = 0; index < mJsonArray.length(); index++) {
                                    JSONObject jsonObject = mJsonArray.getJSONObject(index);
                                    ViewModel mViewModel = new ViewModel();
                                    mViewModel.title = jsonObject.getString("title");
                                    mViewModel.description = jsonObject.getString("description");
                                    mViewModel.imageHref = jsonObject.getString("imageHref");
                                    mAlCountry.add(mViewModel);
                                }
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    //hide and show for progressDialogue
                    if (isLoad) {
                        CloseProgressDialog(mProgressDialog);
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    mAdCountry.notifyDataSetChanged();

                }
            }

            @Override
            public void onResponseFailure(String reason) {
                System.out.println("reason" + reason);
                mUtil.ShowToast(getActivity(), "Incorrect password!");
                CloseProgressDialog(mProgressDialog);
            }

            @Override
            public void onRequestFailure(Throwable t) {
                System.out.println(" t" + t);
                CloseProgressDialog(mProgressDialog);
            }
        }, mProgressDialog);
    }

    //close a progressDialogue
    public void CloseProgressDialog(ProgressDialog mProgressDialog) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }


}
