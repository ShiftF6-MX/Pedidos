package com.gruposhift.cpedidos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gruposhift.cpedidos.R;
import com.gruposhift.cpedidos.model.DVenta;

import java.util.List;

public class AdaptadorAddPedido extends BaseAdapter {

    private Context context;
    private int layout;
    private List<DVenta> dVentas;

    public AdaptadorAddPedido(Context context, int layout, List<DVenta> dVentas) {
        this.context = context;
        this.layout = layout;
        this.dVentas = dVentas;
    }

    @Override
    public int getCount() {
        return dVentas.size();
    }

    @Override
    public Object getItem(int position) {
        return dVentas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LayoutInflater layoutInflater =LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.list_addpedido, null);

        DVenta dventa = dVentas.get(position);

        TextView nombreProducto = (TextView) view.findViewById(R.id.nomProducto);
        TextView cantidad = (TextView) view.findViewById(R.id.cantidad);
        TextView unidad = (TextView) view.findViewById(R.id.unidad);

        nombreProducto.setText("Producto: "  + dventa.getPedido().getDescripcion());
        cantidad.setText("Cantidad: " + dventa.getCantidad());
        unidad.setText("Unidad: " + dventa.getUnidad());

        return view;
    }
}
