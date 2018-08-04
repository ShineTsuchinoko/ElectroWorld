package io.github.shinetsuchinoko.holyenglish

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.shinetsuchinoko.holyenglish.Model.Storage.DownloadService
import io.github.shinetsuchinoko.holyenglish.Model.Storage.MyUploadService
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_test_page.*
import kotlinx.android.synthetic.main.fragment_test_page.view.*
import java.util.*

private val WORD_NAME: String = "WordName"
private val IMAGE_PATH : String = "ImagePath"
private val FLAVOR_TEXT: String = "FlavorText"

class TestPageActivity : AppCompatActivity() {
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private val KEY_FILE_URI = "key_file_uri"
    private val KEY_DOWNLOAD_URL = "key_download_url"
    private val TAG = "TestPageActivity"

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var mDownloadIterator: MutableIterator<String>
    private val mDownloadPaths: MutableList<String> = mutableListOf<String>(
            "images/000001.jpg",
            "images/000002.jpg",
            "images/000003.jpg",
            "images/000004.jpg",
            "images/000005.jpg",
            "images/000006.jpg",
            "images/000007.jpg",
            "images/000008.jpg",
            "images/000009.jpg",
            "images/000010.jpg",
            "images/000011.jpg",
            "images/000012.jpg",
            "images/000013.jpg",
            "images/000014.jpg",
            "images/000015.jpg",
            "images/000016.jpg",
            "images/000017.jpg",
            "images/000018.jpg",
            "images/000019.jpg",
            "images/000020.jpg",
            "images/000021.jpg",
            "images/000022.jpg",
            "images/000023.jpg",
            "images/000024.jpg",
            "images/000025.jpg",
            "images/000026.jpg",
            "images/000027.jpg",
            "images/000028.jpg",
            "images/000029.jpg",
            "images/000030.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_page)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, mDownloadPaths.size)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        mDownloadIterator = mDownloadPaths.iterator()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_test_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager, private val pageMax: Int) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            val placeholderFragment = PlaceholderFragment.newInstance(position + 1)
            val bundle = Bundle().apply {
                putString(WORD_NAME, "wordName")
                if(mDownloadIterator.hasNext())
                    putString(IMAGE_PATH, mDownloadIterator.next())
                else
                    putString(IMAGE_PATH, "error")
                putString(FLAVOR_TEXT, "flavor text")
            }
            placeholderFragment.arguments = bundle

            return placeholderFragment
        }

        override fun getCount(): Int {
            return pageMax
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "OBJECT " + (position + 1)
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        private lateinit var mParentActivity: Activity

        private var wordName: String = ""
        private var imagePath: String = "" // TODO: デフォルト値を後で設定
        private var flavorText: String = ""

        private lateinit var mStorageRef: StorageReference

        override fun onCreate(savedInstanceState: Bundle?){
            super.onCreate(savedInstanceState)
            mParentActivity = activity as Activity
            val bundle = arguments
            bundle?.let{
                wordName = bundle.getString(WORD_NAME)
                imagePath = bundle.getString(IMAGE_PATH)
                flavorText = bundle.getString(FLAVOR_TEXT)
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_test_page, container, false)
            mStorageRef = FirebaseStorage.getInstance().reference
            val imageStorageRef = mStorageRef.child(imagePath)
            val mainImageView = rootView.findViewById(R.id.mainImageView) as ImageView
            Glide.with(this /* context */)
                    .using(FirebaseImageLoader())
                    .load(imageStorageRef)
                    .into(mainImageView)


            rootView.flavor_text.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
