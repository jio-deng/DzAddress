package com.dz.address.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dz.address.R;
import com.dz.address.bean.AddressBean;

import java.util.ArrayList;

/**
 * @Description adapter for address list
 * Created by deng on 2018/8/29.
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder> {

    private IAddressAdapter iAddressAdapter;
    private ArrayList<AddressBean> addressList;

    public AddressListAdapter(IAddressAdapter iAddressAdapter) {
        this.iAddressAdapter = iAddressAdapter;
        addressList = new ArrayList<>();
    }

    public void setAddressList(ArrayList<AddressBean> list) {
        addressList = list;
        notifyDataSetChanged();
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(iAddressAdapter.getContext()).inflate(R.layout.item_address_list, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddressViewHolder holder, int position) {
        AddressBean addressBean = addressList.get(position);
        holder.mTvName.setText(addressBean.name);
        holder.mTvPhone.setText(addressBean.phone);
        holder.mTvAddress.setText(addressBean.address);
        if (!TextUtils.isEmpty(addressBean.tags)) {
            holder.mTvTag.setVisibility(View.VISIBLE);
            holder.mTvTag.setText(addressBean.tags);
        } else {
            holder.mTvTag.setVisibility(View.INVISIBLE);
        }
        holder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAddressAdapter.gotoEdit(addressList, holder.getAdapterPosition());
            }
        });
        holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAddressAdapter.gotoDelete(addressList, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvName;
        private TextView mTvPhone;
        private TextView mTvAddress;
        private TextView mTvTag;
        private TextView mTvEdit;
        private TextView mTvDelete;

        AddressViewHolder(View itemView) {
            super(itemView);

            mTvName = itemView.findViewById(R.id.tv_name);
            mTvPhone = itemView.findViewById(R.id.tv_phone);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvTag = itemView.findViewById(R.id.tv_tag);
            mTvEdit = itemView.findViewById(R.id.tv_btn_edit);
            mTvDelete = itemView.findViewById(R.id.tv_btn_delete);
        }
    }

    public interface IAddressAdapter {
        Context getContext();
        void gotoEdit(ArrayList<AddressBean> addressList, int position);
        void gotoDelete(ArrayList<AddressBean> addressList, int position);
    }
}
