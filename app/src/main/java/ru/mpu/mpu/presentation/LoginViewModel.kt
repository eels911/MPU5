package ru.mpu.lk.login.presentation

import androidx.lifecycle.ViewModel

class LoginViewModel:ViewModel() {

    fun loginViaUrl(
        encodedUrl: String?,
        login: String?,
        listener: ExecutorManager.ResultListener<String?>
    ) {
        val campusOTP = CampusOTP()
        try {
            campusOTP.fromEncodedURL(encodedUrl, App.URL_PREFIX)
        } catch (ex: Ex) {
            listener.onError(ex)
            return
        }
        val task: ExecutorTask = ExecutorTask<String>()
            .onBackground {
                app.createGroup(campusOTP.serviceURL, getDeviceId(), campusOTP.userOTP, login)
                ""
            }
            .onSuccess { result -> listener.onSuccess("") }
            .onError(listener::onError)
        executorManager.execute(task)
    }
}