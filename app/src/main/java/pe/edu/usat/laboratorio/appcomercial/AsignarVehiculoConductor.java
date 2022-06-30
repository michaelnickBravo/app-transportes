package pe.edu.usat.laboratorio.appcomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.logica.Cliente;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;
import pe.edu.usat.laboratorio.appcomercial.util.Pickers;

public class AsignarVehiculoConductor extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnAsignar;
    Spinner spConductores, spVehiculos;
    EditText txtFechaInicio, txtFechaFin;
    ImageView imgFechaInicio, imgFechaFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_vehiculo_conductor);

        this.setTitle("ASIGNAR VEHICULOS A CONDUCTORES");

        //enlazar los controles
        btnAsignar = findViewById(R.id.btnAsignar);

        spConductores = findViewById(R.id.spConductores);
        spVehiculos = findViewById(R.id.spVehiculos);

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        imgFechaInicio = findViewById(R.id.imgFechaInicio);
        imgFechaFin = findViewById(R.id.imgFechaFin);

        //configurar los controles que requieren reconocer el evento click
        btnAsignar.setOnClickListener(this);
        imgFechaInicio.setOnClickListener(this);
        imgFechaFin.setOnClickListener(this);

        //llamar a la clase ClienteTask para cargar el spinner de cliente
        new ConductorTask().execute();

        //llamar a la clase TipoComprobanteTask para cargar el spinner de tipos de comprobantes
        new VehiculoTask().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgFechaInicio:
                //abrir el calendario para seleccionar una fecha
                Pickers.obtenerFecha(this, txtFechaInicio);
                break;
            case R.id.imgFechaFin:
                //abrir el calendario para seleccionar una fecha
                Pickers.obtenerFecha(this, txtFechaFin);
                break;
            case R.id.btnAsignar:
                Asignar();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class ConductorTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Conductor = Helper.BASE_URL_WS + "/conductor/listar";
                HashMap<String,String> parametros = new HashMap<>();

                //realizar la peticion al servicio web
                String listaConductorJSON = new Helper().requestHttpPost(URL_WS_Conductor,parametros);

                //Convertir la lista de clientes en JSON String a JSON Object
                JSONObject jsonObject = new JSONObject(listaConductorJSON);

                Conductor.listaConductor.clear();

                if (jsonObject.getBoolean("status")){ //true
                    //acceder al array de clientes
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for(int i=0; i<jsonArray.length(); i++){
                        //capturar los datos de cada cliente
                        JSONObject jsonDatos = jsonArray.getJSONObject(i);

                        //instanciar un objeto de la clase cliente para guardar los datos
                        Conductor c = new Conductor();
                        c.setEmail(jsonDatos.getString("email"));
                        c.setNombre(jsonDatos.getString("nombre"));
                        c.setNum_brevete(jsonDatos.getString("num_brevete"));
                        Conductor.listaConductor.add(c);
                    }
                    resultado = true;
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

            if(resultado){ //true=verdadero
                //declarar un array para almacenar solo los nombres de los clientes
                String nombreConductor[] = new String[Conductor.listaConductor.size()];

                for (int i=0; i<Conductor.listaConductor.size(); i++){
                    nombreConductor[i] = Conductor.listaConductor.get(i).getNombre();
                }

                //crear un adaptador para cargar los datos en el spinner
                ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        nombreConductor
                );
                //asignar el adaptador al spinner
                spConductores.setAdapter(adaptador);

            }else {
                //Toast.makeText(VentaRegistrarActivity.this, "No se ha podido descargar la lista de clientes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class VehiculoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Placas = Helper.BASE_URL_WS + "/vehiculo/listar";
                HashMap<String,String> parametros = new HashMap<>();

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
                        vehiculo.setPlaca(jsonDatosPlacas.getString("placa"));
                        Vehiculo.listaVehiculos.add(vehiculo);
                    }
                    resultado = true;
                }else {
                    Toast.makeText(AsignarVehiculoConductor.this, "NO HAY VEHICULOS", Toast.LENGTH_SHORT).show();
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
                    spVehiculos.setAdapter(adaptador);

                }else {
                    //Toast.makeText(AsignarVehiculoConductor.this, "No hay vehiculos", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class asignarTask extends AsyncTask<Void, Void, Boolean>{

        private String placa;
        private String brevete;
        private String fInicio;
        private String fFin;

        public asignarTask(String placa, String brevete, String fInicio, String fFin) {
            this.placa = placa;
            this.brevete = brevete;
            this.fInicio = fInicio;
            this.fFin = fFin;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado= false;
            try {
                String URL_WS_Asignar = Helper.BASE_URL_WS + "/vehiculo_conductor/registrar";
                HashMap<String,String> parametros = new HashMap<>();

                parametros.put("placa", this.placa);
                parametros.put("brevete", this.brevete);
                parametros.put("fecha_inicio", this.fInicio);
                parametros.put("fecha_fin", this.fFin);

                //realizar la peticion al servicio web
                String respuestaJSONAsignar = new Helper().requestHttpPost(URL_WS_Asignar,parametros);

                Log.e("RESPUESTA PETICION WS", respuestaJSONAsignar);

                //Convertir la respuestaJSON del ws en JSON String a JSON Object
                JSONObject json = new JSONObject(respuestaJSONAsignar);

                resultado = true;

            }catch (Exception e){
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                //Crear un dialog para confirmar si se desea grabar la venta
                final AlertDialog.Builder dialog = new AlertDialog.Builder(AsignarVehiculoConductor.this);
                dialog.setTitle("Se asigno vehiculo a un conductor correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(AsignarVehiculoConductor.this, MainActivityOficinista.class);
                        startActivity(intent);
                    }
                });
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(AsignarVehiculoConductor.this, "No se pudo asignar el vehiculo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Asignar() {
        final String placa = String.valueOf(Vehiculo.listaVehiculos.get(spVehiculos.getSelectedItemPosition()).getPlaca());
        final String brevete = String.valueOf(Conductor.listaConductor.get(spConductores.getSelectedItemPosition()).getNum_brevete());
        final String fecha_inicio = Helper.formatearDMA_to_AMD(txtFechaInicio.getText().toString());
        final String fecha_fin = Helper.formatearDMA_to_AMD(txtFechaFin.getText().toString());

        AsignarVehiculoConductor.asignarTask asignarTask = new AsignarVehiculoConductor.asignarTask(placa, brevete, fecha_inicio, fecha_fin);
        asignarTask.execute();

    }

}