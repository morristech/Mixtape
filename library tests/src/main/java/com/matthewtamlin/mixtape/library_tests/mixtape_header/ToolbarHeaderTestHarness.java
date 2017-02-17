/*
 * Copyright 2017 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.mixtape.library_tests.mixtape_header;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;
import com.matthewtamlin.mixtape.library.mixtape_header.ToolbarHeader;
import com.matthewtamlin.mixtape.library_tests.R;


/**
 * Test harness for testing the {@link ToolbarHeader} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class ToolbarHeaderTestHarness extends HeaderViewTestHarness {
	private final LruCache<LibraryItem, CharSequence> titleCache = new LruCache<>(1000);

	private final LruCache<LibraryItem, CharSequence> subtitleCache = new LruCache<>(1000);

	private final LruCache<LibraryItem, Drawable> artworkCache =
			new LruCache<LibraryItem, Drawable>(1000000) {
				@Override
				protected int sizeOf(final LibraryItem key, final Drawable value) {
					// All LibraryItems use BitmapDrawable for the artwork
					final Bitmap artworkBitmap = ((BitmapDrawable) value).getBitmap();
					return artworkBitmap.getByteCount();
				}
			};

	/**
	 * The defaults to use when binding data to the test view.
	 */
	private DisplayableDefaults defaults;

	/**
	 * The artwork fade duration to use when transitioning artwork in the test view, measured in
	 * milliseconds.
	 */
	private int fadeDurationMs = 0;

	/**
	 * The view under test.
	 */
	private ToolbarHeader testView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.raw.default_artwork);
		defaults = new ImmutableDisplayableDefaults("Default title", "Default artwork",
				new BitmapDrawable(getResources(), artwork));

		getControlsContainer().addView(createChangeTitleBinderButton());
		getControlsContainer().addView(createChangeSubtitleBinderButton());
		getControlsContainer().addView(createChangeArtworkBinderButton());
	}

	@Override
	public ToolbarHeader getTestView() {
		if (testView == null) {
			testView = new ToolbarHeader(this);
		}

		return testView;
	}

	/**
	 * Creates a button which changes the title binder of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeTitleBinderButton() {
		final Button b = new Button(this);
		b.setText("Change title binder");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setTitleDataBinder(new TitleBinder(cache, defaults));
			}
		});

		return b;
	}

	/**
	 * Creates a button which changes the subtitle binder of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeSubtitleBinderButton() {
		final Button b = new Button(this);
		b.setText("Change subtitle binder");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setSubtitleDataBinder(new SubtitleBinder(cache, defaults));
			}
		});

		return b;
	}

	/**
	 * Creates a button which changes the artwork binder of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeArtworkBinderButton() {
		final Button b = new Button(this);
		b.setText("Change artwork binder");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setArtworkDataBinder(new ArtworkBinder(cache, defaults));
			}
		});

		return b;
	}
}