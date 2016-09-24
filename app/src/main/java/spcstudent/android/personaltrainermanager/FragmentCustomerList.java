package spcstudent.android.personaltrainermanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class FragmentCustomerList extends Fragment{
    private RecyclerView mCustomerRecyclerView;
    private CustomerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_list_fragment, container, false);
        setRetainInstance(true);

        mCustomerRecyclerView = (RecyclerView) view.findViewById(R.id.customer_list_fragment);
        mCustomerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        CustomerList customerList = CustomerList.get(getActivity());
        List<Customer> customers = customerList.getCustomers();

        if (mAdapter == null) {
            mAdapter = new CustomerAdapter(customers);
            mCustomerRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCustomers(customers);
            mAdapter.notifyDataSetChanged();
        }
    }

    // ViewHolder for customer list recycler view
    private class CustomerHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mNameTextView;
        private ImageView mProfileImageView;
        private Customer mCustomer;
        private File mPhotoFile;

        public CustomerHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mNameTextView = (TextView) v.findViewById(R.id.full_name_textview);
            mProfileImageView = (ImageView) v.findViewById(R.id.customer_icon);
        }

        public void bindCustomer(Customer c) {
            mCustomer = c;
            mPhotoFile = CustomerList.get(getActivity()).getPhotoFile(c);
            mNameTextView.setText(mCustomer.getFirstName() + " " + mCustomer.getLastName());
            // link to profile imaage
            updatePhotoView();
        }

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(getActivity(), CustomerPage.class);
            Bundle myBundle = new Bundle();
            myBundle.putSerializable(ApplicationKeys.CUSTOMER_ID, mCustomer.getCustomerId());
            myIntent.putExtras(myBundle);
            getActivity().startActivity(myIntent);
        }

        private void updatePhotoView() {
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mProfileImageView.setImageResource(R.mipmap.profile_img);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                        getActivity());
                mProfileImageView.setImageBitmap(bitmap);
            }
        }
    }

    // Adapter for customer list recycler view
    private class CustomerAdapter extends RecyclerView.Adapter<CustomerHolder> {
        private List<Customer> mCustomers;

        public CustomerAdapter(List<Customer> customers) {
            mCustomers = customers;
        }

        @Override
        public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.listitem_customer, parent, false);
            return new CustomerHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomerHolder holder, int position) {
            Customer c = mCustomers.get(position);
            holder.bindCustomer(c);
        }

        @Override
        public int getItemCount() {
            return mCustomers.size();
        }

        public void setCustomers(List<Customer> customers) {
            mCustomers = customers;
        }
    }
}