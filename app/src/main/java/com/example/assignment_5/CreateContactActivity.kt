package com.example.assignment_5

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import com.example.assignment_5.databinding.ActivityCreateContactBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateContactBinding
    private lateinit var selectedDate: Calendar
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageByteArray: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDueDate()
        setOnClickListners()
    }

    private fun setOnClickListners() {
        // Initialize selectedDate
        selectedDate = Calendar.getInstance()


        binding.imageViewEdit.setOnClickListener {
            showDatePickerDialog(binding.edtDueOn)
        }

        binding.btnSave.setOnClickListener {

            val edtCategry = binding.edtName.text.toString().trim()
            val title = binding.txtTitle.text.toString().trim()
            val dueDate = binding.edtDueOn.text.toString().trim()

            if (edtCategry.isNotEmpty() && dueDate.isNotEmpty() && title.isNotEmpty()) {
                val item = TaskItem(imageByteArray, edtCategry, title, dueDate)
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("item", item)
                }
                startActivity(intent)
                finish()
            }
        }


        binding.imageViewBriefCase.setOnClickListener {
            openGallery()
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val datePickerListener =
            DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(editText)
            }

        // Create DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this@CreateContactActivity,
            datePickerListener,
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        // Show DatePickerDialog
        datePickerDialog.show()
    }

    private fun updateDateInView(editText: EditText) {
        val myFormat = "dd/MM/yyyy" // specify the format
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        editText.setText(sdf.format(selectedDate.time))
    }

    private fun setDueDate() {
        // Get current date
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Add one day
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        binding.edtDueOn.hint = formattedDate
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            try {
                val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                imageByteArray = bitmapToByteArray(selectedImageBitmap) // Convert Bitmap to ByteArray
                val imageView = findViewById<ImageView>(R.id.imageViewBriefCase)
                imageView.setImageURI(selectedImageUri) // Set the selected image to the ImageView
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Function to convert Bitmap to ByteArray
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }}