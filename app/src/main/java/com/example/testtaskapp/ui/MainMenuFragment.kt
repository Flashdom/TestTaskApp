package com.example.testtaskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtaskapp.R


class MainMenuFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main_menu, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.mainMenuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = RecyclerViewAdapter(object : RecyclerViewAdapter.OnItemClicked {
            override fun onItemClicked(index: Int) {
                if (index == 0)
                    parentFragmentManager.beginTransaction().replace(
                        R.id.MainLayout,
                        PhotoFragment.newInstance(),
                        "photoFragment"
                    ).commit()
                if (index == 1)
                    parentFragmentManager.beginTransaction().replace(
                        R.id.MainLayout,
                        AudioFragment.newInstance(),
                        "audioFragment"
                    ).commit()
            }

        }
        )
        recyclerView.adapter = adapter



        return root

    }


    companion object {

        fun newInstance(): MainMenuFragment {
            return MainMenuFragment()
        }
    }


}
