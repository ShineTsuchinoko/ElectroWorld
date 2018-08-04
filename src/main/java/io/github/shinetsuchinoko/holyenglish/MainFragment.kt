package io.github.shinetsuchinoko.holyenglish


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    private lateinit var parentActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        parentActivity = activity as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val startButton = view.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            val intent = Intent(parentActivity, TestPageActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}
