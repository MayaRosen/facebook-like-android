package com.example.foobar_android.activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.ReqRes.EditUserReq;
import com.example.foobar_android.ViewModel.UserViewModel;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etNickname, etPassword;
    private ImageView ivProfilePic;
    private Button btnSaveChanges;
    private UserViewModel userProfileViewModel;
    private String imgFull;
    private Button btnChooseImg;
    private Uri imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        etNickname = findViewById(R.id.nickname);
        etPassword = findViewById(R.id.password);
        ivProfilePic = findViewById(R.id.selected_image);
        btnSaveChanges = findViewById(R.id.btnSave);
        btnChooseImg = findViewById(R.id.btnChooseImg);

        userProfileViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        btnSaveChanges.setOnClickListener(view -> saveChanges());

        btnChooseImg.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent1, 100);
        });
    }

    private void saveChanges() {
        String nickname = etNickname.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (ivProfilePic.getDrawable() != null) {
            Bitmap imgBitmap = ((BitmapDrawable)ivProfilePic.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String img = Base64.encodeToString(byteArray, Base64.DEFAULT);
            imgFull = "data:image/jpeg;base64," + img;
        }

        if (nickname.isEmpty()){
            nickname = MyUser.getUser().getDisplayName();
        }
        else{
            MyUser.updateUserDisplayName(nickname);
        }
        if (password.isEmpty()){
            password = MyUser.getPassword();
        }
        else{
            MyUser.setPassword(password);
        }
        if (ivProfilePic.getDrawable() == null){
            imgFull = MyUser.getUser().getProfilePic();
        }
        else{
            MyUser.updateUserProfilePic(imgFull);
        }
        EditUserReq editUserReq = new EditUserReq(password, nickname, imgFull);

        userProfileViewModel.edit(editUserReq);

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        Intent newEditIntent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
        startActivity(newEditIntent);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivProfilePic.setImageURI(imageUri);
        }
    }
}
