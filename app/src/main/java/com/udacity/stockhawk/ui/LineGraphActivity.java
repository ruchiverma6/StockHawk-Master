package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineGraphActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.activity_line_graph)
    LineChart mLineChart;
    ArrayList<Entry> entries;
    ArrayList<String> labels;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        uri=getIntent().getData();
        entries = new ArrayList<>();
        labels = new ArrayList<>();
        ButterKnife.bind(this);
        if(null!=uri) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    private void drawLineGraph() {
        LineDataSet dataset = new LineDataSet(entries, getString(R.string.stock_values_label));
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        com.github.mikephil.charting.data.LineData lineData = new LineData(labels, dataset);
        mLineChart.setDescription(getString(R.string.stock_values));
        mLineChart.setData(lineData);
        mLineChart.animateY(1000);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                uri,
                Contract.Quote.QUOTE_COLUMNS,
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (null != data && data.moveToFirst()) {
            String history = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            String[] dataArray = history.split("\\r?\\n");
            int val = 0;
            for (String str : dataArray) {
                String historyVal = str.substring(str.indexOf(", ") + 2);
                entries.add(new Entry(Float.parseFloat(historyVal), val++));
                labels.add(String.valueOf(val));
            }

        }
        drawLineGraph();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
