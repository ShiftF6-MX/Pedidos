package com.gruposhift.cpedidos;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.gruposhift.cpedidos.adapters.AdaptadorAddPedido;
import com.gruposhift.cpedidos.constantes.URLs;
import com.gruposhift.cpedidos.dialogs.DialogFormulario;
import com.gruposhift.cpedidos.model.BlockDocumento;
import com.gruposhift.cpedidos.model.Cliente;
import com.gruposhift.cpedidos.model.DVenta;
import com.gruposhift.cpedidos.model.FoliosDocumentos;
import com.gruposhift.cpedidos.model.Pedido;
import com.gruposhift.cpedidos.model.Producto;
import com.gruposhift.cpedidos.model.Venta;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class Formulario extends AppCompatActivity  implements DialogFormulario.DialogoFormularioListener {

    public final static int INSERTAR = 1;
    public final static int EDITAR = 2;

    private int anio, mes, dia;
    private EditText fechaEdt, edtCantiad;
    private Spinner spCliente, spProducto, spCantidad;
    private Button agregar, guardar;
    private ListView listNuevoPedido;
    private AsyncHttpClient httpClient;
    private ArrayList<DVenta> dVentas = new ArrayList<DVenta>();
    private ArrayList<Cliente> clientes;
    private ArrayList<Pedido> pedidos;
    private int ultimo, sysPKVenta, opcion;
    private String serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        httpClient = new AsyncHttpClient();
        spCliente = (Spinner) findViewById(R.id.spCliente);
        spProducto = (Spinner) findViewById(R.id.spProducto);
        spCantidad = (Spinner) findViewById(R.id.spUnidad);
        fechaEdt = (EditText) findViewById(R.id.edtFecha);
        edtCantiad = (EditText)findViewById(R.id.edtCantidad);
        agregar = (Button) findViewById(R.id.btnAgregarPrdocuto);
        listNuevoPedido = (ListView) findViewById(R.id.listNPedido);
        guardar = (Button) findViewById(R.id.btnGuardarPedido);

        recuperarPeticion();
        spinnerEstatico();


        fechaEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validarFormularioAgregarLista()){
                    recuperarFormulario();
                    Toast.makeText(getApplicationContext(), "Producto a la lista", Toast.LENGTH_SHORT).show();
                    limpiarFormulario();
                    spCliente.setEnabled(false);

                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // abrirDialogoProducto();
                if (validarFormularioCrearPedido()){
                    actualizarUltimo(URLs.readBlockDocumento);
                }
            }
        });
    }

    private void recuperarPeticion(){
        Bundle bundle = getIntent().getExtras();
        int opcion = bundle.getInt("opcion");

        if(opcion == INSERTAR){
            llamarPHP(URLs.readClientes, 1);
            llamarPHP(URLs.readPRoductos, 2);
            edtCantiad.setText("");
            fechaEdt.setText("");
        }else if(opcion == EDITAR){

            int sysPK = bundle.getInt("sysPKVenta");
            Toast.makeText(getApplicationContext(),"SysKP" + sysPK ,
                    Toast.LENGTH_SHORT).show();
            int cliente = bundle.getInt("sysPKCliente");
            spCliente.setEnabled(false);
            String fecha = bundle.getString("fecha");
            fechaEdt.setText(fecha);
            llamarPHP(URLs.readClienteSysPK + cliente, 1);
            llamarPHP(URLs.readDetalleVentaSysPK + sysPK, 3);
            Toast.makeText(getApplicationContext(), URLs.readDetalleVentaSysPK+sysPK , Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        final Calendar fecha = Calendar.getInstance();
        anio = fecha.get(Calendar.YEAR);
        mes = fecha.get(Calendar.MONTH) ;
        dia = fecha.get(Calendar.DAY_OF_MONTH) + 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaEdt.setText(year + "-" + (month+1)  +"-" + dayOfMonth );
            }
        }, anio, mes, dia);
        datePickerDialog.show();
    }

    public void llamarPHP(String url, final int opcion){
        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    llenarSpiners(new String(responseBody), opcion);
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void llenarSpiners(String respose, int opcion){
        clientes = new ArrayList<Cliente>();
        pedidos = new ArrayList<Pedido>();

        try {
            JSONArray arrayRespues = new JSONArray(respose);

            if (opcion == 1){

                for (int i=0; i < arrayRespues.length(); i++){
                    Cliente cliente = new Cliente();
                    cliente.setSysPK(arrayRespues.getJSONObject(i).getInt("Sys_PK"));
                    cliente.setNombre(arrayRespues.getJSONObject(i).getString("Nombre"));
                    clientes.add(cliente);
                }
                ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(this, android.R.layout.simple_dropdown_item_1line,clientes);
                spCliente.setAdapter(adapter);
            } else if (opcion == 2){

                for (int i=0; i < arrayRespues.length(); i++){

                    Pedido pedido = new Pedido();
                    pedido.setSysPK(arrayRespues.getJSONObject(i).getInt("Sys_PK"));
                    pedido.setDescripcion(arrayRespues.getJSONObject(i).getString("Descripcion"));
                    pedidos.add(pedido);
                }
                ArrayAdapter<Pedido> adapter = new ArrayAdapter<Pedido>(this, android.R.layout.simple_dropdown_item_1line, pedidos);
                spProducto.setAdapter(adapter);
            }

            if (opcion == 3){
                llamarPHP(URLs.readPRoductos, 2);
                for (int i = 0; i < arrayRespues.length(); i++){
                    final DVenta dVenta = new DVenta();
                    Pedido pedido = new Pedido();
                    pedido.setDescripcion(arrayRespues.getJSONObject(i).getString("Descripcion"));
                    dVenta.setPedido(pedido);
                    dVenta.setUnidad(arrayRespues.getJSONObject(i).getString("Unidad"));
                    dVenta.setCantidad(arrayRespues.getJSONObject(i).getDouble("Notas"));
                    dVentas.add(dVenta);

                    Toast.makeText(getApplicationContext() ," Datos :" +dVentas.get(i).getPedido().getDescripcion()
                    + " " + dVentas.get(i).getUnidad()+ " " +  dVentas.get(i).getCantidad(),Toast.LENGTH_SHORT).show();
                    AdaptadorAddPedido adaptadorAddPedido = new AdaptadorAddPedido(this, R.layout.list_addpedido, dVentas);
                    listNuevoPedido.setAdapter(adaptadorAddPedido);

                    listNuevoPedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String cantidad = Double.toString(dVentas.get(position).getCantidad());
                       //     spCantidad.setSelection(dVentas.get(position).getCantidad());
                            spProducto.setSelection(position);
                            //spProducto.getSelectedItem(dVentas.get(position).getPedido().getSysPK());
                            //spProducto.setOnItemClickListener(this);
                            Toast.makeText(getApplicationContext(), "Aca la posisicone de lvetas : "+ position,
                                    Toast.LENGTH_SHORT).show();
                            edtCantiad.setText(cantidad);


                        }
                    });


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spinnerEstatico(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unidad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantidad.setAdapter(adapter);
    }

    private void recuperarFormulario(){

        Cliente cliente = (Cliente) spCliente.getSelectedItem();
        Pedido producto = (Pedido) spProducto.getSelectedItem();

        DVenta dVenta = new DVenta();

        Venta venta = new Venta();
        cliente.setSysPK(cliente.getSysPK());
        venta.setCliente(cliente);

        producto.setSysPK(producto.getSysPK());
        producto.setDescripcion(producto.getDescripcion());
        dVenta.setPedido(producto);
        dVenta.setCantidad(Double.parseDouble(edtCantiad.getText().toString()));
        String unidad = spCantidad.getSelectedItem().toString();
        dVenta.setUnidad(unidad);

        cliente.setSysPK(cliente.getSysPK());
        dVentas.add(dVenta);

        AdaptadorAddPedido adapter = new AdaptadorAddPedido(this, R.layout.list_addpedido, dVentas );
        listNuevoPedido.setAdapter(adapter);

    }

    private  boolean validarFormularioAgregarLista(){
        if(edtCantiad.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Complete el campo Cantidad " , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validarFormularioCrearPedido(){

        if(dVentas.isEmpty()){
            Toast.makeText(getApplicationContext(), "Debe agregar producto para hacer un pedido", Toast.LENGTH_SHORT).show();
            return  false;
        } else if(fechaEdt.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Debe Seleccionar la una Fecha para el pedido", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return  true;
    }

    private void actualizarUltimo(String url){

        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){

                   actualizarUltimo2(new String(responseBody));

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarUltimo2(String response){
        try {
            JSONArray jsonObject = new JSONArray(response);
            for (int i  = 0; i < jsonObject.length(); i++) {

                BlockDocumento blockDocumento = new BlockDocumento();
                blockDocumento.setSysPK(jsonObject.getJSONObject(i).getInt("Sys_PK"));
                blockDocumento.setFultimo((jsonObject.getJSONObject(i).getInt("FUltimo"))+1);
                blockDocumento.setSerie(jsonObject.getJSONObject(i).getString("Serie"));

               String url = URLs.updateBlockDocumento +
                       "FUltimo=" + blockDocumento.getFultimo() + "&Sys_PK=" + blockDocumento.getSysPK();

                actualizarURL(url, blockDocumento, 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void actualizarURL(String url, final BlockDocumento blockDocumento,int opcion) {

        if(opcion == 1 ){

            httpClient.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200){
                        String url = URLs.insertFoliosDocumento + "Folio=" +blockDocumento.getFultimo();
                        actualizarURL(url, blockDocumento, 2);
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(),"Error al actulizar", Toast.LENGTH_SHORT).show();
                }
            });
        } else if(opcion == 2){

            httpClient.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200){
                        String url = URLs.readUltimoFolioDocumento;
                        actualizarURL(url, blockDocumento, 3);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(),"Error al actulizar", Toast.LENGTH_SHORT).show();
                }
            });
        } else  if (opcion == 3){
            httpClient.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200){
                        recuperarFolio(new String(responseBody), blockDocumento);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

    }

    private void recuperarFolio(String response, BlockDocumento blockDocumento){
        try {
            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++){
                FoliosDocumentos foliosDocumentos = new FoliosDocumentos();
                foliosDocumentos.setSysPK(array.getJSONObject(i).getInt("Sys_PK"));
                blockDocumento.setSysPK(blockDocumento.getSysPK());
                blockDocumento.setSerie(blockDocumento.getSerie());
                blockDocumento.setFultimo(blockDocumento.getFultimo());
                foliosDocumentos.setBlockDocumentos(blockDocumento);

                String urlVenta = URLs.insertVenta;
                insertVenta(foliosDocumentos, urlVenta);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void insertVenta(FoliosDocumentos documentos, String url){

        Cliente cliente = (Cliente) spCliente.getSelectedItem();
        cliente.setSysPK(cliente.getSysPK());
        String referencia = documentos.getBlockDocumentos().getSerie() + documentos.getBlockDocumentos().getFultimo();

        String parametros = "Fecha="+ fechaEdt.getText() + "&Referencia=" + referencia + "&ICliente=" + cliente.getSysPK() +
                            "&IFolio=" + documentos.getSysPK();
        httpClient.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(getApplicationContext(), "Se hizo el insert De venta", Toast.LENGTH_SHORT).show();
                    llamarVenta(URLs.readUltimaVeta);
                } else  {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error en la venta", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void llamarVenta(String url){

        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    recupearVenta(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private  void recupearVenta(String response){
        try {
            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++){

                Venta venta = new Venta();
                venta.setSysPK(array.getJSONObject(i).getInt("Sys_PK"));
                Toast.makeText(getApplicationContext(), "Sys de la ultima venta " +
                        venta.getSysPK(), Toast.LENGTH_SHORT).show();
                insertDetalleventa(venta);
            }

        }catch (JSONException e){

        }

    }

    private void insertDetalleventa(Venta venta){

        String url = URLs.insertDetalleVenta;

        for(int i = 0; i < dVentas.size(); i++){
            Pedido pedido = new Pedido();

            String pa =  "IProducto=" + dVentas.get(i).getPedido().getSysPK() +
                         "&FK_Venta_Detalle=" + venta.getSysPK() +
                         "&Notas=" + dVentas.get(i).getCantidad() +
                         "&Unidad="+ dVentas.get(i).getUnidad();


            String parametro = url + pa;
            insertarDetalle(parametro);

        }
        Toast.makeText(getApplicationContext(), "Pedido creado con exito", Toast.LENGTH_SHORT).show();

    }

   private void  insertarDetalle(String parametro){

       httpClient.post(parametro, new AsyncHttpResponseHandler() {
           @Override
           public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               if (statusCode == 200){

               }else  {
                   Toast.makeText(getApplicationContext(), "Error, no se pudo guardar el registro", Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

           }
       });
    }

    private void limpiarFormulario(){
        fechaEdt.setText("");
        edtCantiad.setText("");
    }

    public void abrirDialogoProducto (){
        DialogFormulario formulario = new DialogFormulario();
        formulario.show(getSupportFragmentManager(), "Formulario ejemplo");
    }

    @Override
    public void enviarDatos(String texto) {
        fechaEdt.setText(texto);
    }

    private void guardaPedido(){
        llamarPHP("", 3);
    }
}
