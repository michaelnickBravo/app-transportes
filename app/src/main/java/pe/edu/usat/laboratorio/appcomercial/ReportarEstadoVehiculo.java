package pe.edu.usat.laboratorio.appcomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.logica.ReportarEstado;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;
import pe.edu.usat.laboratorio.appcomercial.util.Pickers;

public class ReportarEstadoVehiculo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Button btnReportar;
    Spinner spPlacasVehiculos;
    TextView txtEstadoVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_estado_vehiculo);

        this.setTitle("REPORTAR ESTADO");

        //enlazar los controles
        btnReportar = findViewById(R.id.btnReportar);

        txtEstadoVehiculo = findViewById(R.id.txtEstadoVehiculo);

        spPlacasVehiculos = findViewById(R.id.spPlacasVehiculos);

        //configurar los controles que requieren reconocer el evento click
        btnReportar.setOnClickListener(this);

        //llamar a la clase para cargar el spinner de placas
        new ReportarEstadoVehiculo.PlacasTask().execute();
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_reportar_estado_vehiculo, container, false);
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReportar:
                ReportarEstado();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class PlacasTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Placas = Helper.BASE_URL_WS + "/vehiculo/listarPlacas";
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("num_brevete", Sesion.NUM_BREVETE);

                //realizar la peticion al servicio web
                String listaPlacasJSON = new Helper().requestHttpPost(URL_WS_Placas,parametros);

                //Log.e("ERROR PLACAS: ", listaPlacasJSON);

                //Convertir la lista en JSON String a JSON Object
                JSONObject jsonObject = new JSONObject(listaPlacasJSON);

                if (jsonObject.getBoolean("status")){ //true
                    //acceder al array de clientes
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    //limpiar el almacen de datos clientes
                    Vehiculo.listaVehiculos.clear();

                    for(int i=0; i<jsonArray.length(); i++){
                        //capturar los datos de cada
                        JSONObject jsonDatosPlacas = jsonArray.getJSONObject(i);

                        //instanciar un objeto de la clase para guardar los datos
                        Vehiculo vehiculo = new Vehiculo();
                        vehiculo.setEstado(jsonDatosPlacas.getString("estado"));
                        vehiculo.setConductor(jsonDatosPlacas.getString("nombre"));
                        vehiculo.setNum_brevete(jsonDatosPlacas.getString("num_brevete"));
                        vehiculo.setPlaca(jsonDatosPlacas.getString("placa"));
                        Vehiculo.listaVehiculos.add(vehiculo);
                    }
                    resultado = true;
                }else {
                    Toast.makeText(ReportarEstadoVehiculo.this, "NO TIENE VEHICULOS ASIGNADOS", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e){
                //Toast.makeText(VentaRegistrarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            try {
                if(resultado){ //true=verdadero
                    //declarar un array para almacenar solo las placas
                    String placasVehiculos[] = new String[Vehiculo.listaVehiculos.size()];

                    for (int i=0; i<Vehiculo.listaVehiculos.size(); i++){
                        placasVehiculos[i] = Vehiculo.listaVehiculos.get(i).getPlaca();
                    }

                    //crear un adaptador para cargar los datos en el spinner
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            placasVehiculos
                    );
                    //asignar el adaptador al spinner
                    spPlacasVehiculos.setAdapter(adaptador);

                }else {
                    Toast.makeText(ReportarEstadoVehiculo.this, "No se ha podido descargar la lista de placas", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    private class ReportarEstadoTask extends AsyncTask<Void, Void, Boolean>{

        private String placa, estado;

        public ReportarEstadoTask(String placa, String estado) {
            this.placa = placa;
            this.estado = estado;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado= false;
            try {
                String URL_WS_Reportar = Helper.BASE_URL_WS + "/vehiculo/actualizar/estado";
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("placa", this.placa);
                parametros.put("estado", this.estado);

                //realizar la peticion al servicio web
                String respuestaJSONReportar = new Helper().requestHttpPost(URL_WS_Reportar,parametros);

                //Convertir la respuestaJSON del ws en JSON String a JSON Object
                JSONObject json = new JSONObject(respuestaJSONReportar);

                Log.d("ReportarEstado",json.toString());

                resultado = json.getBoolean("status");

            }catch (Exception e){
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            try {
                if (resultado) {
                    //Crear un dialog para confirmar si se desea grabar
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ReportarEstadoVehiculo.this);
                    dialog.setTitle("Reporte de estado");
                    dialog.setIcon(R.drawable.ic_question);
                    dialog.setMessage("Se reporto estado correctamente");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ReportarEstadoVehiculo.this, MainActivityConductor.class);
                            startActivity(intent);
                        }
                    });
                    //Mostrar el cuadro de dialogo
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }else{
                    Toast.makeText(ReportarEstadoVehiculo.this, "No se reportar estado de vehiculo", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void ReportarEstado(){  //Metodo que ejecuta grabar la clase ReportarEstadoTask
        //declarar las variables para la lectura de datos
        final String placas = String.valueOf(Vehiculo.listaVehiculos.get(spPlacasVehiculos.getSelectedItemPosition()).getPlaca());
        final String estado = txtEstadoVehiculo.getText().toString();

        ReportarEstadoVehiculo.ReportarEstadoTask reportarEstadoTask = new ReportarEstadoVehiculo.ReportarEstadoTask(placas, estado);
        reportarEstadoTask.execute();
    }

   /* @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return true;
    }

    class TaskE implements Runnable{
        @Override
        public void run() {
        }
    }*/
}