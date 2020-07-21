package com.jiayou.githubsuser.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiayou.githubsuser.GithubAdapter;
import com.jiayou.githubsuser.R;
import com.jiayou.githubsuser.model.ItemResponse;
import com.jiayou.githubsuser.services.ApiInterface;
import com.jiayou.githubsuser.services.ServiceGenerator;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {
    private RecyclerView recyclerView;
    private GithubAdapter fadapter;
    ItemResponse itemResponse;
    LinearLayoutManager mLayoutManager;
    String username;
    ProgressBar progressBar;
    TextView notFound;

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view  = inflater.inflate(R.layout.fragment_following, container, false);
        tampilFollowing(view);
        return view;

    }


    private void tampilFollowing(View view){
        recyclerView = view.findViewById(R.id.rv_following);
        progressBar = view.findViewById(R.id.progressBar);
        notFound = view.findViewById(R.id.notFolowing);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        itemResponse = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra("itemm");

        progressBar.setVisibility(View.VISIBLE);
        String token = "03e6965398e6246b8501510daa726a42013516a4";
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Token "+token);
        Call<List<ItemResponse>> call = service.getFollowing(Objects.requireNonNull(itemResponse).getLogin());
        call.enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                if (response.isSuccessful()) {
                    List<ItemResponse> item = response.body();
                    fadapter = new GithubAdapter(item, getContext());
                    recyclerView.setAdapter(fadapter);

                    if (item.isEmpty()) {
                        notFound.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(getContext(), R.string.not_found_list, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
