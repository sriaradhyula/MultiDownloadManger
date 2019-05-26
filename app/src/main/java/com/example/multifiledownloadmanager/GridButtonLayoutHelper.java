package com.example.multifiledownloadmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GridButtonLayoutHelper {

    public static void addProgressBar(View alert_dialog_view, String display_text,
                                      ProgressBar progressBar, ImageView progressDoneImageView,
                                      ImageView fileInfoImageView, int current_row_count) {


        Context ctx = alert_dialog_view.getContext();
        GridLayout gridLayout = alert_dialog_view.findViewById(R.id.gl_progress_bars);

        /*
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:textSize="@dimen/activity_text_size_large"
            android:textColor="@color/colorPrimaryDark"
            android:text="Module 1"
            android:layout_margin="@dimen/activity_text_size_small"
            android:layout_row="0"
            android:layout_column="0"/>
        */
        GridLayout.LayoutParams gridLayoutParamsTextView = new GridLayout.LayoutParams(
                GridLayout.spec(current_row_count),
                GridLayout.spec(0));


        int margin_dimension = (int)ctx.getResources().getDimension(R.dimen.activity_text_size_very_very_small);
        gridLayoutParamsTextView.setMargins(margin_dimension, margin_dimension, margin_dimension,
                margin_dimension);
        TextView textView = new TextView(ctx);
        textView.setText(display_text);
        textView.setLayoutParams(gridLayoutParamsTextView);
        textView.setTextColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
        textView.setTextSize(ctx.getResources().getDimension(R.dimen.activity_text_size_large));


        /*
            <ProgressBar
            android:id="@+id/progressBar"
            style="@style/DownloadProgressBar"
            android:padding="@dimen/activity_text_size_small"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="10dp"
            android:layout_marginStart="10dp"
            android:layout_marginTop="10dp"
            android:progress="25"
            android:layout_column="1"
            android:layout_row="0"/>

         */
        GridLayout.LayoutParams gridLayoutParamsProgressBar = new GridLayout.LayoutParams(
                GridLayout.spec(current_row_count),
                GridLayout.spec(1));

        int pb_layout_margin = (int) ctx.getResources().getDimension(R.dimen.dp10);

        gridLayoutParamsProgressBar.setMargins(pb_layout_margin,
                (int) ctx.getResources().getDimension(R.dimen.activity_text_size_small), pb_layout_margin,pb_layout_margin);

        gridLayoutParamsProgressBar.width = (int) ctx.getResources().getDimension(R.dimen.popup_selection_long_width);

        // Get the Drawable custom_progressbar
        Drawable draw=ctx.getDrawable(R.drawable.custom_progressbar);

        // set the drawable as progress drawable
        progressBar.setProgressDrawable(draw);

        int progress_bar_padding = (int) ctx.getResources().getDimension(R.dimen.activity_text_size_small);
        progressBar.setProgress(0);
        progressBar.setLayoutParams(gridLayoutParamsProgressBar);


        /*
        <ImageView
            android:layout_width="25dp"
            android:layout_height="25dp"
            android:layout_gravity="center|top"
            android:layout_margin="@dimen/activity_text_size_small_plus"
            android:scaleType="fitXY"
            android:textStyle="bold"
            android:id="@+id/iv_download_progress_ok"
            android:src="@drawable/checked"
            android:layout_column="2"
            android:layout_row="0"/>
         */


        GridLayout.LayoutParams gridLayoutParamsFileInfoImageView = new GridLayout.LayoutParams(
                GridLayout.spec(current_row_count),
                GridLayout.spec(2));

        int iv_layout_margin = (int) ctx.getResources().getDimension(R.dimen.activity_text_size_very_very_small);

        gridLayoutParamsFileInfoImageView.setMargins(iv_layout_margin,
                (int) ctx.getResources().getDimension(R.dimen.activity_text_size_very_small),
                iv_layout_margin,
                iv_layout_margin);

        gridLayoutParamsFileInfoImageView.setGravity(Gravity.CENTER | Gravity.TOP);
        gridLayoutParamsFileInfoImageView.width = (int) ctx.getResources().getDimension(R.dimen.activity_text_size_large);
        gridLayoutParamsFileInfoImageView.height = (int) ctx.getResources().getDimension(R.dimen.activity_text_size_large);
        fileInfoImageView.setLayoutParams(gridLayoutParamsFileInfoImageView);
        fileInfoImageView.setImageDrawable(ctx.getDrawable(R.drawable.info_32));
        fileInfoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        fileInfoImageView.setVisibility(View.VISIBLE);

        /*
        <ImageView
            android:layout_width="25dp"
            android:layout_height="25dp"
            android:layout_gravity="center|top"
            android:layout_margin="@dimen/activity_text_size_small_plus"
            android:scaleType="fitXY"
            android:textStyle="bold"
            android:id="@+id/iv_download_progress_ok"
            android:src="@drawable/checked"
            android:layout_column="2"
            android:layout_row="0"/>
         */
        GridLayout.LayoutParams gridLayoutParamsImageView = new GridLayout.LayoutParams(
                GridLayout.spec(current_row_count),
                GridLayout.spec(3));

        gridLayoutParamsImageView.setMargins(iv_layout_margin,
                (int) ctx.getResources().getDimension(R.dimen.activity_text_size_very_small),
                iv_layout_margin,
                iv_layout_margin);

        gridLayoutParamsImageView.setGravity(Gravity.CENTER | Gravity.TOP);
        gridLayoutParamsImageView.width = (int) ctx.getResources().getDimension(R.dimen.activity_text_size_large);
        gridLayoutParamsImageView.height = (int) ctx.getResources().getDimension(R.dimen.activity_text_size_large);
        progressDoneImageView.setLayoutParams(gridLayoutParamsImageView);
        progressDoneImageView.setImageDrawable(ctx.getDrawable(R.drawable.checked));
        progressDoneImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        progressDoneImageView.setVisibility(View.INVISIBLE);

        gridLayout.addView(textView);
        gridLayout.addView(progressBar);
        gridLayout.addView(fileInfoImageView);
        gridLayout.addView(progressDoneImageView);

    }
}
