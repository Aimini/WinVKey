package com.win_vkey.startai.winvkey

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.win_vkey.startai.winvkey.data_class.Settings
import com.win_vkey.startai.winvkey.database.database
import kotlinx.android.synthetic.main.fragment_settings.*
import java.lang.Integer.parseInt
import java.net.URISyntaxException
import java.net.URL


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        input_host.text = SpannableStringBuilder(settings.host)
        input_port.text = SpannableStringBuilder(settings.port.toString())
        super.onActivityCreated(savedInstanceState)
    }

    var settings:Settings = Settings()
}// Required empty public constructor
