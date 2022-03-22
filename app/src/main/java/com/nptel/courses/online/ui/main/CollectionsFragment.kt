package com.nptel.courses.online.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nptel.courses.online.R
import com.nptel.courses.online.adapters.CollectionListAdapter
import com.nptel.courses.online.databinding.CollectionsFragmentBinding
import com.nptel.courses.online.databinding.EditCollectionBinding
import com.nptel.courses.online.entities.Collection
import com.nptel.courses.online.interfaces.ListItemClickListener
import com.nptel.courses.online.ui.playlist.PlaylistActivity
import com.nptel.courses.online.ui.playlist.PlaylistFragment

class CollectionsFragment : Fragment() {
    private lateinit var binding: CollectionsFragmentBinding
    private lateinit var adapter: CollectionListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CollectionsFragmentBinding.inflate(inflater)
        adapter = CollectionListAdapter(object : ListItemClickListener<Collection> {
            override fun onClick(collection: Collection) {
                startActivity(Intent(requireContext(), PlaylistActivity::class.java)
                        .putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.COLLECTION.toString())
                        .putExtra(PlaylistActivity.ID, collection.name)
                        .putExtra(PlaylistActivity.COURSE_NAME, collection.name)
                )
            }

            override fun onOptionClicked(collection: Collection, optionId: Int) {
                if (optionId == R.id.edit) {
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    val binding = EditCollectionBinding.inflate(bottomSheetDialog.layoutInflater)
                    bottomSheetDialog.setContentView(binding.root)
                    bottomSheetDialog.show()
                    binding.collectionName.setText(collection.name)
                    binding.save.setOnClickListener {
                        val newCollectionName = binding.newCollectionName.text.toString()
                        if (newCollectionName.isEmpty()) {
                            binding.newCollectionName.error = "Name can not be empty"
                            return@setOnClickListener
                        }
                        /*RetrofitFactory.getRetrofitService(requireContext()).updateCollection(collection.name, newCollectionName).enqueue(object : Callback<Void?> {
                            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(requireContext(), "Collection name updated", Toast.LENGTH_SHORT).show()
                                    bottomSheetDialog.dismiss()
                                    fetch()
                                } else Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<Void?>, throwable: Throwable) {
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        })*/
                    }
                    binding.delete.setOnClickListener {
                        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
                        dialogBuilder.setTitle("Are you sure about deleting this collection?")
                        dialogBuilder.setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                            /*RetrofitFactory.getRetrofitService(requireContext()).deleteCollection(collection.name)!!.enqueue(object : Callback<Void?> {
                                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(requireContext(), "Collection deleted.", Toast.LENGTH_SHORT).show()
                                        bottomSheetDialog.dismiss()
                                        fetch()
                                    } else Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<Void?>, throwable: Throwable) {
                                    Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show()
                                }
                            })*/
                        }.setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }.show()
                    }
                }
            }
        })
        binding.recyclerView.adapter = adapter
        fetch()
        return binding.root
    }

    private fun fetch() {
        /*RetrofitFactory.getRetrofitService(requireContext()).collections.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                if (response.isSuccessful) {
                    adapter.submitList(response.body())
                    binding.noCollection.visibility = if (response.body()!!.isEmpty()) View.VISIBLE else View.GONE
                }
            }

            override fun onFailure(call: Call<List<Collection>>, throwable: Throwable) {}
        })*/
    }

}