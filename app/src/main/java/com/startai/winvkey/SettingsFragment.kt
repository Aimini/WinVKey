package com.win_vkey.startai.winvkey

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.win_vkey.startai.winvkey.data_class.Settings
import kotlinx.android.synthetic.main.fragment_keys.*
import kotlinx.android.synthetic.main.fragment_settings.*


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        loadSettings()
        super.onActivityCreated(savedInstanceState)
    }

    private var settings: Settings = Settings()

    fun getSettings():Settings{
            if (input_host != null && input_port !=null) {
                settings.host = input_host.text.toString()
                settings.port = input_port.text.toString().toInt()
            }
            return settings
        }


    fun   setSettings(s:Settings){
        settings = s
    if (input_host != null && input_port !=null)
        loadSettings()
    }
    private fun loadSettings() {
        input_host.text = SpannableStringBuilder(settings.host)
        input_port.text = SpannableStringBuilder(settings.port.toString())
    }
}// Required empty public constructor
