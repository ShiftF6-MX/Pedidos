package com.gruposhift.cpedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gruposhift.cpedidos.adapters.AdapterPedidos;
import com.gruposhift.cpedidos.constantes.URLs;
import com.gruposhift.cpedidos.model.Cliente;
import com.gruposhift.cpedidos.model.Venta;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private int anio, mes, dia;
    private Button insertar, actualizar;
    private TextView fechaFiltro;
    private ListView listaPedidos;
    private FloatingActionButton floatInsert;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpClient = new AsyncHttpClient();

        insertar = (Button) findViewById(R.id.btnNuevo);
        actualizar = (Button) findViewById(R.id.btnActualizar);
        floatInsert = (FloatingActionButton) findViewById(R.id.flotInsert);
        listaPedidos = (ListView) findViewById(R.id.lPedidos);
        fechaFiltro = (TextView) findViewById(R.id.fechaFiltro);


        leerPhp(URLs.readPedidosActivos);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busquedaFecha();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaFiltro.setText("Fecha");
                leerPhp(URLs.readPedidosActivos);
            }
        });

        floatInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Formulario.class);
                intent.putExtra("opcion", Formulario.INSERTAR);
                startActivity(intent);
            }
        });

        fechaFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarFecha();

            }
        });
    }

    private void filtrarFecha(){
        final Calendar fecha = Calendar.getInstance();
        anio = fecha.get(Calendar.YEAR);
        mes = fecha.get(Calendar.MONTH) ;
        dia = fecha.get(Calendar.DAY_OF_MONTH) + 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaFiltro.setText(year + "-" + (month + 1)  +"-" + dayOfMonth );
            }
        }, anio, mes, dia);
        datePickerDialog.show();
    }

    private boolean busquedaFecha(){
        if (fechaFiltro.getText()=="Fecha"){
            Toast.makeText(getApplicationContext(), "Especifique la Fecha", Toast.LENGTH_SHORT).show();
            return  false;
        }else {
            leerPhp(URLs.readPedidosActivosPorFecha + fechaFiltro.getText());
            return  true;
        }
    }

    public void leerPhp(String url){

        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    cargarDatos( new String(responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void cargarDatos(String response){

        final ArrayList<Venta> ventas = new ArrayList<Venta>();
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++){
                final Venta venta = new Venta();
                venta.setSysPK(array.getJSONObject(i).getInt("Sys_PK"));
                Cliente cliente = new Cliente();
                cliente.setSysPK(array.getJSONObject(i).getInt("ICliente"));
                cliente.setNombre(array.getJSONObject(i).getString("Nombre"));
                venta.setCliente(cliente);
                venta.setReferencia(array.getJSONObject(i).getString("Referencia"));
                venta.setFecha(array.getJSONObject(i).getString("Fecha"));
                ventas.add(venta);

             //   Toast.makeText(getApplicationContext(), "DAtos " + venta.getCliente().getNombre(), Toast.LENGTH_SHORT).show();

                AdapterPedidos adapterPedidos = new AdapterPedidos(this,R.layout.list_pedidos, ventas);
                listaPedidos.setAdapter(adapterPedidos);

                listaPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(MainActivity.this, Formulario.class);
                        intent.putExtra("sysPKVenta", ventas.get(position).getSysPK());
                        Toast.makeText(getApplicationContext(),"SysKP" + ventas.get(position).getSysPK() ,
                                Toast.LENGTH_SHORT).show();
                        intent.putExtra("sysPKCliente", ventas.get(position).getCliente().getSysPK());
                        intent.putExtra("fecha", ventas.get(position).getFecha());
                        intent.putExtra("opcion", Formulario.EDITAR);
                        startActivity(intent);
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
