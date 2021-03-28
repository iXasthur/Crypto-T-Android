package com.xcnk.cryptot.screens.home.cryptos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.xcnk.cryptot.R
import com.xcnk.cryptot.models.crypto.CryptoAsset

class CryptosAdapter(
    private val context: Context,
    private val assets: ArrayList<CryptoAsset>
): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return assets.count()
    }

    override fun getItem(position: Int): Any {
        return assets[position]
    }

    override fun getItemId(position: Int): Long {
        return assets[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.element_crypto_cell, parent, false)
        } else {
            view = convertView
        }

        val asset: CryptoAsset = assets[position]

        updateCellView(view, asset)

        return view
    }

    private fun updateCellView(view: View, p: CryptoAsset) {
        val icon: ImageView = view.findViewById(R.id.element_crypro_cell_icon)
        val name: TextView = view.findViewById(R.id.element_crypro_cell_text_name)
        val code: TextView = view.findViewById(R.id.element_crypro_cell_text_code)

        name.text = p.name
        code.text = p.code
    }
}