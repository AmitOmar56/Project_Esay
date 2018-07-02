package com.cleaner.esaymart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.model.Cart;

import java.util.ArrayList;
import java.util.List;

import static com.cleaner.esaymart.activity.HomeActivity.itemofcart;


/**
 * Created by user on 12/22/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context mContext;
    private List<Cart> cartList;

    //declare interface
    private CartAdapter.Product_OnItemClicked onClick;

    private CartAdapter.Product_OnClicked onClicknew;
    public static int item = 1;
    private String select_item;

    //    =======================================================
    private CartAdapter.Product_On_spin_Clicked onClick_spin_new;

    public interface Product_On_spin_Clicked {
        void Product_on_spin_Clicked(int position);
    }

    //    ==========================================================

    public interface Product_OnClicked {
        void Product_onClick(int position);
    }

    //make interface like this
    public interface Product_OnItemClicked {
        void Product_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView product_name, product_price, cart_quantity;
        private ImageView product_image;
        private LinearLayout cartLinearLayout, removeLayout;
        private Spinner spinner;

        public MyViewHolder(View view) {
            super(view);
            product_price = (TextView) view.findViewById(R.id.cart_price);
            product_name = (TextView) view.findViewById(R.id.cart_name);
            product_image = (ImageView) view.findViewById(R.id.cartImage);
            cartLinearLayout = (LinearLayout) view.findViewById(R.id.cartLinearLayout);
            cart_quantity = (TextView) view.findViewById(R.id.cart_quantity);
            removeLayout = (LinearLayout) view.findViewById(R.id.removeLayout);
            spinner = (Spinner) view.findViewById(R.id.spinner);
        }
    }

    public CartAdapter(Context mContext, List<Cart> cartList) {
        this.mContext = mContext;
        this.cartList = cartList;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_cardview, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {

        final Cart cart = cartList.get(position);
        holder.product_name.setText(cart.getProduct_name());
        holder.product_price.setText("Rs. " + item * cart.getProduct_price() + "");
        //  holder.cart_quantity.setText(cart.getProduct_quantity() + "");
        Glide.with(mContext).load(cart.getProduct_image()).into(holder.product_image);
        Log.d("Image", cart.getProduct_image());

        holder.cartLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.Product_onItemClick(position);
            }
        });

        holder.removeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicknew.Product_onClick(position);
                removeItem(position);
                itemofcart--;
            }
        });
        // Spinner Drop down elements
        List<String> quantity = new ArrayList<String>();
        quantity.add("1");
        quantity.add("2");
        quantity.add("3");
        quantity.add("4");
        quantity.add("5");
        quantity.add("6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, quantity);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        holder.spinner.setAdapter(dataAdapter);
        holder.spinner.setSelection(cart.getProduct_quantity() - 1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_item = adapterView.getItemAtPosition(i).toString();
                item = Integer.parseInt(select_item);
                holder.product_price.setText("Rs. " + item * cart.getProduct_price() + "");
                onClick_spin_new.Product_on_spin_Clicked(position);
                Toast.makeText(mContext, item + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    //
    private void removeItem(int position) {
        cartList.remove(position);
//        // notify the item removed by position
//         to perform recycler view delete animations
//         NOTE: don't call notifyDataSetChanged()
        notifyDataSetChanged();
        notifyItemRemoved(position);
//
    }

    public void setOn_Spin_Click(CartAdapter.Product_On_spin_Clicked onClick_spin_new) {
        this.onClick_spin_new = onClick_spin_new;
    }

    public void setOnClick(CartAdapter.Product_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    public void setClick(CartAdapter.Product_OnClicked onClicknew) {
        this.onClicknew = onClicknew;
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

}

