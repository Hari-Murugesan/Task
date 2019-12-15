package com.llc.project.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.llc.project.R;

import java.util.ArrayList;

/**
 * Created by User on 13-12-2019.
 */

public class CommonModel {
    String title;
    String description;

    public CommonModel(String title,String description,String imageHref){
    this.title = title;
    this.description = description;
    this.imageHref = imageHref;
    }

    public CommonModel(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    String imageHref;



}
