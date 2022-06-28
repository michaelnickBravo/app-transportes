package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListaClientes;

import pe.edu.usat.laboratorio.appcomercial.logica.Cliente;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;



public class clientes  extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView clientesRecyclerView;
    EditText EditTextBusqueda;
    Spinner spinnerTipoBusqueda;

    ArrayList<Cliente> listaServicioWS;
    AdaptadorListaClientes adaptador;

    ProgressDialog dialog;
    SwipeRefreshLayout swipeRefreshLayoutSC;


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

        //configurar los controles
        clientesRecyclerView = view.findViewById(R.id.recycler_view_clientes);
        clientesRecyclerView.setHasFixedSize(true);
        clientesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        //llamar al metodo para mostrar el catalogo
        listarClientes();
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        //refescar el catalogo de productos
        listarClientes();
        //cerrar la animacion del swipRefreshLayout
        this.swipeRefreshLayoutSC.setRefreshing(false);
    }

    private class clientesTask extends AsyncTask<Void, Void, Boolean> {
        public int getTipoB() {
            return tipoB;
        }

        public void setTipoB(int tipoB) {
            this.tipoB = tipoB;
        }

        public String getBusqueda() {
            return busqueda;
        }

        public void setBusqueda(String busqueda) {
            this.busqueda = busqueda;
        }

        public  int tipoB=0;
        public  String busqueda;

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_clientes="";
                HashMap<String, String> parametros = new HashMap<>();
                Log.e("TIPO", String.valueOf(tipoB));
                Log.e("ESTADO", String.valueOf(busqueda));
                if(tipoB==0){
                    URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/listar";
                    //parametros.put("token", Sesion.TOKEN);

                }else{
                    if(tipoB==1){
                        URL_WS_clientes = Helper.BASE_URL_WS + "/cliente/listar/estado";
                        //parametros.put("token", Sesion.TOKEN);
                        parametros.put("estado_id", busqueda);
                    }
                }


                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_clientes, parametros);

                //Log.e("JSON Solicitud Carga", resultadoJSON);

                JSONObject jsonclientes = new JSONObject(resultadoJSON);
                if (jsonclientes.getBoolean("status")) {
                    //acceder a los datos
                    JSONArray jsonArrayClientes = jsonclientes.getJSONArray("data");

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

            }
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
        clientesTask clasee=new clientesTask();

        new clientesTask().execute();

    }
}