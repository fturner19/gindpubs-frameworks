package com.giniem.gindpubs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.giniem.gindpubs.R;
import com.giniem.gindpubs.workers.BitmapCache;
import com.giniem.gindpubs.workers.DownloaderTask;

public class MagazineThumb extends LinearLayout {

	private String name;

	private String title;

	private String info;

	private String date;

	private Integer size;

	private String cover;

	private String url;
	
	private Integer sizeMB;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSizeMB() {
		return sizeMB;
	}

	public void setSizeMB(Integer sizeMB) {
		this.sizeMB = sizeMB;
	}

	public MagazineThumb(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.MagazineThumb, 0, 0);

		try {
			this.name = typedArray.getString(R.styleable.MagazineThumb_name);
			this.title = typedArray.getString(R.styleable.MagazineThumb_title);
			this.info = typedArray.getString(R.styleable.MagazineThumb_info);
			this.date = typedArray.getString(R.styleable.MagazineThumb_date);
			this.size = typedArray
					.getInteger(R.styleable.MagazineThumb_size, 0);
			this.cover = typedArray.getString(R.styleable.MagazineThumb_cover);
			this.url = typedArray.getString(R.styleable.MagazineThumb_url);
		} finally {
			typedArray.recycle();
		}

		init(context, attrs);
	}

	public MagazineThumb(Context context) {
		super(context);
	}

	public void init(Context context, AttributeSet attrs) {
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.magazine_thumb_options, this, true);

		this.sizeMB = (this.size / 1048576);
		
		ImageView imageView = (ImageView) getChildAt(0);
		BitmapCache dit = new BitmapCache(context, imageView);
		// We pass the cover URL and the name to save the cached image with that
		// name.
		dit.execute(this.cover, this.name);

		TextView tvTitle = (TextView) ((LinearLayout) getChildAt(1))
				.getChildAt(0);
		tvTitle.setEllipsize(null);
		tvTitle.setSingleLine(false);
		tvTitle.setText(this.title);

		TextView tvInfo = (TextView) ((LinearLayout) getChildAt(1))
				.getChildAt(1);
		tvInfo.setEllipsize(null);
		tvInfo.setSingleLine(false);
		tvInfo.setText(this.info);

		TextView tvDate = (TextView) ((LinearLayout) getChildAt(1))
				.getChildAt(2);
		tvDate.setEllipsize(null);
		tvDate.setSingleLine(false);
		tvDate.setText(this.date);

		TextView tvSize = (TextView) ((LinearLayout) getChildAt(1))
				.getChildAt(3);
		tvSize.setEllipsize(null);
		tvSize.setSingleLine(false);
		tvSize.setText("" + this.sizeMB + " MB");
		
		LinearLayout subLayout = (LinearLayout) ((LinearLayout) getChildAt(1))
				.getChildAt(5);
		TextView tvProgress = (TextView) subLayout.getChildAt(0);
		tvProgress.setText("0 MB / " + this.sizeMB + " MB");

		Button buttonDownload = (Button) ((LinearLayout) getChildAt(1))
				.getChildAt(4);
		buttonDownload.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Button button = (Button) v;
				button.setVisibility(View.GONE);
				LinearLayout parent = (LinearLayout) ((LinearLayout) getChildAt(1))
						.getChildAt(5);
				parent.setVisibility(View.VISIBLE);
				
				DownloaderTask downloader = new DownloaderTask(MagazineThumb.this);
				downloader.execute(url, name);
			}
		});
	}
	
	public void updateProgress(long progress, long fileProgress, long length) {
		LinearLayout parent = (LinearLayout) ((LinearLayout) getChildAt(1))
				.getChildAt(5);
		
		fileProgress = fileProgress / 1048576;
		length = length / 1048576;
		
		TextView tvProgress = (TextView) parent.getChildAt(0);
		tvProgress.setText(String.valueOf(fileProgress) + " MB / " + length + " MB");
		
		ProgressBar progressBar = (ProgressBar) parent.getChildAt(1);
		
		Integer intProgress = (int) (long) progress;
		
		progressBar.setProgress(intProgress);
	}
	
	public void showActions() {
		LinearLayout downloadingUI = (LinearLayout) ((LinearLayout) getChildAt(1))
				.getChildAt(5);
		downloadingUI.setVisibility(View.GONE);
		
		LinearLayout actionsUI = (LinearLayout) ((LinearLayout) getChildAt(1))
				.getChildAt(6);
		actionsUI.setVisibility(View.VISIBLE);
	}

}
