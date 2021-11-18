package com.kurokiji.myvideogames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurokiji.myvideogames.models.Platform;
import com.kurokiji.myvideogames.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Adapter que infla la lista del spinner personalizado de plataformas.
 *
 *
 */
public class PlatformAdapter extends ArrayAdapter<Platform> {
    public PlatformAdapter (Context context, ArrayList<Platform> data){
        super(context, 0, (List<Platform>) data); // cuidado con esto, en realidad le he pasado un array, pero ha hecho un cast a List
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_element, parent, false);
        }
        ImageView myImage = convertView.findViewById(R.id.platformIcon);
        TextView myText = convertView.findViewById(R.id.platformName);

        Platform currentPlatform = getItem(position);

        if(currentPlatform != null){
            myImage.setImageResource(currentPlatform.getPlatformImage());
            myText.setText(currentPlatform.getPlatformName());
        }
        return convertView;
    }
}
