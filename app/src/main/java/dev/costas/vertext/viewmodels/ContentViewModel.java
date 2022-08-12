package dev.costas.vertext.viewmodels;

import androidx.lifecycle.ViewModel;

public class ContentViewModel extends ViewModel {
	private String title = "";
	private String content =  "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
