package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListaClientes;

import pe.edu.usat.laboratorio.appcomercial.logica.Cliente;
import pe.edu.usat.laboratorio.appcomercial.logica.EstadoCliente;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;



public class clientes  extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView clientesRecyclerView;
    EditText EditTextBusqueda;
    Spinner spinnerTipoBusqueda;
    Spinner spinnerestados;
    Button botonBuscar;
    SwipeRefreshLayout swipeRefreshLayoutSC;

    ArrayList<Cliente> listaServicioWS;
    AdaptadorListaClientes adaptador;

    ProgressDialog dialog;



    public clientes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        //Mostrar un titulo para el fragment

        this.getActivity().setTitle("Lista de clientes");


        EditTextBusqueda = view.findViewById(R.id.edit_text_busqueda);
        spinnerTipoBusqueda=view.findViewById(R.id.spinner);
        spinnerestados=view.findViewById(R.id.estados);
        botonBuscar=view.findViewById(R.id.boton_buscar);
        botonBuscar.setOnClickListener(this);
        swipeRefreshLayoutSC = view.findViewById(R.id.swipeRefreshLayoutSC);
        //configurar los controles
        clientesRecyclerView = view.findViewById(R.id.recycler_view_clientes);
        clientesRecyclerView.setHasFixedSize(true);
        clientesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listarClientes();

        //llamar al metodo para mostrar el catalogo

        new clientes.estadoClienteTask().execute();




        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.boton_buscar:
                listarClientes();

        }

    }

    @Override
    public void onRefresh() {
        //refescar el catalogo de productos
        listarClientes();
        //cerrar la animacion del swipRefreshLayout
        this.swipeRefreshLayoutSC.setRefreshing(false);
    }

    private class clientesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_clientes="";
                HashMap<String, String> parametros = new HashMap<>();

                Log.e("TIPOOOO", String.valueOf(spinnerTipoBusqueda.getSelectedItem().toString()));
                String tipoB=spinnerTipoBusqueda.getSelectedItem().toString();
                String busqueda=EditTextBusqueda.getText().toString();
                String busquedaEstado=spinnerestados.getSelectedItem().toString().split("-")[0];
                if(tipoB.equalsIgnoreCase("Todos")){
                    URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/listar";
                    //parametros.put("token", Sesion.TOKEN);

                }else{
                    if(tipoB.equalsIgnoreCase("Nombre o razon social")){
                        URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/listar/nombre";
                        //parametros.put("token", Sesion.TOKEN);
                        parametros.put("nombre", busqueda);
                    }else{
                        if(tipoB.equalsIgnoreCase("DNI o RUC")){
                            URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/listar/doc";
                            //parametros.put("token", Sesion.TOKEN);
                            parametros.put("doc", busqueda);
                        }else{
                            if(tipoB.equalsIgnoreCase("Estado")){
                                URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/listar/estado";
                                //parametros.put("token", Sesion.TOKEN);

                                parametros.put("estado_id", busquedaEstado);
                            }
                        }
                    }}


                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_clientes, parametros);

                //Log.e("JSON Solicitud Carga", resultadoJSON);

                JSONObject jsonclientes = new JSONObject(resultadoJSON);
                if (jsonclientes.getBoolean("status")) {
                    //acceder a los datos
                    JSONArray jsonArrayClientes = jsonclientes.getJSONArray("data");

                    if (jsonclientes.getString("data").equalsIgnoreCase("No hay registros")){
                        dialog.setMessage("No se encontro datos");
                        dialog.hide();
                    }else{
                        //limpiar el almacen de datos
                        listaServicioWS.clear();

                        for (int i = 0; i < jsonArrayClientes.length(); i++) {
                            //capturar los datos
                            JSONObject jsonCarga = jsonArrayClientes.getJSONObject(i);

                            //instanciar un objeto para guardar los datos
                            Cliente cliente = new Cliente();
                            cliente.setNombre(jsonCarga.getString("nombre"));
                            cliente.setDocumento(jsonCarga.getString("num_documento"));
                            cliente.setTipo(jsonCarga.getString("tipo de documento"));
                            cliente.setTelefono(jsonCarga.getString("telefono"));
                            cliente.setEstado(jsonCarga.getString("estado"));
                            cliente.setDireccion(jsonCarga.getString("direccion"));


                            //almacenar el objeto en el almacen de datos
                            listaServicioWS.add(cliente);
                        }
                        //retornar un valor verdadero(true)
                        resultado = true;
                    }

                } else {
                    //retornar un valor falso (false), cuando no hay registro
                    //Toast.makeText(SolicitudesServicioCargaConductor.this.getContext(), "ACTUALMENTE NO SE ENCUENTRAN ATENDIENDO UNA CARGA", Toast.LENGTH_SHORT).show();
                    dialog.setMessage("ERROR");
                    dialog.setCancelable(true);
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

            if (resultado) { //true
                //se ha descargado el catalogo correctamente
                //enviar los datos almacenados en el ArrayList al adaptador
                adaptador.cargarListaSolicitudes(listaServicioWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }else{ dialog.dismiss();}
        }
    }

    private void listarClientes() {
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos



        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        dialog.setMessage("Descargando la lista de clientes, por favor espere");

        dialog.setCancelable(true);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaServicioWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorListaClientes(this.getContext());
        clientesRecyclerView.setAdapter(adaptador);

        //ejecutar la clase


        new clientesTask().execute();

    }

    private class estadoClienteTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_clientes="";
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

                    for(int i=0; i<jsonArray.length(); i++){
                        //capturar los datos de cada
                        JSONObject jsonDatosestados= jsonArray.getJSONObject(i);

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
                    dialog.setMessage("ERROR");
                    dialog.setCancelable(true);
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
                            getActivity().getApplicationContext(),
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


}