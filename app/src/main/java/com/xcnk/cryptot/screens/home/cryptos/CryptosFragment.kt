package com.xcnk.cryptot.screens.home.cryptos

import android.os.Bundle
import android.view.*
import android.widget.GridView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xcnk.cryptot.R
import com.xcnk.cryptot.api.Session


class CryptosFragment : Fragment() {

    private lateinit var grid: GridView

    override fun onResume() {
        super.onResume()
        Session.selectedAsset = null
        syncItems()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cryptos, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        grid = view.findViewById(R.id.fragment_crypros_grid)
    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home_cryptos_action_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_crypto) {
            findNavController().navigate(R.id.action_cryptosFragment_to_creatorActivity)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun syncItems() {
        val adapter = CryptosAdapter(requireContext(), Session.getLocalAssets() ?: ArrayList())
        grid.adapter = adapter
        grid.setOnItemClickListener { parent, view, position, id ->
            Session.selectedAsset = Session.getLocalAssets()?.get(position)
            findNavController().navigate(R.id.action_cryptosFragment_to_cryptoDetailsActivity)
        }
    }

}