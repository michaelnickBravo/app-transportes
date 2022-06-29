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

    Button btnRegistrarConductor, btnRegistrarVehiculos, btnListarConductor, btnListarVehiculos;

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

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegistrarConductores:
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
                break;

            case R.id.btnListarVehiculos:
                break;
        }
    }
}