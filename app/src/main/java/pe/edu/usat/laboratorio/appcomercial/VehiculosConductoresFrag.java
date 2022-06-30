package pe.edu.usat.laboratorio.appcomercial;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class VehiculosConductoresFrag extends Fragment implements View.OnClickListener{

    Button btnRegistrarConductor, btnRegistrarVehiculos, btnListarConductor, btnListarVehiculos, btnAsignarVehiculosConductores, btnListarVehiculosConductores;

    public VehiculosConductoresFrag() {
        // Required empty public constructor
    }


    public static VehiculosConductoresFrag newInstance(String param1, String param2) {
        VehiculosConductoresFrag fragment = new VehiculosConductoresFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vehiculos_conductores, container, false);

        btnRegistrarConductor = view.findViewById(R.id.btnRegistrarConductores);
        btnRegistrarConductor.setOnClickListener(this);

        btnListarConductor = view.findViewById(R.id.btnListarConductores);
        btnListarConductor.setOnClickListener(this);

        btnRegistrarVehiculos = view.findViewById(R.id.btnRegistrarVehiculos);
        btnRegistrarVehiculos.setOnClickListener(this);

        btnListarVehiculos = view.findViewById(R.id.btnListarVehiculos);
        btnListarVehiculos.setOnClickListener(this);

        btnAsignarVehiculosConductores = view.findViewById(R.id.btnAsignarVehiculosConductores);
        btnAsignarVehiculosConductores.setOnClickListener(this);

        btnListarVehiculosConductores = view.findViewById(R.id.btnListarVehiculosConductores);
        btnListarVehiculosConductores.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegistrarConductores:
                Intent intent= new Intent(getContext(), RegistrarConductores.class);
                startActivity(intent);
                break;
            case R.id.btnListarConductores:
                ListadoConductoresO fragment = new ListadoConductoresO();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedor, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.btnRegistrarVehiculos:
                Intent intent1= new Intent(getContext(), RegistroVehiculos.class);
                startActivity(intent1);
                break;
            case R.id.btnListarVehiculos:
                ListaTodosVehiculos fragment2 = new ListaTodosVehiculos();
                FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.contenedor, fragment2);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;
            case R.id.btnAsignarVehiculosConductores:
                Intent intent2 = new Intent(getContext(), AsignarVehiculoConductor.class);
                startActivity(intent2);
                break;
            case R.id.btnListarVehiculosConductores:
                ListadoVehiculosO fragment1 = new ListadoVehiculosO();
                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.contenedor, fragment1);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;
        }
    }
}