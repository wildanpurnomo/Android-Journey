package com.example.dzakiyh.simpel.latihan_soal;

import android.util.Log;

public class Questions {

    public String mQuestions[] = {
            "Apa arti marka putus-putus pada jalanan?",
            "Apakah arti warna kuning pada lampu lalu lintas?",
            "Melalui lajur manakan Anda seharusnya mendahului?",

    };

    private String mChoice[][] = {
            {"Tempat Penyebrangan","Diperbolehkan mendahului", "Tidak diperbolehkan mendahului", "Pembatas arus jalan"},
            {"Berhenti","Berjalan perlahan", "Bersiap untuk jalan", "Mempercepat laju"},
            {"Kanan","Kiri", "Tergantung kecepatan mobil di depan", "Tergantung keramaian jalan"},
    };

    private String mCOrrectAnswers[] = {"Diperbolehkan mendahului", "Bersiap untuk jalan","Kanan"};

    public String getQuestion (int a) {
      String question = mQuestions[a];
      return question;
    };

    public String getChoice1 (int a) {
        String choice = mChoice[a][0];
        return choice;
    }

    public String getChoice2 (int a) {
        Log.d("getChoice2 ", mChoice[a][1]);
        String choice = mChoice[a][1];
        return choice;
    }

    public String getChoice3 (int a) {
        String choice = mChoice[a][2];
        return choice;
    }

    public String getChoice4 (int a) {
        String choice = mChoice[a][3];
        return choice;
    }

    public String getCorrectAnswer(int a) {
        String answer = mCOrrectAnswers[a];
        return answer;
    }

}
