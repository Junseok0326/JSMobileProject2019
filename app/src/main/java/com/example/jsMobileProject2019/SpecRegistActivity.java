package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SpecRegistActivity extends AppCompatActivity {
    MaterialEditText editGrade,editToeic,editName,editSchool,editLisence,editOversea,editAward,editIntern;    //9
    Spinner editToeicSpeaking, editOPIc;    //2
    String name,email,college, opic, toeicSpeaking,sex; //6
    long award, license,intern, overseas,toeic;   //5
    double grade;   //1
    FirebaseFirestore db;
    Intent intent;
    UserData user;
    RadioGroup rg;
    RadioButton rb_man, rb_woman;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_regist_page);
        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        email = intent.getExtras().getString("email");
        rg=findViewById(R.id.radioGroup);
        rb_man = findViewById(R.id.radioMan);
        rb_woman = findViewById(R.id.radioWoman);
        editGrade = findViewById(R.id.editGrade);
        editToeic = findViewById(R.id.editToeic);
        editToeicSpeaking = findViewById(R.id.editToeicS);
        editSchool = findViewById(R.id.editSchool);
        editName = findViewById(R.id.editName);
        editAward = findViewById(R.id.editAwards);
        editIntern = findViewById(R.id.editIntern);
        editLisence = findViewById(R.id.editLincense);
        editOPIc = findViewById(R.id.editOpic);
        editOversea = findViewById(R.id.editOversea);
    }

    //입력 체크 메서드드
   public void clickRegist(View v){
        //프로필 입력란에 입력되지 않는 항목이 있다면 에러 메시지를 띄워준다.
        if (isStrNull(editName)){
            editName.setError("이름을 입력하세요!");
        }
        else if (isStrNull(editSchool)){
            editSchool.setError("학교를 입력하세요!");
        }
        else if(Float.parseFloat(editGrade.getText().toString())>4.5){
            editGrade.setError("학점은 4.5이하 까지만 입력 가능합니다.");
        }
        else if (isStrNull(editGrade)){
            editGrade.setError("학점을 입력하세요!");
        }
        else if (Integer.parseInt(editToeic.getText().toString())>990){
            editToeic.setError("토익점수는 최대 990점까지 입력 가능합니다.");
        }
        else if (isStrNull(editToeic)){
            editToeic.setError("토익점수를 입력하세요!");
        }
        else if (isStrNull(editAward)){
            editAward.setError("수상경험을 입력하세요!");
        }
        else if (isStrNull(editIntern)){
            editIntern.setError("인턴경력을 입력하세요!");
        }
        else if (isStrNull(editOversea)){
            editOversea.setError("해외경험을 입력하세요!");
        }
        else if (isStrNull(editLisence)){
            editLisence.setError("자격증 개수를 입력하세요!");
        }
        else {
            registUser();
        }
    }

    //프로필 등록 메서드
    private void registUser(){
        //입력란에 입력된 값들을 userData 컬렉션 각각의 이메일 도큐먼트로 저장한다.
        DocumentReference doc = db.collection("userData").document(email);
        grade = Float.parseFloat(editGrade.getText().toString());
        toeic = Integer.parseInt(editToeic.getText().toString());
        award = Integer.parseInt(editAward.getText().toString());
        license = Integer.parseInt(editLisence.getText().toString());
        intern = Integer.parseInt(editIntern.getText().toString());
        overseas = Integer.parseInt(editOversea.getText().toString());
        name = editName.getText().toString();
        college = editSchool.getText().toString();
        opic = editOPIc.getSelectedItem().toString();
        toeicSpeaking = editToeicSpeaking.getSelectedItem().toString();
        // 체크박스의 선택 유무로 성별을 지정한다.
        if (rb_man.isChecked()){
            sex = rb_man.getText().toString();
        }else if(rb_woman.isChecked()){
            sex = rb_woman.getText().toString();
        }

        user = new UserData(email, name, college, opic, toeicSpeaking, grade, toeic, award, license, intern, overseas, sex);

        //등록의 성공 유무를 Toast 메시지로 보여준다.
        doc.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"스펙 등록 성공",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"스펙 등록 실패",Toast.LENGTH_LONG).show();
            }
        });
        intent = new Intent(SpecRegistActivity.this, WelcomActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }

    //값 입력 판별 메서드
    private boolean isStrNull(MaterialEditText m){
        return m.getText().toString().equals("");
    }

}
