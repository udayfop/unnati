package com.project.tmc.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.tmc.R;

public class ContactusFragment extends Fragment {

    private TextView phone_number,email_id,website_link,support_time;
    private ImageView call_btn, whatApp_btn,message_btn;
    private Intent intent;

    String mobilleNumber="Contact Number Not Available";
    String emailId="Email Not Available";
    String webAddres="Web Address Not Available";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contactus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phone_number = (TextView) view.findViewById(R.id.phone_number);
        email_id = (TextView) view.findViewById(R.id.email_id);
        website_link = (TextView) view.findViewById(R.id.website_link);
        support_time = (TextView) view.findViewById(R.id.support_time);
        call_btn = (ImageView) view.findViewById(R.id.call_btn);
        whatApp_btn = (ImageView) view.findViewById(R.id.what_btn);
        message_btn = (ImageView) view.findViewById(R.id.msg_btn);
        intent = new Intent(Intent.ACTION_DIAL);

        phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse("tel:"+phone_number.getText().toString()));
                startActivity(intent);
            }
        });
        email_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+"akshay.singh@miracleites.in"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "email_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "email_body");
                startActivity(intent);


            }
        });
        website_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.miracleites.in/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.setData(Uri.parse("tel:"+phone_number.getText().toString()));
                startActivity(intent);
            }
        });
        whatApp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact("+91"+ phone_number.getText().toString());
            }
        });
        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:+91"+phone_number.getText().toString());
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Hello Support Team, Please Call me,");
                startActivity(sms_intent);
            }
        });
    }

    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra(Intent.EXTRA_TEXT, "Hello Support Team,");
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}