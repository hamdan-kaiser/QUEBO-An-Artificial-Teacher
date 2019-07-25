package hamdan.juniordesign.quebo.pdf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hamdan.juniordesign.quebo.R;
import hamdan.juniordesign.quebo.utils.AppUtils;

public class PdfActivity extends AppCompatActivity {

    public static List<Bitmap> images = new ArrayList<>();
    ListView pdfListView;
    File directory;
    ArrayList<File> fileList = new ArrayList<>();
    PdfAdapter pdfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfListView = findViewById(R.id.pdfList);
        directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        getFile(directory);
        pdfAdapter = new PdfAdapter(getApplicationContext(), fileList);
        pdfListView.setAdapter(pdfAdapter);

        pdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File pdfFile = new File(pdfAdapter.all_pdf.get(position).getAbsolutePath());
                try {
                    InputStream inputStream = new FileInputStream(pdfFile);
                    images = AppUtils.renderToBitmap(PdfActivity.this, pdfFile);

                    startActivity(new Intent(PdfActivity.this, SelectPdfImage.class));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getFile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File aListFile : listFile) {
                if (aListFile.isDirectory()) {
                    getFile(aListFile);

                } else {

                    boolean booleanPdf = false;
                    if (aListFile.getName().endsWith(".pdf")) {

                        for (int j = 0; j < fileList.size(); j++) {
                            if (fileList.get(j).getName().equals(aListFile.getName())) {
                                booleanPdf = true;
                            } else {
                                // No PDF found
                            }
                        }

                        if (booleanPdf) {
                            booleanPdf = false;
                        } else {
                            fileList.add(aListFile);
                        }
                    }
                }
            }
        }
    }
}
