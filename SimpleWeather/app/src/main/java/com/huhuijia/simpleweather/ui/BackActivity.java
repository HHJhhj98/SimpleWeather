package com.huhuijia.simpleweather.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huhuijia.simpleweather.R;

public class BackActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtPhone;
    private EditText mEtContent;
    private Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        initView();

    }

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mBtnSend = (Button) findViewById(R.id.btn_send);

        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String phone=mEtPhone.getText().toString().trim();
                String context=mEtContent.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(context)){
                    Toast.makeText(this,"反馈成功！",Toast.LENGTH_LONG).show();
                    mEtPhone.setText("");
                    mEtContent.setText("");
                    //反馈结果
                }else {
                    Toast.makeText(this,"请输入反馈信息！",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
