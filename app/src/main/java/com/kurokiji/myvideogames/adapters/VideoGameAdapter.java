package com.kurokiji.myvideogames.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurokiji.myvideogames.Constants;
import com.kurokiji.myvideogames.R;
import com.kurokiji.myvideogames.models.VideoGame;

import java.util.ArrayList;
/**
 * Adapter de la lista de videojuegos, la vista principal de la aplicación.
 *  Realiza comprobaciones para no cargar valores nulos.
 *
 */
public class VideoGameAdapter extends ArrayAdapter {

    Context context;
    int itemLayout; // layout del elemento
    ArrayList<VideoGame> arrayVideoGames = new ArrayList();

    public VideoGameAdapter(@NonNull Context context, int resource, @NonNull ArrayList<VideoGame> objects) {
        super(context, resource, objects);
        this.context = context;
        itemLayout = resource;
        arrayVideoGames = objects;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(itemLayout, parent, false);
        }
        TextView nameView = convertView.findViewById(R.id.name);
        if(arrayVideoGames.get(position).name != null) {
            nameView.setText(arrayVideoGames.get(position).name);
        }
        TextView platformView = convertView.findViewById(R.id.platform);
        ImageView platformImage = convertView.findViewById(R.id.platformImage);
        if(arrayVideoGames.get(position).platform != null) {
            platformView.setText(arrayVideoGames.get(position).platform);
            String platform = arrayVideoGames.get(position).platform;
            // Carga una imagen personalizada para cada plataforma
            switch (platform) {
                case (Constants.PC_NAME):
                    platformImage.setImageResource(R.drawable.windows);
                    break;
                case (Constants.MACOS_NAME):
                    platformImage.setImageResource(R.drawable.macos);
                    break;
                case (Constants.ANDROID_NAME):
                    platformImage.setImageResource(R.drawable.android);
                    break;
                case (Constants.IOS_NAME):
                    platformImage.setImageResource(R.drawable.ios);
                    break;
                case (Constants.PS4_NAME):
                    platformImage.setImageResource(R.drawable.ps4);
                    break;
                case (Constants.XBOX_NAME):
                    platformImage.setImageResource(R.drawable.xbox);
                    break;
                case (Constants.SWITCH_NAME):
                    platformImage.setImageResource(R.drawable.nintendo);
                    break;
                default:
                    platformImage.setImageResource(android.R.color.transparent);
            }
        } else{
            platformView.setText("");
            platformImage.setImageResource(android.R.color.transparent);
        }

        // Oculta las estrellas de valoación si la valoración es negativa, es decir, no se ha guardado nada
        RatingBar ratingBar = convertView.findViewById(R.id.review);
        Float rating = arrayVideoGames.get(position).review;
        if(rating < 0){
            ratingBar.setAlpha(0);
        } else{
            ratingBar.setAlpha(1);
            ratingBar.setRating(arrayVideoGames.get(position).review);
        }

        // Oculta la barra de progreso si el progreso es negativo, es decir, no se ha guardado nada
        ProgressBar progressBar = convertView.findViewById(R.id.progress);
        // intento de encontrar el color para la barra de progreso
        //progressBar.getProgressDrawable().setColorFilter(R.color.my_yellow, PorterDuff.Mode.SRC_ATOP);
        int progress = arrayVideoGames.get(position).progress;
        if(progress < 0){
            progressBar.setAlpha(0);
        } else {
            progressBar.setAlpha(1);
            progressBar.setProgress(arrayVideoGames.get(position).progress);
        }

        // Carga una imagen personalizada para cada formato, o la oculta si no se tiene el juego o no se ha guardado preferencia
        ImageView formatImage = convertView.findViewById(R.id.format);
        if(arrayVideoGames.get(position).format != null){
           String formatText = arrayVideoGames.get(position).format;
           switch (formatText){
               case("Físico"):
                   formatImage.setImageResource(R.drawable.fisico);
                   break;
               case("Digital"):
                   formatImage.setImageResource(R.drawable.digital);
                   break;
               case("No lo tengo"):
                   formatImage.setImageResource(android.R.color.transparent);
                   break;
               default:
                   formatImage.setImageResource(android.R.color.transparent);
                   break;
           }
       } else{
           formatImage.setImageResource(android.R.color.transparent);
       }

        // Carga la imagen del juego o establece una por defecto si no se ha guardado
        ImageView imageView = convertView.findViewById(R.id.cover);
        if(arrayVideoGames.get(position).uriPath != null){
            String uriString = arrayVideoGames.get(position).uriPath;
            Uri imagePath = Uri.parse(uriString);
            imageView.setImageURI(imagePath);
        } else {
            imageView.setImageResource(R.drawable.empty_cover);
        }
        return convertView;
    }
}