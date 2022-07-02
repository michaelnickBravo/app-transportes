package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.EstadoCliente;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class dialogestadoCliente extends AppCompatActivity implements View.OnClickListener {

    TextView txtNombre, txtDireccion, txtDocumento, txttipo, txtTelefono, txtEstado;
    Button btnActualizar, btnCancelar;
    Spinner spinnerestados;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogestado_cliente);
        Bundle extras = getIntent().getExtras();



        this.setTitle("Actualizar estado");


        txtNombre = findViewById(R.id.txt_nombre);
        txttipo = findViewById(R.id.txt_tipo);
        txtDocumento = findViewById(R.id.txt_dni);
        txtDireccion = findViewById(R.id.txt_direccion);
        txtTelefono = findViewById(R.id.txt_telefono);
        txtEstado = findViewById(R.id.txt_estado);
        spinnerestados = findViewById(R.id.estadoscliente);
        btnActualizar = findViewById(R.id.boton_actualizar);
        btnCancelar = findViewById(R.id.boton_cencelar);
        btnActualizar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        txtNombre.setText(extras.getString("nombre").toUpperCase());
        txttipo.setText(extras.getString("tipo"));
        txtDocumento.setText(extras.getString("dni"));
        txtDireccion.setText(extras.getString("direccion").toUpperCase());
        txtTelefono.setText(extras.getString("telefono"));
        txtEstado.setText(extras.getString("estado").toUpperCase());


        new dialogestadoCliente.estadoClienteTask().execute();


    }

    public dialogestadoCliente() {
        // Required empty public constructor
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.boton_actualizar:
                Asignar();
                break;

            case R.id.boton_cencelar:
                Intent intent = new Intent(dialogestadoCliente.this, MainActivityOficinista.class);
                startActivity(intent);

                break;


        }

    }


    private class estadoClienteTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_clientes = "";
                HashMap<String, String> parametros = new HashMap<>();

                URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/estados";
                //parametros.put("token", Sesion.TOKEN);

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_clientes, parametros);

                JSONObject jsonestados = new JSONObject(resultadoJSON);
                if (jsonestados.getBoolean("status")) {
                    //acceder a los datos

                    JSONArray jsonArray = jsonestados.getJSONArray("data");

                    //limpiar el almacen de datos clientes
                    EstadoCliente.listaEstadosCliente.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //capturar los datos de cada
                        JSONObject jsonDatosestados = jsonArray.getJSONObject(i);

                        //instanciar un objeto de la clase para guardar los datos
                        EstadoCliente estado = new EstadoCliente();
                        estado.setId(jsonDatosestados.getString("id"));
                        estado.setNombre(jsonDatosestados.getString("nombre"));
                        estado.listaEstadosCliente.add(estado);
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
                if(resultado){ //true=verdadero
                    //declarar un array para almacenar solo las placas
                    String estadosClientes[] = new String[EstadoCliente.listaEstadosCliente.size()];

                    for (int i=0; i<EstadoCliente.listaEstadosCliente.size(); i++){
                        estadosClientes[i] = EstadoCliente.listaEstadosCliente.get(i).getId()+"-"+EstadoCliente.listaEstadosCliente.get(i).getNombre();
                    }

                    //crear un adaptador para cargar los datos en el spinner
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                            dialogestadoCliente.this.getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            estadosClientes
                    );
                    //asignar el adaptador al spinner
                    spinnerestados.setAdapter(adaptador);

                }else {
                    //Toast.makeText(AsignarVehiculoConductor.this, "No hay vehiculos", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private class actualizarTask extends AsyncTask<Void, Void, Boolean>{

        private String id_estado;
        private String doc_cliente;


        public actualizarTask(String id_estado, String doc_cliente) {
            this.id_estado = id_estado;
            this.doc_cliente = doc_cliente;

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado= false;
            try {
                String URL_WS_Asignar = Helper.BASE_URL_WS + "/cliente/actualizar/estado";
                HashMap<String,String> parametros = new HashMap<>();

                parametros.put("estado_id", this.id_estado);
                parametros.put("num_documento", this.doc_cliente);


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
                final AlertDialog.Builder dialog = new AlertDialog.Builder(dialogestadoCliente.this);
                dialog.setTitle("Se actualizo el estado del cliente correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(dialogestadoCliente.this, MainActivityOficinista.class);
                        startActivity(intent);
                    }
                });
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(dialogestadoCliente.this, "No se pudo actualizar el estado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Asignar() {
        final String id_estado = String.valueOf(spinnerestados.getSelectedItem().toString().split("-")[0]);
        final String doc = String.valueOf(txtDocumento.getText());


        dialogestadoCliente.actualizarTask actualiatTask = new dialogestadoCliente.actualizarTask(id_estado, doc);
        txtEstado.setText(spinnerestados.getSelectedItem().toString().split("-")[1]);
        actualiatTask.execute();


    }

}





