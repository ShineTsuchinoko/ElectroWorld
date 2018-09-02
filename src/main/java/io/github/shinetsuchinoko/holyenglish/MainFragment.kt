package io.github.shinetsuchinoko.holyenglish


import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment(), DeckListAdapter.OnDeckSelectedListener{

    private lateinit var parentActivity: FragmentActivity
    private lateinit var mDB : FirebaseFirestore
    private lateinit var mQuery: Query
    private val cards = "dards"
    private val TAG = "MainFragment"
    private lateinit var mViewModel: ViewModel
    private val LIMIT = 50

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        parentActivity = activity as FragmentActivity
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
        decksView.adapter = DeckListAdapter(mQuery, this@MainFragment)


        // Get Decks
//        mDB.collection("decks")
//                .get()
//                .addOnCompleteListener{ task ->
//                    if(task.isSuccessful){
//                        for (document in task.result) {
//                            Log.d(TAG, document.id + " => " + document.data)
//                        }
//                    } else {
//                        Log.w(TAG, "Error getting decks.", task.exception)
//                    }
//                }


//        val startButton = view.findViewById<Button>(R.id.start_button)
//        startButton.setOnClickListener {
//            // Create a new user with a first and last name
//            val intent = Intent(parentActivity, TestPageActivity::class.java)
//            startActivity(intent)
//        }

        return view
    }

    override fun onDeckSelected(restaurant: DocumentSnapshot) {
        // Go to the details page for the selected restaurant
        val intent = Intent(parentActivity, TestPageActivity::class.java)
        // intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.id)

        startActivity(intent)
        parentActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }
}


