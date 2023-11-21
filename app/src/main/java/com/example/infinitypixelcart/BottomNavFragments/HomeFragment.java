package com.example.infinitypixelcart.BottomNavFragments;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.infinitypixelcart.API.FetchingProductsApi;
import com.example.infinitypixelcart.Adapter.ProductAdapter;
import com.example.infinitypixelcart.Intents.OnProductSearch;
import com.example.infinitypixelcart.Intents.ProductViewer;
import com.example.infinitypixelcart.MainActivity;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.RetrofitService;
import com.example.infinitypixelcart.Service.TokenManager;
import com.example.infinitypixelcart.ViewModels.ProductsViewModel;
import com.example.infinitypixelcart.model.ProductDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    RecyclerView recyclerView;

    Toolbar toolbar;
    List<ProductDTO> products = null;

    SearchView searchView;

    ProductsViewModel productsViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toolbar=view.findViewById(R.id.homeToolbar);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        searchView = view.findViewById(R.id.searchProduct);
        recyclerView = view.findViewById(R.id.product_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView.clearFocus();
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        productsViewModel.setContext(getContext());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle= new Bundle();
                bundle.putString("productName",query.trim());
                Intent intent = new Intent(getContext(), OnProductSearch.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_custom_tool_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_logout:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                TokenManager.getInstance(getContext()).clearToken();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "You Have Been Logged Out", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}