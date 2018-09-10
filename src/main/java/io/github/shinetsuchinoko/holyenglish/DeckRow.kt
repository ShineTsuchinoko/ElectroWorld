package io.github.shinetsuchinoko.holyenglish

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.item_deck.view.*

data class Deck(var name: String, var abstruct: String){
    constructor(): this("", "")  // ViewHolderで使用している
}

class DeckRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(snapshot: DocumentSnapshot,
             listener: DeckListAdapter.OnDeckSelectedListener) {

        val deck = snapshot.toObject(Deck::class.java)
        val resources = itemView.resources

        // Load image
//        Glide.with(imageView.getContext())
//                .load(restaurant.getPhoto())
//                .into(imageView)

        itemView.deckItemName.text = deck.name
        itemView.deckItemAbstruct.text = deck.abstruct

        // Click listener
        itemView.setOnClickListener {
            listener.onDeckSelected(snapshot)
        }
    }
}

open class DeckListAdapter(val mQuery: Query, val mListener: OnDeckSelectedListener)
    : FirestoreAdapter<DeckRowHolder>(mQuery){

    interface OnDeckSelectedListener {
        fun onDeckSelected(deckDocumentSnapshot: DocumentSnapshot)
    }

    companion object {
        val DECK_NAME = "deckName"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckRowHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_deck, parent, false)
        val holder = DeckRowHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: DeckRowHolder, position: Int) {
        holder.bind(getSnapshot(position), mListener)
    }
}