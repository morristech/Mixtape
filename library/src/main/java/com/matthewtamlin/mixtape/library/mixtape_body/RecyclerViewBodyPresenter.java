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

package com.matthewtamlin.mixtape.library.mixtape_body;

import android.view.MenuItem;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;

import java.util.List;

/**
 * A DirectBodyPresenter which can be used with a RecyclerViewBody. Although this class is not
 * abstract, the user interaction handling methods do nothing. To handle user interactions, override
 * {@link #onItemSelected(BodyContract.View, LibraryItem)} and {@link #onContextualMenuItemSelected
 * (BodyContract.View, LibraryItem, MenuItem)}.
 *
 * @param <D>
 * 		the type of data to present
 * @param <S>
 * 		the type of data source to present from
 */
public class RecyclerViewBodyPresenter<
		D extends LibraryItem,
		S extends ListDataSource<D>>
		extends
		DirectBodyPresenter<D, S, RecyclerViewBody> {
	/**
	 * Binds title data to the view.
	 */
	private TitleBinder titleDataBinder;

	/**
	 * Binds subtitle data to the view.
	 */
	private SubtitleBinder subtitleDataBinder;

	/**
	 * Binds artwork data to the view.
	 */
	private ArtworkBinder artworkDataBinder;

	/**
	 * Constructs a new SmallHeaderPresenter.
	 *
	 * @param titleDataBinder
	 * 		binds titles to the UI, not null
	 * @param subtitleDataBinder
	 * 		bind subtitle to the UI, not null
	 * @param artworkDataBinder
	 * 		binds artwork to the UI, not null
	 * @throws IllegalArgumentException
	 * 		if {@code titleDataBinder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code subtitleDataBinder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code artworkDataBinder} is null
	 */
	public RecyclerViewBodyPresenter(final TitleBinder titleDataBinder, final SubtitleBinder
			subtitleDataBinder, final ArtworkBinder artworkDataBinder) {
		super();

		this.titleDataBinder = NullChecker.checkNotNull(titleDataBinder,
				"titleDataBinder cannot be null");
		this.subtitleDataBinder = NullChecker.checkNotNull(subtitleDataBinder,
				"subtitleDataBinder cannot be null");
		this.artworkDataBinder = NullChecker.checkNotNull(artworkDataBinder,
				"artworkDataBinder cannot be null");
	}

	@Override
	public void setView(RecyclerViewBody view) {
		super.setView(view);

		if (view != null) {
			view.setTitleDataBinder(titleDataBinder);
			view.setSubtitleDataBinder(subtitleDataBinder);
			view.setArtworkDataBinder(artworkDataBinder);
		}
	}


	@Override
	public void onDataModified(BaseDataSource<List<D>> source, List<D> data) {
		// If the old data is not removed from the cache, the data binders will not update the UI
		titleDataBinder.getCache().clearTitles();
		subtitleDataBinder.getCache().clearSubtitles();
		artworkDataBinder.getCache().clearArtwork();

		super.onDataModified(source, data);
	}

	@Override
	public void onItemModified(ListDataSource<D> source, D modified, int index) {
		// If the old data is not removed from the cache, the data binders will not update the UI
		titleDataBinder.getCache().removeTitle(modified);
		subtitleDataBinder.getCache().removeSubtitle(modified);
		artworkDataBinder.getCache().removeArtwork(modified);

		super.onItemModified(source, modified, index);
	}

	@Override
	public void onItemSelected(final BodyContract.View hostView, final LibraryItem item) {
		// Default implementation does nothing
	}

	@Override
	public void onContextualMenuItemSelected(final BodyContract.View hostView, final LibraryItem
			libraryItem, final MenuItem menuItem) {
		// Default implementation does nothing
	}

	/**
	 * @return the TitleBinder used to bind titles to the UI, not null
	 */
	public final TitleBinder getTitleDataBinder() {
		return titleDataBinder;
	}

	/**
	 * @return the SubtitleBinder used to bind subtitles to the UI, not null
	 */
	public final SubtitleBinder getSubtitleDataBinder() {
		return subtitleDataBinder;
	}

	/**
	 * @return the ArtworkBinder used to bind artwork to the UI, not null
	 */
	public final ArtworkBinder getArtworkDataBinder() {
		return artworkDataBinder;
	}
}