package io.github.shinetsuchinoko.holyenglish


import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

/**
 * A simple [Fragment] subclass.
 *
 */
class MainDeckListFragment : Fragment(), DeckListAdapter.OnDeckSelectedListener{

    private lateinit var parentActivity: FragmentActivity
    private lateinit var mDB : FirebaseFirestore
    private lateinit var mQuery: Query
    private lateinit var mAdapter: DeckListAdapter
    private val cards = "dards"
    private val TAG = "MainDeckListFragment"
    private lateinit var mViewModel: ViewModel
    private val LIMIT = 50

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        parentActivity = activity as FragmentActivity

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true)

        mDB = FirebaseFirestore.getInstance()
        // Get ${LIMIT} restaurants
        mQuery = mDB.collection("decks")
                //.orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT.toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
//        val position = arguments!!.getInt("position")  // TODO: nullになる
//
//        mViewModel = ViewModelProviders.of(parentActivity)
//                .get(position.toString(), MainFragmentViewModel::class.java)

        // RecyclerView作成 // TODO: kotlin extension を後で使い方調べる
        val decksView = view.findViewById<RecyclerView>(R.id.decksRView)
        decksView.layoutManager = LinearLayoutManager(parentActivity)
        mAdapter = object: DeckListAdapter(mQuery = mQuery, mListener = this@MainDeckListFragment) {
            override fun onDataChanged(){

            }

            override fun onError(e: FirebaseFirestoreException) =// Show a snackbar on errors
                    Snackbar.make(view.findViewById<View>(android.R.id.content),
                            "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
        }
        decksView.adapter = mAdapter

        return view
    }

    override fun onStart() {
        super.onStart()
        // Start listening for Firestore updates
        mAdapter.startListening()
    }

    override fun onDeckSelected(restaurant: DocumentSnapshot) {
        // Go to the details page for the selected restaurant
        val intent = Intent(parentActivity, TestPageActivity::class.java)
        // intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.id)

        startActivity(intent)
        parentActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }
}


