package io.github.shinetsuchinoko.holyenglish

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import io.github.shinetsuchinoko.holyenglish.MainCardItemFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.item_card.view.*
import java.net.URL

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
data class CardItem(val name: String, val image: URL, val num: Int )

class MainCardItemRecyclerViewAdapter(
        private val mValues: List<CardItem>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MainCardItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as CardItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNameView.text = item.name
        holder.mNumView.text = item.num.toString()
        // TODO: Glideで画像セット

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.cardNameTextView
        val mNumView: TextView = mView.cardNumTextView
        val mImageView: ImageView = mView.cardImageView

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
