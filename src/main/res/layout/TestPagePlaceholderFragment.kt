import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.shinetsuchinoko.holyenglish.FLAVOR_TEXT
import io.github.shinetsuchinoko.holyenglish.IMAGE_PATH
import io.github.shinetsuchinoko.holyenglish.R
import io.github.shinetsuchinoko.holyenglish.WORD_NAME
import kotlinx.android.synthetic.main.fragment_test_page.view.*

class TestPagePlaceholderFragment : Fragment()  {
/**
 * A placeholder fragment containing a simple view.
 * Firestoreから画像を取得する
 */
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


        rootView.flavorText.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
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
        fun newInstance(sectionNumber: Int): TestPagePlaceholderFragment {
            val fragment = TestPagePlaceholderFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}