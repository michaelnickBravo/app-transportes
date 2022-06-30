package pe.edu.usat.laboratorio.appcomercial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.usat.laboratorio.appcomercial.util.Gallery;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class RegistrarConductores extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_PICK = 1;

    CircleImageView circleImgConductor;
    EditText editTextNombreCon, editTextNumBrevete, editTextEmailCon, editTextContrasenaCon;
    Button botonCancelarCon, botonRegistrarCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_conductores);

        this.setTitle("REGISTRAR CONDUCTORES");

        circleImgConductor = findViewById(R.id.circle_img_conductor);
        editTextNombreCon = findViewById(R.id.edit_text_nombre_con);
        editTextNumBrevete = findViewById(R.id.edit_text_num_brevete_con);
        editTextContrasenaCon = findViewById(R.id.edit_text_contrasena_con);
        editTextEmailCon = findViewById(R.id.edit_text_email_con);

        botonCancelarCon = findViewById(R.id.boton_cancelar_con);
        botonRegistrarCon = findViewById(R.id.boton_registrar_con);

        circleImgConductor.setOnClickListener(this);
        botonRegistrarCon.setOnClickListener(this);
        botonCancelarCon.setOnClickListener(this);

        circleImgConductor.setTag("foto_no_es_real");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_img_conductor:
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_PICK );
                break;

            case R.id.boton_cancelar_con:
                Intent intent = new Intent(RegistrarConductores.this, MainActivityOficinista.class);
                startActivity(intent);
                RegistrarConductores.this.finish();

            case R.id.boton_registrar_con:
                registrarConductor();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK){
            if (resultCode == Activity.RESULT_OK){
                //Capturar la imagen seleccionada en la galería
                Uri rutaImagen = data.getData();
                try{
                    Bitmap bitmap = Gallery.rotateImage(getApplicationContext(), rutaImagen, Gallery.getOrientation(getApplicationContext(), rutaImagen));
                    Bitmap bitmapCompress = Gallery.compress(bitmap);
                    circleImgConductor.setImageBitmap(bitmapCompress);
                    circleImgConductor.setTag("foto_real");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private class RegistrarConductorTask extends AsyncTask<Void, Void, Boolean> {
        private String numBrevete, nombre, contrasena, email, img;

        public RegistrarConductorTask(String numBrevete, String nombre, String contrasena, String email, String img) {
            this.numBrevete = numBrevete;
            this.nombre = nombre;
            this.contrasena = contrasena;
            this.email = email;
            this.img = img;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Registrar = Helper.BASE_URL_WS + "/conductor/registrar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("num_brevete", this.numBrevete);
                parametros.put("nombre", this.nombre);
                parametros.put("contrasena", this.contrasena);
                parametros.put("email", this.email);
                parametros.put("img", this.img);

                //Realizar la petición al servicio web
                String respuestaJSONRegistro = new Helper().requestHttpPost(URL_WS_Registrar, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSONRegistro);

                Log.d("RegistrarConductor",json.toString());

                resultado = json.getBoolean("status");

            }catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                //Crear un dialog para confirmar si se desea grabar la venta
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrarConductores.this);
                dialog.setTitle("Se registro correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RegistrarConductores.this, MainActivityOficinista.class);
                        startActivity(intent);
                    }
                });
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(RegistrarConductores.this, "No se pudo registrar el conductor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registrarConductor() {
        final String numBrevete = editTextNumBrevete.getText().toString();
        final String nombre = editTextNombreCon.getText().toString();
        final String contrasena = editTextContrasenaCon.getText().toString();
        final String email = editTextEmailCon.getText().toString();
        final String img = circleImgConductor.getTag().equals("foto_real") ? Helper.imageToBase64(((BitmapDrawable)circleImgConductor.getDrawable()).getBitmap()) : "NO SE GUARDO IMAGEN";

        if(numBrevete.equals("")){
            Toast.makeText(RegistrarConductores.this, "DEBE INGRESAR DATOS", Toast.LENGTH_SHORT).show();
        }else {
            RegistrarConductores.RegistrarConductorTask registrarConductorTask = new RegistrarConductores.RegistrarConductorTask(numBrevete, nombre, contrasena, email, img);
            registrarConductorTask.execute();
        }
    }

}