package hamdan.juniordesign.quebo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Objects;

import hamdan.juniordesign.quebo.summary.SummaryTool;

public class ShowSummaryActivity extends AppCompatActivity {

    String topic, calculation = "";
    TextView calculationText, summaryText;
    StringBuilder summaryFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_summary);

        calculationText = findViewById(R.id.calculationText);
        summaryText = findViewById(R.id.summaryText);

        topic = Objects.requireNonNull(getIntent().getExtras()).getString("topic");

        SummaryTool summary = new SummaryTool(ShowSummaryActivity.this, topic);
        summary.init();

        calculation = summary.getStats();
        calculationText.setText(calculation);

        summaryFinal = summary.getSummary();
        summaryText.setText(summaryFinal);
    }
}
