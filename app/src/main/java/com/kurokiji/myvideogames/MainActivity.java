package com.kurokiji.myvideogames;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kurokiji.myvideogames.adapters.VideoGameAdapter;
import com.kurokiji.myvideogames.models.VideoGame;
import com.kurokiji.myvideogames.sortclasses.ReversedSortByRating;
import com.kurokiji.myvideogames.sortclasses.SortByName;
import com.kurokiji.myvideogames.sortclasses.SortByPlatform;
import com.kurokiji.myvideogames.sortclasses.SortByRating;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    // Lista y array de videojuegos
    private ListView lvVideoGames;
    private ArrayList<VideoGame> arrayVideoGames = new ArrayList();

    // Datos de los videojuegos
    private String name;
    private float rating;
    private String platform;
    private String imagePath;
    private int progress;
    private String format;
    private int videogameToBeEdit;

    // Barra superior personalizada
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = findViewById(R.id.myBar);
        lvVideoGames = findViewById(R.id.videogamesList);


        @SuppressLint("SdCardPath") File f = new File(
                "/data/data/com.kurokiji.myvideogames/shared_prefs/preferencesData.xml"); // COMPROBAR PATH
        if (f.exists()) {
            loadData();
        }
        setSupportActionBar(toolBar); // Se le dan las funciones de la barra del sistema a la barra personalizada

        // Creación del adapter de la vista lista con el array de videojuegos personalizado
        VideoGameAdapter adapter = new VideoGameAdapter(this, R.layout.videogame, arrayVideoGames);
        lvVideoGames.setAdapter(adapter);

        // Listeners de la lista para detectar los clicks en sus elementos
        lvVideoGames.setOnItemClickListener((adapterView, view, i, l) -> {
           //AQUI ESTA EL LOG!!!!!!
            //AQUI ESTA EL LOG!!!!!!
            //AQUI ESTA EL LOG!!!!!!
            //AQUI ESTA EL LOG!!!!!!
            //AQUI ESTA EL LOG!!!!!!//AQUI ESTA EL LOG!!!!!!

            // Log.d("Mi Tag", "Videojuego 1: " + arrayVideoGames.get(1).name);
        });

        // Cuando el usuario deja pulsado un elemento de la lista se llama a un método que abre un cuadro de diálogo
        lvVideoGames.setOnItemLongClickListener((adapterView, view, i, l) -> {
            launchDeleteDialog(i, adapter);
            return false;
        });
    }

    /**
     * Recibe los datos de la actividad de edición/adición de videojuego.
     *
     * @param  requestCode El código que identifica el Intent que espera resultados.
     * @param  resultCode Código que indica si la operación ha salid correctamente
     * @param  data El intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Recibe los datos de la actividad de creación de un videojuego
        // Raliza las comprobaciones necesarias para no guardar ningún valor nulo
        if(requestCode == Constants.ADD_DATA_CODE && resultCode == Activity.RESULT_OK){
            if (data.getStringExtra(Constants.NAME_DATA) != null) {
                name = data.getStringExtra(Constants.NAME_DATA);
            }
            if (data.getStringExtra(Constants.RATING_DATA) != null) {
                rating = Float.parseFloat(data.getStringExtra(Constants.RATING_DATA));
            }
            if (data.getStringExtra(Constants.IMAGE_DATA) != null) {
                imagePath = data.getStringExtra(Constants.IMAGE_DATA);
            }
            if (data.getStringExtra(Constants.PLATFORM_DATA) != null){
                platform = data.getStringExtra(Constants.PLATFORM_DATA);
            }
            if (data.getStringExtra(Constants.PROGRESS_DATA) != null){
                progress = Integer.parseInt(data.getStringExtra(Constants.PROGRESS_DATA));
            }
            if(data.getStringExtra(Constants.FORMAT_DATA) != null){
                format = data.getStringExtra(Constants.FORMAT_DATA);
            }
            arrayVideoGames.add(new VideoGame(name, imagePath, rating, platform, progress, format));
            //actualizar adapter
            VideoGameAdapter adapter = (VideoGameAdapter) lvVideoGames.getAdapter();
            adapter.notifyDataSetChanged();
            eraseVariables();
            saveData();
        }
        // Recibe los datos de la actividad de edición de un videojuego
        // Raliza las comprobaciones necesarias para no guardar ningún valor nulo
        // Además comprueba que elementos han sido modificados y los modifica.
        if(requestCode == Constants.EDIT_DATA_CODE && resultCode == Activity.RESULT_OK){
            if (data.getStringExtra(Constants.NAME_DATA) != null) {
                name = data.getStringExtra(Constants.NAME_DATA);
                if(name != arrayVideoGames.get(videogameToBeEdit).name){
                    arrayVideoGames.get(videogameToBeEdit).name = name;
                }
            }
            if (data.getStringExtra(Constants.RATING_DATA) != null) {
                rating = Float.parseFloat(data.getStringExtra(Constants.RATING_DATA));
                if(rating != arrayVideoGames.get(videogameToBeEdit).review){
                    arrayVideoGames.get(videogameToBeEdit).review = rating;
                }
            }
            if (data.getStringExtra(Constants.IMAGE_DATA) != null) {
                imagePath = data.getStringExtra(Constants.IMAGE_DATA);
                if(imagePath != arrayVideoGames.get(videogameToBeEdit).uriPath){
                    arrayVideoGames.get(videogameToBeEdit).uriPath = imagePath;
                }
            }
            if (data.getStringExtra(Constants.PLATFORM_DATA) != null){
                platform = data.getStringExtra(Constants.PLATFORM_DATA);
                if(platform != arrayVideoGames.get(videogameToBeEdit).platform){
                    arrayVideoGames.get(videogameToBeEdit).platform = platform;
                }
            }
            if (data.getStringExtra(Constants.PROGRESS_DATA) != null){
                progress = Integer.parseInt(data.getStringExtra(Constants.PROGRESS_DATA));
                if(progress != arrayVideoGames.get(videogameToBeEdit).progress){
                    arrayVideoGames.get(videogameToBeEdit).progress = progress;
                }
            }
            if(data.getStringExtra(Constants.FORMAT_DATA) != null){
                format = data.getStringExtra(Constants.FORMAT_DATA);
                if(format != arrayVideoGames.get(videogameToBeEdit).format){
                    arrayVideoGames.get(videogameToBeEdit).format = format;
                }
            }

            //actualizar adapter
            VideoGameAdapter adapter = (VideoGameAdapter) lvVideoGames.getAdapter();
            adapter.notifyDataSetChanged();
            eraseVariables();
            // guardar datos
            saveData();
        }
    }

    /**
     * Crea un intent de la actividad de edición/adición
     * y lo lanza esperando resultados.
     *
     * @param  view La vista que llama a este método.
     */
    public void addButton(View view){
        //abrir una actividad nueva para agregar otro elemento
        Intent addDataIntent = new Intent(this, AddActivity.class);
        startActivityForResult(addDataIntent, Constants.ADD_DATA_CODE);
    }

    /**
     * Pasa el array de videojuegos a Json, crea el archivo de preferencias
     * y guarda el string JSON en dicho archivo.
     *
     */
    public void saveData(){
        Gson gson = new Gson();
        String dataToSave = gson.toJson(arrayVideoGames);
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.SAVED_DATA, dataToSave);
        editor.commit();
    }

    /**
     * Recupera el archivo de preferencias, y pasa el String que contiene
     * el array de videojuegos desde JSON a una lista legible por Java que,
     * después, carga en el array de videojuegos.
     *
     */
    public void loadData(){
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_DATA,Context.MODE_PRIVATE);
        String dataToLoad = preferences.getString(Constants.SAVED_DATA, null);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<VideoGame>>(){}.getType();
        arrayVideoGames = gson.fromJson(dataToLoad, listType);
    }

    /**
     * Ordena el array de videojuegos por la reseña.
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortListByRating(){
        arrayVideoGames.sort(new SortByRating());
        VideoGameAdapter adapter = (VideoGameAdapter) lvVideoGames.getAdapter();
        adapter.notifyDataSetChanged();
    }

    /**
     * Ordena el array de videojuegos por la reseña de manera inversa.
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reversedSortByRating(){
        arrayVideoGames.sort(new ReversedSortByRating());
        VideoGameAdapter adapter = (VideoGameAdapter) lvVideoGames.getAdapter();
        adapter.notifyDataSetChanged();
    }

    /**
     * Ordena el array de videojuegos por la nombre.
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByName(){
        arrayVideoGames.sort(new SortByName());
        VideoGameAdapter adapter = (VideoGameAdapter) lvVideoGames.getAdapter();
        adapter.notifyDataSetChanged();
    }

    /**
     * Ordena el array de videojuegos por plataforma.
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByPlatform(){
        arrayVideoGames.sort(new SortByPlatform());
        VideoGameAdapter adapter = (VideoGameAdapter) lvVideoGames.getAdapter();
        adapter.notifyDataSetChanged();
    }

    /**
     * Lanza un intent de la actividad de edición y la pasa los datos del elemento
     * que se ha selecionado.
     *
     * @param position Posicion del objetoa editar
     */
    public void launchEditIntent(int position){
        Intent editDataIntent = new Intent(this, AddActivity.class);
        editDataIntent.putExtra(Constants.NAME_DATA, arrayVideoGames.get(position).name);
        editDataIntent.putExtra(Constants.PLATFORM_DATA, arrayVideoGames.get(position).platform);
        editDataIntent.putExtra(Constants.RATING_DATA, String.valueOf(arrayVideoGames.get(position).review));
        editDataIntent.putExtra(Constants.IMAGE_DATA, arrayVideoGames.get(position).uriPath);
        editDataIntent.putExtra(Constants.PROGRESS_DATA, String.valueOf(arrayVideoGames.get(position).progress));
        editDataIntent.putExtra(Constants.FORMAT_DATA, arrayVideoGames.get(position).format);
        videogameToBeEdit = position;
        startActivityForResult(editDataIntent, Constants.EDIT_DATA_CODE);
    }

    /**
     * Lanza un diálogo para preguntarle al usuario si desea eliminar o editar
     * el elemento de la lista que se le pasa.
     *
     * @param position Posicion del objeto a editar
     * @param adapter El adapter que controla la vista de la lista
     *
     */
    public void launchDeleteDialog(int position, VideoGameAdapter adapter){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar entrada");
        builder.setMessage("¿Quieres editar o eliminar la entrada seleccionada?");
        builder.setPositiveButton(Html.fromHtml("<font color='#FFF'>Editar</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchEditIntent(position);
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#FA3428'>Eliminar</font>"), new DialogInterface.OnClickListener() { // se ha usado código HTML para darle formato a los botones
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* Se elimina el elemento selecionado del array, se notifican cambios al adapter
                y se guardan los datos.*/
                arrayVideoGames.remove(position);
                adapter.notifyDataSetChanged();
                saveData();
            }
        });

        builder.setNeutralButton(Html.fromHtml("<font color='#FFF'>Cancelar</font>"), new DialogInterface.OnClickListener() { // se ha usado código HTML para darle formato a los botones
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /**
     * Muestra el popup desplegable con las opciones para ordenar los datos de la lista
     *
     * @param view La vista que llama a esta función
     *
     */
    public void showPopup(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate((R.menu.popup_menu));
        popup.show();
    }

    /**
     * Lanza las funciones de ordenación en función del elemento que se le pasa
     *
     * @param item El elemento seleccionado de la lista de ordenación
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.shortByRating:
                sortListByRating();
                return true;
            case R.id.sortByInvertedRating:
                reversedSortByRating();
                return true;
            case R.id.sortByName:
                sortByName();
                return true;
            case R.id.sortByPlatform:
                sortByPlatform();
                return true;
            default:
            return false;
        }

    }
    /**
     * Deja a 0 o nulas todas las variables de datos relativas a un videojuego.
     *
     */
    public void eraseVariables(){
        name = null;
        rating = 0;
        platform = null;
        imagePath = null;
        progress = 0;
        format = null;
        videogameToBeEdit = -1;
    }
}