//package com.cy.pj.sys.controller;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import okhttp3.*;
//
//import java.io.IOException;
//
//
///**
// * 展示了如何在 Android 应用中模拟表单提交数据
// * 我们创建了一个简单的表单提交功能，并使用 OkHttp 库来发送 POST 请求。
// * 你可以通过修改 simulateFormSubmit 方法中的参数来模拟不同的表单提交数据。
// */
//public class MainActivity extends AppCompatActivity {
//
//    private EditText editTextName;
//    private EditText editTextEmail;
//    private Button submitButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        editTextName = findViewById(R.id.editTextName);
//        editTextEmail = findViewById(R.id.editTextEmail);
//        submitButton = findViewById(R.id.submitButton);
//
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = editTextName.getText().toString();
//                String email = editTextEmail.getText().toString();
//
//                // 模拟表单提交
//                simulateFormSubmit(name, email);
//            }
//        });
//    }
//
//    private void simulateFormSubmit(String name, String email) {
//        OkHttpClient client = new OkHttpClient();
//
//        RequestBody formBody = new FormBody.Builder()
//                .add("name", name)
//                .add("email", email)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("https://example.com/submit")
//                .post(formBody)
//                .build();
//
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // Handle failure
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                // Handle success
//            }
//        });
//    }
//}
//
