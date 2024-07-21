package com.cs346.musclememo.api.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ImageUploadService {
     fun uploadImage(uri: Uri): String {
        val profilePicture = uri
         var downloadUrl = ""
        profilePicture.let {
            val uuid = UUID.randomUUID();
            val uniqueImgName = "${uuid}.jpg"
            val storage = FirebaseStorage.getInstance()
            val ref = storage.reference.child(uri.toString()).child(uniqueImgName)
            ref.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        downloadUrl = uri.toString()
                        // Handle the download URL (e.g., save it to your database or use it in your app)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure
                    exception.printStackTrace()
                }
        }
        return downloadUrl
    }
}