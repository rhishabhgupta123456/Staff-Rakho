package com.staffrakho.utility

import java.util.regex.Pattern

object ValidationData {

    fun isEmail(input: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return input.matches(emailPattern)
    }

    fun isPhoneNumber(input: String): Boolean {
        val phoneNumberPattern = Regex("^[+]?[0-9]{10,13}\$")
        return input.matches(phoneNumberPattern)
    }

    fun passCheck(pass : String) : Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$"
        val patternPass = Pattern.compile(passwordRegex)
        val password = patternPass.matcher(pass)
        return  password.matches()
    }



}