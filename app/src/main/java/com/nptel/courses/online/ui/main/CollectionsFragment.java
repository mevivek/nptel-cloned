package com.nptel.courses.online.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nptel.courses.online.R;
import com.nptel.courses.online.adapters.CollectionListAdapter;
import com.nptel.courses.online.databinding.CollectionsFragmentBinding;
import com.nptel.courses.online.databinding.EditCollectionBinding;
import com.nptel.courses.online.entities.Collection;
import com.nptel.courses.online.interfaces.ListItemClickListener;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.ui.playlist.PlaylistActivity;
import com.nptel.courses.online.ui.playlist.PlaylistFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsFragment extends Fragment {

    private CollectionsFragmentBinding binding;
    private CollectionListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CollectionsFragmentBinding.inflate(inflater);
        adapter = new CollectionListAdapter(new ListItemClickListener<Collection>() {
            @Override
            public void onClick(Collection collection) {
                startActivity(new Intent(requireContext(), PlaylistActivity.class)
                        .putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.COLLECTION.toString())
                        .putExtra(PlaylistActivity.ID, collection.getName())
                        .putExtra(PlaylistActivity.COURSE_NAME, collection.getName())
                );
            }

            @Override
            public void onOptionClicked(Collection collection, int optionId) {
                if (optionId == R.id.edit) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                    EditCollectionBinding binding = EditCollectionBinding.inflate(bottomSheetDialog.getLayoutInflater());
                    bottomSheetDialog.setContentView(binding.getRoot());
                    bottomSheetDialog.show();

                    binding.collectionName.setText(collection.getName());
                    binding.save.setOnClickListener(view -> {
                        String newCollectionName = binding.newCollectionName.getText().toString();
                        if (newCollectionName.isEmpty()) {
                            binding.newCollectionName.setError("Name can not be empty");
                            return;
                        }
                        RetrofitFactory.getRetrofitService(requireContext()).updateCollection(collection.getName(), newCollectionName).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Collection name updated", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                    fetch();
                                } else
                                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                    binding.delete.setOnClickListener(view -> {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
                        dialogBuilder.setTitle("Are you sure about deleting this collection?");
                        dialogBuilder.setPositiveButton("Yes", (dialog, which) -> RetrofitFactory.getRetrofitService(requireContext()).deleteCollection(collection.getName()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Collection deleted.", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                    fetch();
                                } else
                                    Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                                Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        })).setNegativeButton("Candel", (dialog, which) -> {
                        }).show();
                    });
                }
            }
        });
        binding.recyclerView.setAdapter(adapter);
        fetch();
        return binding.getRoot();
    }

    private void fetch() {

        RetrofitFactory.getRetrofitService(requireContext()).getCollections().enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(@NonNull Call<List<Collection>> call, @NonNull Response<List<Collection>> response) {
                if (response.isSuccessful()) {
                    adapter.submitList(response.body());
                    binding.noCollection.setVisibility(response.body().size() == 0 ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Collection>> call, @NonNull Throwable throwable) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
