package pe.edu.usat.laboratorio.appcomercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class RegistroVehiculos extends AppCompatActivity implements View.OnClickListener{

    EditText editTextPlacasVehiculo, editTextNumRuedas;
    Button botonCancelarVe, botonRegistrarVe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_vehiculos);

        this.setTitle("REGISTRAR VEHICULOS");

        editTextPlacasVehiculo = findViewById(R.id.edit_text_placa_vehiculo);
        editTextNumRuedas = findViewById(R.id.edit_text_num_ruedas);

        botonCancelarVe = findViewById(R.id.boton_cancelar_ve);
        botonRegistrarVe = findViewById(R.id.boton_registrar_ve);

        botonRegistrarVe.setOnClickListener(this);
        botonCancelarVe.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boton_cancelar_ve:
                Intent intent = new Intent(RegistroVehiculos.this, MainActivityOficinista.class);
                startActivity(intent);
                RegistroVehiculos.this.finish();

            case R.id.boton_registrar_ve:
                registrarVehiculos();
        }
    }

    private class RegistrarVehiculosTask extends AsyncTask<Void, Void, Boolean> {
        private String placa;
        private String ruedas;

        public RegistrarVehiculosTask(String placa, String ruedas) {
            this.placa = placa;
            this.ruedas = ruedas;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Registrar = Helper.BASE_URL_WS + "/vehiculo/insertar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("placa", this.placa);
                parametros.put("num_ruedas", this.ruedas);

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
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RegistroVehiculos.this);
                dialog.setTitle("Se registro correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RegistroVehiculos.this, MainActivityOficinista.class);
                        startActivity(intent);
                    }
                });
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(RegistroVehiculos.this, "No se pudo registrar el vehiculo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registrarVehiculos() {
        final String placas = editTextPlacasVehiculo.getText().toString();
        final String ruedas = editTextNumRuedas.getText().toString();

        if(placas.equals("")){
            Toast.makeText(RegistroVehiculos.this, "DEBE INGRESAR DATOS", Toast.LENGTH_SHORT).show();
        }else {
            RegistroVehiculos.RegistrarVehiculosTask registrarVehiculosTask = new RegistroVehiculos.RegistrarVehiculosTask(placas, ruedas);
            registrarVehiculosTask.execute();
        }
    }
}