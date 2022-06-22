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

public class RegistrarCliente extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_PICK = 1;

    CircleImageView circleImgCliente;
    EditText editTextNombre, editTextNumDocRuc, editTextDireccion, editTextEmail, editTextTelefono, editTextContrasena;
    Spinner spinnerTipoDoc;
    Button botonCancelar, botonRegistrar;

    // Tipos de usuario
    final String [] tipoDoc = {"DNI", "RUC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cliente);

        circleImgCliente = findViewById(R.id.circle_img_cliente);
        editTextNombre = findViewById(R.id.edit_text_nombre);
        editTextNumDocRuc = findViewById(R.id.edit_text_num_doc_ruc);
        editTextDireccion = findViewById(R.id.edit_text_direccion);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextTelefono = findViewById(R.id.edit_text_telefono);
        editTextContrasena = findViewById(R.id.edit_text_contrasena);
        spinnerTipoDoc = findViewById(R.id.spinner_tipo_doc);
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonRegistrar = findViewById(R.id.boton_registrar);

        circleImgCliente.setOnClickListener(this);
        botonRegistrar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);

        //Definimos el arrayAdaptar para nuestro spinner de tipo de documento
        ArrayAdapter<String> arrayAdapterTipoDoc = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipoDoc);
        arrayAdapterTipoDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDoc.setAdapter(arrayAdapterTipoDoc);

        circleImgCliente.setTag("foto_no_es_real");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_img_cliente:
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_PICK );
                break;

            case R.id.boton_cancelar:
                Intent intent = new Intent(RegistrarCliente.this, LoginActivity.class);
                startActivity(intent);
                RegistrarCliente.this.finish();

            case R.id.boton_registrar:
                registrarCliente();
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
                    circleImgCliente.setImageBitmap(bitmapCompress);
                    circleImgCliente.setTag("foto_real");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private class RegistrarClienteTask extends AsyncTask<Void, Void, Boolean>{
        private String numDocumento, tipoDocId, nombre, direccion, email, telefono, contrasena, img;

        public RegistrarClienteTask(String numDocumento, String tipoDocId, String nombre, String direccion, String email, String telefono, String contrasena, String img) {
            this.numDocumento = numDocumento;
            this.tipoDocId = tipoDocId;
            this.nombre = nombre;
            this.direccion = direccion;
            this.email = email;
            this.telefono = telefono;
            this.contrasena = contrasena;
            this.img = img;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Registrar = Helper.BASE_URL_WS + "/cliente/registrar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("num_documento", this.numDocumento);
                parametros.put("tipo_doc_id", this.tipoDocId);
                parametros.put("nombre", this.nombre);
                parametros.put("direccion", this.direccion);
                parametros.put("email", this.email);
                parametros.put("telefono", this.telefono);
                parametros.put("contrasena", this.contrasena);
                parametros.put("img", this.img);

                //Realizar la petición al servicio web
                String respuestaJSONRegistro = new Helper().requestHttpPost(URL_WS_Registrar, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSONRegistro);

                Log.d("RegistrarCliente",json.toString());

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
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrarCliente.this);
                dialog.setTitle("Se registro correctamente");
                dialog.setIcon(R.drawable.ic_question);
                dialog.setMessage("Espera a que la empresa habilite su cuenta");

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RegistrarCliente.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(RegistrarCliente.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registrarCliente() {
        final String numDocumento = editTextNumDocRuc.getText().toString();
        final String tipoDocId = spinnerTipoDoc.getSelectedItem().toString().equals("RUC") ? "1" : "2";
        final String nombre = editTextNombre.getText().toString();
        final String direccion = editTextDireccion.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String telefono = editTextTelefono.getText().toString();
        final String contrasena = editTextContrasena.getText().toString();//Helper.convertPassMd5(editTextContrasena.getText().toString());
        final String img = circleImgCliente.getTag().equals("foto_real") ? Helper.imageToBase64(((BitmapDrawable)circleImgCliente.getDrawable()).getBitmap()) : "NO SE GUARDO IMAGEN";

        RegistrarClienteTask registrarClienteTask = new RegistrarClienteTask(numDocumento, tipoDocId, nombre, direccion, email, telefono, contrasena, img);
        registrarClienteTask.execute();
    }

}