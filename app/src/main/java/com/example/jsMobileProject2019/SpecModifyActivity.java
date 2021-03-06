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

public class SpecModifyActivity extends AppCompatActivity {
    MaterialEditText editGrade,editToeic, editLisence,editOversea,editAward,editIntern, editSchool;    //9
    Spinner editToeicSpeaking, editOPIc;    //2
    String email, opic, toeicSpeaking, college; //6
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
        setContentView(R.layout.spec_modify_page);
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        db = FirebaseFirestore.getInstance();
        email = user.getEmail();
        rg=findViewById(R.id.radioGroup);
        rb_man = findViewById(R.id.radioMan);
        rb_woman = findViewById(R.id.radioWoman);

        editSchool = findViewById(R.id.editSchool);
        editGrade = findViewById(R.id.editGrade);
        editToeic = findViewById(R.id.editToeic);
        editToeicSpeaking = findViewById(R.id.editToeicS);
        editAward = findViewById(R.id.editAwards);
        editIntern = findViewById(R.id.editIntern);
        editLisence = findViewById(R.id.editLincense);
        editOPIc = findViewById(R.id.editOpic);
        editOversea = findViewById(R.id.editOversea);

        editSchool.setText(user.getCollege());
        editGrade.setText((Math.round(user.getGrade()*100)/100.0)+"");
        editToeic.setText(user.getToeic()+"");
        editAward.setText(user.getAward()+"");
        editIntern.setText(user.getIntern()+"");
        editLisence.setText(user.getLicense()+"");
        editOversea.setText(user.getOverseas()+"");
    }

    public void clickModify(View v){
        if (isStrNull(editSchool)){
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
            modifyProfile();
        }
    }

    private void modifyProfile(){
        DocumentReference doc = db.collection("userData").document(email);
        college = editSchool.getText().toString();
        grade = Float.parseFloat(editGrade.getText().toString());
        toeic = Integer.parseInt(editToeic.getText().toString());
        award = Integer.parseInt(editAward.getText().toString());
        license = Integer.parseInt(editLisence.getText().toString());
        intern = Integer.parseInt(editIntern.getText().toString());
        overseas = Integer.parseInt(editOversea.getText().toString());
        opic = editOPIc.getSelectedItem().toString();
        toeicSpeaking = editToeicSpeaking.getSelectedItem().toString();


        //String email, String name, String college, String major, String opic, String toeicSpeaking, double grade, long toeic, long award, long license, long intern, long overseas
        user = new UserData(user.getEmail(), user.getName(), college, opic, toeicSpeaking, grade, toeic, award, license, intern, overseas,user.getSex());

        doc.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"스펙 수정 성공",Toast.LENGTH_LONG).show();
                intent = new Intent(SpecModifyActivity.this, WelcomActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"스펙 수정 실패",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isStrNull(MaterialEditText m){
        return m.getText().toString().equals("");
    }
}
