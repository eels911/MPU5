package ru.mpu.lk.login.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import ru.mpu.lk.R
import ru.mpu.lk.databinding.FragmentLoginBinding
import java.util.Stack

class FragmentLogin :
    BaseFragment(R.layout.fragment_login) {
    private var editTextLogin: AppCompatEditText? = null
    private var editTextPassword: AppCompatEditText? = null
    private var buttonLogin: ProgressButton? = null
    private val urlsToLogin = Stack<String>()
    private var login: String? = null
    private lateinit var bindingSignIn: FragmentLoginBinding

    // -----------------------------------------------------------------------------------------------------------------
    // заменить на onCreateView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_login, container, false)
        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
      //  toolbar.setNavigationContentDescription { v -> navigator.back() }
        val otherLoginMethods = rootView.findViewById<View>(R.id.otherLoginMethods)

        val bundle: Bundle? = arguments
//        if (bundle != null) {
//            val showBackButton = bundle.getBoolean(SHOW_BACK_BUTTON, false)
//            toolbar.setNavigationButtonVisible(showBackButton)
//        }
        buttonLogin = rootView.findViewById<Button>(R.id.buttonLogin)
        //
        buttonLogin.setEnabled(false)
       // buttonLogin.setOnClickListener { v -> login() }
        editTextLogin = rootView.findViewById(R.id.editTextLogin)
       // editTextLogin.addTextChangedListener(Watcher())
        editTextPassword = rootView.findViewById(R.id.editTextPassword)
     //   editTextPassword.addTextChangedListener(Watcher())
        return rootView
    }

    // -----------------------------------------------------------------------------------------------------------------
    // вызывать вью модель вместо логие
    private fun login() {
        if (editTextLogin!!.text == null || editTextPassword!!.text == null) {
            return
        }
        login = editTextLogin!!.text.toString()
        val password = editTextPassword!!.text.toString()
        buttonLogin.showProgress(true)
        buttonLogin.setEnabled(false)
        // ui state
        activityMain.login(login, password, object : ResultListener<List<String?>?>() {
            fun onSuccess(urls: List<String>?) {
                urlsToLogin.clear()
                urlsToLogin.addAll(urls!!)
                if (urlsToLogin.isEmpty()) {
                    buttonLogin.showProgress(false)
                    buttonLogin.setEnabled(true)
                    CustomToast.show(activityMain, getString(R.string.noGroupsError))
                } else {
                    loginUrls(urlsToLogin.pop())
                }
            }

            fun onError(ex: Ex?) {
                buttonLogin.showProgress(false)
                buttonLogin.setEnabled(true)
            }
        })
    }

    // -----------------------------------------------------------------------------------------------------------------
    private fun loginUrls(url: String) {
        activityMain.loginViaUrl(url, login, object : ResultListener<String?>() {
            fun onSuccess(s: String?) {
                processNextUrl()
            }

            fun onError(ex: Ex?) {
                processNextUrl()
            }
        })
    }

    // -----------------------------------------------------------------------------------------------------------------
    private fun processNextUrl() {
        if (urlsToLogin.isEmpty()) {
            app.currentGroup = null
            activityMain.showFragmentChangeGroup(false)
        } else {
            loginUrls(urlsToLogin.pop())
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private fun checkFields() {
        if (editTextLogin!!.text == null || editTextPassword!!.text == null) {
            buttonLogin.setEnabled(false)
            return
        }
        if (editTextLogin!!.text.toString().isEmpty() || editTextPassword!!.text.toString()
                .isEmpty()
        ) {
            buttonLogin.setEnabled(false)
            return
        }
        buttonLogin.setEnabled(true)
    }

    // =================================================================================================================
    private inner class Watcher : TextWatcher {
        // -------------------------------------------------------------------------------------------------------------
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        // -------------------------------------------------------------------------------------------------------------
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            checkFields()
        }

        // -------------------------------------------------------------------------------------------------------------
        override fun afterTextChanged(s: Editable) {}
    }

    companion object {
        const val SHOW_BACK_BUTTON = "SHOW_BACK_BUTTON"
    }
}
