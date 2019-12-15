package com.llc.project.model;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.llc.project.R;

import java.io.File;

/**
 * Created by User on 13-12-2019.
 */

public class ViewModel {

    public String title,description,imageHref;

    public ViewModel(){

    }

    //Value set in view model
    public ViewModel(CommonModel mCommonModel){
        this.title = mCommonModel.title;
        this.description = mCommonModel.description;
        this.imageHref = mCommonModel.imageHref;
    }

    //Image view rendering on glide library
    @BindingAdapter({ "avatar" })
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext()).load(getObjectType(imageURL))
                .placeholder(R.drawable.ic_default_gallery)
                .error(R.drawable.ic_default_gallery)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.5f)
                .crossFade()
                .into(imageView);
    }


    static Object getObjectType(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof File) {
            return (File) o;
        } else if (o instanceof Uri) {
            return (Uri) o;
        } else {
            return null;
        }
    }

    //Display default image
    static int getDefaultImage(int option) {
        int drawable = R.drawable.ic_default_gallery;
        switch (option) {
            case 0:
                drawable = R.drawable.ic_default_gallery;
                break;
            case 1:
                drawable = R.drawable.ic_default_gallery;
                break;
            case 2:
                drawable = R.drawable.ic_default_gallery;
                break;
            case 3:
                drawable = R.drawable.ic_default_gallery;
                break;
            case 4:// Load Shop Image in BR
                drawable = R.drawable.ic_default_gallery;
                break;
            case 5:
                drawable = R.drawable.ic_default_gallery;
                break;
            case 8:
                drawable = R.drawable.ic_default_gallery;
                break;
        }
        return drawable;
    }

}
