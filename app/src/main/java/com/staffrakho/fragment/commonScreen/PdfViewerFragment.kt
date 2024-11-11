package com.staffrakho.fragment.commonScreen

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
//import com.github.barteksc.pdfviewer.PDFView
import com.staffrakho.R
import com.staffrakho.databinding.FragmentPdfViewerBinding
import com.staffrakho.utility.AppConstant
import com.staffrakho.utility.BaseFragment
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class PdfViewerFragment : BaseFragment() {

    lateinit var binding: FragmentPdfViewerBinding
    private lateinit var currentUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentPdfViewerBinding.inflate(
                LayoutInflater.from(requireActivity()),
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.USER_PROFILE_SCREEN) {
                        val bundle = Bundle()
                        bundle.putInt(AppConstant.SCREEN, 2)
                        findNavController().navigate(R.id.openProfileScreen, bundle)
                    }else if(requireArguments().getString(AppConstant.SOURCE_FRAGMENT) == AppConstant.USER_PROFILE_VIEW_SCREEN){
                        val bundle = Bundle()
                        bundle.putString(AppConstant.USER_DETAIL, requireArguments().getString(AppConstant.USER_DETAIL))
                        findNavController().navigate(R.id.openUserProfileViewScreen, bundle)
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        currentUrl = requireArguments().getString(AppConstant.PDF_URL)!!
        Log.e("PDF URL", currentUrl)

        if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
        } else {
            //RetrievePDFFromURL(binding.idPDFView).execute(currentUrl)
        }

    }

//    class RetrievePDFFromURL(pdfView: PDFView) :
//        AsyncTask<String, Void, InputStream>() {
//        @SuppressLint("StaticFieldLeak")
//        val myPdfView: PDFView = pdfView
//
//        @Deprecated("Deprecated in Java")
//        override fun doInBackground(vararg params: String?): InputStream? {
//            var inputStream: InputStream? = null
//            try {
//                val url = URL(params[0])
//
//                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
//
//                if (urlConnection.responseCode == 200) {
//                    inputStream = BufferedInputStream(urlConnection.inputStream)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                return null
//            }
//            return inputStream
//        }
//
//        @Deprecated("Deprecated in Java", ReplaceWith("mypdfView.fromStream(result).load()"))
//        override fun onPostExecute(result: InputStream?) {
//            myPdfView.fromStream(result).load()
//
//        }
//    }
}