package pe.edu.usat.laboratorio.appcomercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorEstadosSolicitudes;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoVehiculos;
import pe.edu.usat.laboratorio.appcomercial.logica.EstadoCliente;
import pe.edu.usat.laboratorio.appcomercial.logica.EstadoSolicitud;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class dialog_gestion_asignacion_Vehiculo_conductor extends AppCompatActivity implements View.OnClickListener {

    TextView txtCliente, txtDni, txtClase, txttipo, txtdireccionP, txtFecha, txtDireccionL;
    Button btnRegistrar;
    Spinner spinnerestados;
    ArrayList<Vehiculo> listaTarifassWS;
    ProgressDialog dialog;
    AdaptadorListadoVehiculos adaptador;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_gestion_asignacion_vehiculo_conductor);
        Bundle extras = getIntent().getExtras();


        this.setTitle("Gestion de asignacion de vehiculos");


        txtCliente = findViewById(R.id.txtNombreCliente);
        txttipo = findViewById(R.id.txtTipoCarga);
        txtDni = findViewById(R.id.txtnumerodocumento);
        txtClase = findViewById(R.id.txtClaseCarga);
        txtdireccionP = findViewById(R.id.txtDireccionPartida);
        txtFecha = findViewById(R.id.txtFechaPartida);
        txtDireccionL = findViewById(R.id.txtDireccionLlegada);
        spinnerestados = findViewById(R.id.vehiculo_conductor);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(this);


        txtCliente.setText(extras.getString("id") + "-" + extras.getString("cliente").toUpperCase());
        txttipo.setText(extras.getString("tipo"));
        txtDni.setText(extras.getString("dni"));
        txtdireccionP.setText(extras.getString("direccionP").toUpperCase());
        txtDireccionL.setText(extras.getString("direccionL").toUpperCase());
        txtFecha.setText(extras.getString("fecha"));
        txtClase.setText(extras.getString("clase").toUpperCase());

        recycler = findViewById(R.id.recyclerViewVehiculos);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        new dialog_gestion_asignacion_Vehiculo_conductor.estadoTask().execute();

        listarTarifas();


    }

    public dialog_gestion_asignacion_Vehiculo_conductor() {
        // Required empty public constructor
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.btnRegistrar:
                Asignar();
                break;


        }

    }


    private class estadoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_clientes = "";
                HashMap<String, String> parametros = new HashMap<>();

                URL_WS_clientes = Helper.BASE_URL_WS + "/vehiculo_conductor/listar2";
                //parametros.put("token", Sesion.TOKEN);

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_clientes, parametros);

                JSONObject jsonestados = new JSONObject(resultadoJSON);
                if (jsonestados.getBoolean("status")) {
                    //acceder a los datos

                    JSONArray jsonArray = jsonestados.getJSONArray("data");

                    //limpiar el almacen de datos clientes
                    Vehiculo.listaVehiculos.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //capturar los datos de cada
                        JSONObject jsonDatosestados = jsonArray.getJSONObject(i);

                        //instanciar un objeto de la clase para guardar los datos
                        Vehiculo estado = new Vehiculo();
                        estado.setId_vehiculo_conductor(jsonDatosestados.getInt("id"));
                        estado.setConductor(jsonDatosestados.getString("nombre"));
                        estado.setPlaca(jsonDatosestados.getString("vehiculo_placa"));
                        estado.listaVehiculos.add(estado);
                    }
                    resultado = true;


                } else {
                    //retornar un valor falso (false), cuando no hay registro
                    //Toast.makeText(SolicitudesServicioCargaConductor.this.getContext(), "ACTUALMENTE NO SE ENCUENTRAN ATENDIENDO UNA CARGA", Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Error");
                    resultado = false;
                }
            } catch (Exception e) {
                //Toast.makeText(CatalogoProductoFragment.this.getContext(), "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d("ERROR APP COMERCIAL", e.getMessage());
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            try {
                if (resultado) { //true=verdadero
                    //declarar un array para almacenar solo las placas
                    String estadosClientes[] = new String[Vehiculo.listaVehiculos.size()];

                    for (int i = 0; i < Vehiculo.listaVehiculos.size(); i++) {
                        estadosClientes[i] = Vehiculo.listaVehiculos.get(i).getId_vehiculo_conductor() + "-" + Vehiculo.listaVehiculos.get(i).getConductor() + "-" + Vehiculo.listaVehiculos.get(i).getPlaca();
                    }

                    //crear un adaptador para cargar los datos en el spinner
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                            dialog_gestion_asignacion_Vehiculo_conductor.this.getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            estadosClientes
                    );
                    //asignar el adaptador al spinner
                    spinnerestados.setAdapter(adaptador);

                } else {
                    //Toast.makeText(AsignarVehiculoConductor.this, "No hay vehiculos", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class actualizarTask extends AsyncTask<Void, Void, Boolean> {

        private String id_conductor_vehiculo;
        private String id_solicitud;


        public actualizarTask(String id_conductor_vehiculo, String id_solicitud) {
            this.id_conductor_vehiculo = id_conductor_vehiculo;
            this.id_solicitud = id_solicitud;


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Asignar = Helper.BASE_URL_WS + "/vehiculo_asignado_servicio/registrar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("id_servicio", this.id_solicitud);
                parametros.put("id_vehiculo_conductor", this.id_conductor_vehiculo);


                //realizar la peticion al servicio web
                String respuestaJSONAsignar = new Helper().requestHttpPost(URL_WS_Asignar, parametros);

                Log.e("RESPUESTA PETICION WS", respuestaJSONAsignar);

                //Convertir la respuestaJSON del ws en JSON String a JSON Object
                JSONObject json = new JSONObject(respuestaJSONAsignar);

                resultado = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                //Crear un dialog para confirmar si se desea grabar la venta
                final AlertDialog.Builder dialog = new AlertDialog.Builder(dialog_gestion_asignacion_Vehiculo_conductor.this);
                dialog.setTitle("Se asigno el vehiculo");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Intent intent = new Intent(DialogGestionestadosSolicitud.this, MainActivityOficinista.class);
                        //startActivity(intent);
                        listarTarifas();

                    }
                });
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            } else {
                Toast.makeText(dialog_gestion_asignacion_Vehiculo_conductor.this, "No se pudo asignar el vehiculo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Asignar() {
        final String id_vehiculo_conductor = String.valueOf(spinnerestados.getSelectedItem().toString().split("-")[0]);
        final String id_solicitud = String.valueOf(txtCliente.getText().toString().split("-")[0]);


        dialog_gestion_asignacion_Vehiculo_conductor.actualizarTask actualiatTask = new dialog_gestion_asignacion_Vehiculo_conductor.actualizarTask(id_vehiculo_conductor, id_solicitud);

        actualiatTask.execute();


    }


    private class listarEstadosTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Tarifas = Helper.BASE_URL_WS + "/vehiculo_asignado_servicio/listar_vehiculos_servicio";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("solicitud_id", txtCliente.getText().toString().split("-")[0]);

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_Tarifas, parametros);

                Log.e("JSON Catalogo", resultadoJSON);

                JSONObject jsonCon = new JSONObject(resultadoJSON);
                if (jsonCon.getBoolean("status")) {
                    //acceder a los datos de los productos
                    JSONArray jsonArrayProductos = jsonCon.getJSONArray("data");

                    //limpiar el almacen de datos de los productos
                    listaTarifassWS.clear();

                    for (int i = 0; i < jsonArrayProductos.length(); i++) {
                        //capturar los datos de cada producto
                        JSONObject jsonConductores = jsonArrayProductos.getJSONObject(i);

                        //instanciar un objeto de la clase producto para guardar los datos
                        Vehiculo tarifa = new Vehiculo();
                        tarifa.setPlaca(jsonConductores.getString("placa"));
                        tarifa.setConductor(jsonConductores.getString("chofer"));
                        tarifa.setNum_brevete(jsonConductores.getString("num_brevete"));
                        tarifa.setEstado("Vehiculo de "+jsonConductores.getString("num_ruedas")+" ruedas");

                        //almacenar el objeto "producto" en el almacen de datos
                        listaTarifassWS.add(tarifa);
                    }
                    //retornar un valor verdadero(true)
                    resultado = true;
                } else {
                    //retornar un valor falso (false), cuando no hay registro en el catalogo de productos
                    dialog.dismiss();
                    resultado = false;
                }
            } catch (Exception e) {
                //Toast.makeText(CatalogoProductoFragment.this.getContext(), "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d("ERROR APP COMERCIAL", e.getMessage());
                dialog.dismiss();
                e.printStackTrace();
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) { //true
                //se ha descargado el catalogo correctamente
                //enviar los datos almacenados en el ArrayList al adaptador
                adaptador.cargarVehiculos(listaTarifassWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }
        }
    }

    private void listarTarifas() {
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de vehiculos asignados, por favor espere");
        dialog.setCancelable(false);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaTarifassWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorListadoVehiculos(this);
        recycler.setAdapter(adaptador);

        //ejecutar la clase
        new dialog_gestion_asignacion_Vehiculo_conductor.listarEstadosTask().execute();
    }

}