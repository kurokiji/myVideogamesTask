package com.kurokiji.myvideogames;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.kurokiji.myvideogames.adapters.PlatformAdapter;
import com.kurokiji.myvideogames.models.Platform;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class AddActivity extends AppCompatActivity {
    // Arrays, intents y adapters
    public ArrayList<Platform> platformList;
    public PlatformAdapter platformAdapter;
    private Intent editIntent;
    private String[] formatArray;

    // Campos de la actividad
    private EditText videogameName;
    private ImageView coverImage;
    private Uri imagePath;
    private Bitmap coverImageFile;
    private RatingBar ratingBar;
    private Spinner platformSpinner;
    private Spinner formatSpinner;
    private TextView progressText;
    private SeekBar progressBar;
    public Button loadButton;

    // Algunos de los datos
    private String platform;
    private int gameProgress;
    private String format;

    // Booleanas que controlan si se ha modificado el valor de algunos inputs
    public boolean ratingChanged = false;
    public boolean imageChanged = false;
    public boolean platformChanged = false;
    public boolean formatChanged = false;
    public boolean progressChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        loadButton = findViewById(R.id.loadImage);
        videogameName = findViewById(R.id.videoGameName);
        coverImage = findViewById(R.id.coverImage);
        progressText = findViewById(R.id.progressText);

        // Código relativo a la Rating Bar
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingChanged = true;
            }
        });

        // Código relativo al spinner de seleccion de plataforma
        platformSpinner = findViewById(R.id.platformSpinner);
        initList(); // Inicialización de la lista del spinner personalizado
        platformAdapter = new PlatformAdapter(this, platformList); // Creación del Adapter personalizado
        platformSpinner.setAdapter(platformAdapter); // Asignación del adapter al spinner

        // Listener el spinner para detectar clicks en su vista
        platformSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Se extrae el objeto de la posición seleccionada en la lista del spinner y se asigna a la variable platform
                Platform selectedPlatform = (Platform) parent.getItemAtPosition(position);
                platform = selectedPlatform.getPlatformName();
                platformChanged = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Código relativo a la barra de progreso
        progressBar = findViewById(R.id.progressSeek);

        // Listener el spinner para detectar clicks en su vista
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Se extrae su progreso y se modifica el texto de la actividad para que el usuario vea el porcentaje
                progressText.setText("Progreso: " + progress + "%");
                gameProgress = progress;
                progressChanged = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // puedo eliminar estos metodos?
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Código relativo al spinner de formato
        formatSpinner = findViewById(R.id.formatSpinner);

        // Listener el spinner para detectar clicks en su vista
        formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Se extrae el string del objeto situado en la posición selecionada
                format = parent.getItemAtPosition(position).toString();
                formatChanged = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                format = null;
            }
        });

        // Código reltivo a la recepcion de datos para su edicion
        editIntent = getIntent();
        // Se realizan una comprobación por cada campo para saber si los objetos que contiene el intent son nulos
        if (editIntent.getStringExtra(Constants.NAME_DATA) != null) {
            videogameName.setText(editIntent.getStringExtra(Constants.NAME_DATA));
        }
        if (editIntent.getStringExtra(Constants.RATING_DATA) != null) {
            ratingBar.setRating(Float.parseFloat(editIntent.getStringExtra(Constants.RATING_DATA)));
        }
        if (editIntent.getStringExtra(Constants.IMAGE_DATA) != null) {
            imagePath = Uri.parse(editIntent.getStringExtra(Constants.IMAGE_DATA));
            coverImage.setImageURI(imagePath);
        }
        if (editIntent.getStringExtra(Constants.PLATFORM_DATA) != null){
            platform = editIntent.getStringExtra(Constants.PLATFORM_DATA);
            for (int i = 0; i < platformList.size(); i++) {
                if (platformSpinner.getItemAtPosition(i).toString().equals(platform)) {
                    platformSpinner.setSelection(i);
                }
            }
        }
        if (editIntent.getStringExtra(Constants.PROGRESS_DATA) != null){
            progressBar.setProgress(Integer.parseInt(editIntent.getStringExtra(Constants.PROGRESS_DATA)));
        }
        if(editIntent.getStringExtra(Constants.FORMAT_DATA) != null){
            // Encontrar la seleccion
            formatArray = getResources().getStringArray(R.array.format_array);
            format = editIntent.getStringExtra(Constants.FORMAT_DATA);
            for (int i = 0; i < formatArray.length; i++) {
                if (formatArray[i].equals(format)) {
                    formatSpinner.setSelection(i);
                }
            }
        }
    }

    /**
     * Recibe los datos desde las actividades de cámara y galería
     * y coloca la imagen en la vista de edición.
     *
     * @param  requestCode El código que identifica el Intent que espera resultados.
     * @param  resultCode Código que indica si la operación ha salid correctamente
     * @param  data El intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // recoge los datos del intent que se lanza para recibir una imagen desde la camara
        if (requestCode == Constants.OPEN_CAMERA_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            coverImageFile = (Bitmap) extras.get("data");
            coverImage.setImageBitmap(coverImageFile);
            imagePath = saveImageToGallery(coverImageFile);
            imageChanged = true;
        }
        // recoge los datos del intent que se lanza para recibir una imagen desde la galeria
        if (requestCode == Constants.OPEN_GALLERY_CODE && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                InputStream stream = this.getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(stream);
                coverImage.setImageBitmap(selectedImage);
                imagePath = saveImageToGallery(coverImageFile);
                imageChanged = true;
            } catch (FileNotFoundException e) {
                Log.d("App", "No se ha podido recuperar la foto");
            }
        }
    }

    /**
     * Devuelve los datos a la actividad principal realizando
     * las comprobaciones necesarias para no enviar datos nulos.
     *
     * @param  view La vista que acciona este método.
     */
    public void saveData(View view){
        Intent resultIntent = new Intent();
        if(videogameName.getText().toString() != null){
            resultIntent.putExtra(Constants.NAME_DATA, videogameName.getText().toString());
        }
        if(ratingChanged){
            resultIntent.putExtra(Constants.RATING_DATA, valueOf(ratingBar.getRating()));
        } else {
            resultIntent.putExtra(Constants.RATING_DATA, "-1");
        }
        if(imageChanged){
            resultIntent.putExtra(Constants.IMAGE_DATA, imagePath.toString());
        }
        if(platformChanged){
            resultIntent.putExtra(Constants.PLATFORM_DATA, platform);
        }
        if(formatChanged){
            resultIntent.putExtra(Constants.FORMAT_DATA, format);
        }
        if(progressChanged){
            resultIntent.putExtra(Constants.PROGRESS_DATA, valueOf(gameProgress));
        } else{
            resultIntent.putExtra(Constants.PROGRESS_DATA, "-1");
        }
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    /**
     * Muestra un diálogo en el que el usuario decide si cargar una
     * imagen desde la galería o de la cámara.
     *
     * @param  view La vista que acciona este método.
     */
    public void loadImage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una foto.");
        builder.setMessage("¿Quieres elegirla de la galería o hacer una foto nueva de la cámara?");
        builder.setPositiveButton(Html.fromHtml("<font color='#FFF'>Galeria</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                askForGalleryPhoto();
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#FFF'>Cámara</font>"), new DialogInterface.OnClickListener() { // se ha usado código HTML para darle formato a los botones
            @Override
            public void onClick(DialogInterface dialog, int which) {
                askForNewPhoto();
            }
        });
        builder.show();
    }
    /**
     * Crea un intent de camara para que el usuario haga una foto
     *
     * */
    public void askForNewPhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.OPEN_CAMERA_CODE);
    }
    /**
     * Crea un intent de galeria para que el usuario haga una foto
     *
     * */
    public void askForGalleryPhoto(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constants.OPEN_GALLERY_CODE);
    }
    /**
     * Almacena una foto en la galeria del usuario
     *
     * @param img Recibe la imagen en formato como un objeto Bitmap
     * @return uri.parse(path) Devuelve un objeto uri que contiene la ruta de acceso a la imagen almacenada
     * */
    public Uri saveImageToGallery (Bitmap img) {
        ContentResolver cr = getContentResolver();
        String path = MediaStore.Images.Media.insertImage(cr, img, "Caratula", "Caratula");
        //add nombre al nombre del archivo si no es nulo

        return Uri.parse(path);
    }

    /**
     * Inicializa la lista de plataformas incluyendo algunas dentro de esta
     *
     * */
    private void initList(){
        platformList = new ArrayList<>();
        platformList.add((new Platform(Constants.EMPTY_SPINNER, R.drawable.question)));
        platformList.add((new Platform(Constants.PC_NAME, R.drawable.windows)));
        platformList.add((new Platform(Constants.MACOS_NAME, R.drawable.macos)));
        platformList.add((new Platform(Constants.ANDROID_NAME, R.drawable.android)));
        platformList.add((new Platform(Constants.IOS_NAME, R.drawable.ios)));
        platformList.add((new Platform(Constants.PS4_NAME, R.drawable.ps4)));
        platformList.add((new Platform(Constants.XBOX_NAME, R.drawable.xbox)));
        platformList.add((new Platform(Constants.SWITCH_NAME, R.drawable.nintendo)));
    }

}