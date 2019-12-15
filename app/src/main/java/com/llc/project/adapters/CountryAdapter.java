package com.llc.project.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.llc.project.R;
import com.llc.project.databinding.ViewBinding;
import com.llc.project.model.ViewModel;

import java.util.ArrayList;

/**
 * Created by User on 13-12-2019.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryHolder> {

    //View elements
    Context mContext;
    ArrayList<ViewModel> mAlCountry;

    //Constructor creation
    public CountryAdapter(Context context, ArrayList<ViewModel> mAlCountry) {
        this.mContext = mContext;
        this.mAlCountry = mAlCountry;
    }

    @NonNull
    @Override
    public CountryAdapter.CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Layout initialization for Two way binding
        ViewBinding mViewBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.adapter_country, parent, false);
        return new CountryHolder(mViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryHolder holder, int i) {
        //Binding a data in two way
        ViewModel mViewModel = mAlCountry.get(i);
        holder.bind(mViewModel);
    }


    @Override
    public int getItemCount() {
        if (mAlCountry != null) {
            return mAlCountry.size();
        } else {
            return 0;
        }
    }

    public class CountryHolder extends RecyclerView.ViewHolder {
        //View Elements
        ViewBinding mViewBinding;

        public CountryHolder(ViewBinding mViewBinding) {
            super(mViewBinding.getRoot());
            this.mViewBinding = mViewBinding;
        }

        public void bind(ViewModel mViewModel) {
            this.mViewBinding.setCommonView(mViewModel);
        }

        public ViewBinding getmViewBinding() {
            return mViewBinding;
        }
    }


}
