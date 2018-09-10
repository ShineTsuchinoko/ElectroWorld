package io.github.shinetsuchinoko.holyenglish

import android.app.Activity
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
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

import kotlinx.android.synthetic.main.activity_test_page.*
import kotlinx.android.synthetic.main.fragment_test_page.view.*

val WORD_NAME: String = "WordName"
val IMAGE_PATH : String = "ImagePath"
val FLAVOR_TEXT: String = "FlavorText"

class TestPageActivity : AppCompatActivity() {
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var mDownloadIterator: MutableIterator<String>
    private val mDownloadPaths: MutableList<String> = mutableListOf<String>(
            "images/000001.png",
            "images/000002.png",
            "images/000003.png",
            "images/000004.png",
            "images/000005.png",
            "images/000006.png",
            "images/000007.png",
            "images/000008.png",
            "images/000009.png",
            "images/000010.png",
            "images/000011.png",
            "images/000012.png",
            "images/000013.png",
            "images/000014.png",
            "images/000015.png",
            "images/000016.png",
            "images/000017.png",
            "images/000018.png",
            "images/000019.png",
            "images/000020.png",
            "images/000021.png",
            "images/000022.png",
            "images/000023.png",
            "images/000024.png",
            "images/000025.png",
            "images/000026.png",
            "images/000027.png",
            "images/000028.png",
            "images/000029.png",
            "images/000030.png"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_page)

        //setSupportActionBar(toolbar)

        mDownloadIterator = mDownloadPaths.iterator()

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, mDownloadPaths.size)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
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

            val placeholderFragment = TestPagePlaceholderFragment.newInstance(position + 1)
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
}
