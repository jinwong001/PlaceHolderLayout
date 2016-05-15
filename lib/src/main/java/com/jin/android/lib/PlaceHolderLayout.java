package com.jin.android.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;


/**
 * 占位布局。
 * 在ProgressActivity上基础上改写，详情见
 * <ul>
 * <li>ProgressActivity <a href="https://github.com/chrisbanes/PhotoView">ProgressActivity</a></li>
 * </ul>
 */
public class PlaceHolderLayout extends RelativeLayout {
    /**
     * empty view tag，see{@link View#setTag(Object)}
     */
    private static final String TAG_EMPTY = PlaceHolderLayout.class.getSimpleName() + ".EMPTY";
    /**
     * error view tag，see{@link View#setTag(Object)}
     */
    private static final String TAG_ERROR = PlaceHolderLayout.class.getSimpleName() + ".ERROR";

    @StringDef({TYPE_CONTENT, TYPE_EMPTY, TYPE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Type {

    }

    private static final String TYPE_CONTENT = "type_content";
    private static final String TYPE_EMPTY = "type_empty";
    private static final String TYPE_ERROR = "type_error";

    private LayoutParams layoutParams;
    private Drawable currentBackground;

    private List<View> contentViews = new ArrayList<>();

    private LinearLayout emptyViewLayout;
    private AppCompatImageView emptyStateImageView;
    private AppCompatTextView emptyStateTitleTextView;
    private AppCompatTextView emptyStateContentTextView;

    private LinearLayout errorViewLayout;
    private AppCompatImageView errorStateImageView;
    private AppCompatTextView errorStateTitleTextView;
    private AppCompatButton errorStateButton;
    private AppCompatButton emptyStateButton;

    private int emptyStateImageWidth;
    private int emptyStateImageHeight;
    private int emptyStateTitleTextSize;
    private int emptyStateContentTextSize;
    private int emptyStateTitleTextColor;
    private int emptyStateContentTextColor;
    private int emptyStateBackgroundColor;

    private int errorStateImageWidth;
    private int errorStateImageHeight;
    private int errorStateTitleTextSize;
    private int errorStateContentTextSize;
    private int errorStateTitleTextColor;
    private int errorStateContentTextColor;
    private int errorStateButtonTextColor;
    private int emptyStateButtonTextColor;
    private int errorStateBackgroundColor;

    private PlaceholderContent placeholderContent;
    private PlaceholderEmpty placeholderEmpty;
    private PlaceholderError placeholderError;

    public PlaceHolderLayout(Context context) {
        super(context);
    }

    public PlaceHolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PlaceHolderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyLayout);

        //Empty state attrs
        emptyStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_emptyImageWidth, -1);

        emptyStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_emptyImageHeight, -1);

        emptyStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_emptyTitleTextSize, -1);

        emptyStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_emptyContentTextSize, -1);

        emptyStateTitleTextColor =
                typedArray.getColor(R.styleable.EmptyLayout_emptyTitleTextColor, -1);

        emptyStateContentTextColor =
                typedArray.getColor(R.styleable.EmptyLayout_emptyContentTextColor, -1);

        emptyStateBackgroundColor =
                typedArray.getColor(R.styleable.EmptyLayout_emptyBackgroundColor, -1);

        //Error state attrs
        errorStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_errorImageWidth, -1);

        errorStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_errorImageHeight, -1);

        errorStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_errorTitleTextSize, -1);

        errorStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.EmptyLayout_errorContentTextSize, -1);

        errorStateTitleTextColor =
                typedArray.getColor(R.styleable.EmptyLayout_errorTitleTextColor, -1);

        errorStateContentTextColor =
                typedArray.getColor(R.styleable.EmptyLayout_errorContentTextColor, -1);

        errorStateButtonTextColor =
                typedArray.getColor(R.styleable.EmptyLayout_errorButtonTextColor, -1);
        emptyStateButtonTextColor =
                typedArray.getColor(R.styleable.EmptyLayout_emptyButtonTextColor, -1);

        errorStateBackgroundColor =
                typedArray.getColor(R.styleable.EmptyLayout_errorBackgroundColor, -1);
        typedArray.recycle();

        currentBackground = this.getBackground();
    }

    @CallSuper
    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (!TAG_EMPTY.equals(child.getTag()) && !TAG_ERROR.equals(child.getTag())) {
            contentViews.add(child);
        }
    }

    @CallSuper
    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (!TAG_EMPTY.equals(child.getTag()) && !TAG_ERROR.equals(child.getTag())) {
            contentViews.remove(child);
        }
    }

    public void setPlaceholderContent(PlaceholderContent placeholderContent) {
        this.placeholderContent = placeholderContent;
    }

    public void setPlaceholderEmpty(PlaceholderEmpty placeholderEmpty) {
        this.placeholderEmpty = placeholderEmpty;
    }

    public void setPlaceholderError(PlaceholderError placeholderError) {
        this.placeholderError = placeholderError;
    }

    /**
     * Hide all other states and show content
     */
    public void showContent() {
        int[] skipIds = null;
        if (placeholderContent != null) {
            skipIds = placeholderContent.skipIds;
        }
        show(TYPE_CONTENT, null, null, null, null, null, null, skipIds);
    }

    /**
     * Show empty view when there are not data to show
     */
    public void showEmpty() {
        Drawable emptyImageDrawable = null;
        CharSequence emptyTextTitle = null;
        CharSequence emptyTextContent = null;
        OnClickListener contentListener = null;
        CharSequence emptyButtonText = null;
        OnClickListener buttonListener = null;
        int[] skipIds = null;
        if (placeholderEmpty != null) {
            emptyImageDrawable = placeholderEmpty.drawable;
            emptyTextTitle = placeholderEmpty.title;
            emptyTextContent = placeholderEmpty.content;
            contentListener = placeholderEmpty.contentListener;
            emptyButtonText = placeholderEmpty.buttonText;
            buttonListener = placeholderEmpty.buttonListener;
            skipIds = placeholderEmpty.skipIds;
        }
        show(TYPE_EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, contentListener,
                emptyButtonText, buttonListener, skipIds);
    }


    public void showError() {
        Drawable errorImageDrawable = null;
        CharSequence errorTextTitle = null;
        CharSequence errorButtonText = null;
        OnClickListener buttonListener = null;
        int[] skipIds = null;
        if (placeholderError != null) {
            errorImageDrawable = placeholderError.drawable;
            errorTextTitle = placeholderError.title;
            errorButtonText = placeholderError.buttonText;
            buttonListener = placeholderError.buttonListener;
            skipIds = placeholderError.skipIds;
        }
        show(TYPE_ERROR, errorImageDrawable, errorTextTitle, null, null, errorButtonText, buttonListener, skipIds);
    }

    private void show(@Type String type, Drawable drawable, CharSequence errorText,
                      CharSequence errorTextContent, OnClickListener contentListener,
                      CharSequence errorButtonText, OnClickListener buttonListener,
                      int... skipIds) {
        switch (type) {
            case TYPE_CONTENT:
                //Hide all state views to display content
                hideEmptyView();
                hideErrorView();

                setContentVisibility(true, skipIds);
                break;
            case TYPE_EMPTY:
                hideErrorView();

                setEmptyView();
                if (drawable != null) emptyStateImageView.setImageDrawable(drawable);
                if (!TextUtils.isEmpty(errorText)) emptyStateTitleTextView.setText(errorText);
                if (!TextUtils.isEmpty(errorTextContent))
                    emptyStateContentTextView.setText(errorTextContent);
                emptyStateContentTextView.setVisibility(TextUtils.isEmpty(errorTextContent) ? View.GONE : View.VISIBLE);
                if (contentListener != null)
                    emptyStateContentTextView.setOnClickListener(contentListener);
                if (!TextUtils.isEmpty(errorButtonText)) emptyStateButton.setText(errorButtonText);
                emptyStateButton.setOnClickListener(buttonListener);
                emptyStateButton.setVisibility(buttonListener != null ? View.VISIBLE : View.GONE);
                setContentVisibility(false, skipIds);
                break;
            case TYPE_ERROR:
                hideEmptyView();

                setErrorView();
                if (drawable != null) errorStateImageView.setImageDrawable(drawable);
                if (!TextUtils.isEmpty(errorText)) errorStateTitleTextView.setText(errorText);
                if (!TextUtils.isEmpty(errorButtonText)) errorStateButton.setText(errorButtonText);
                errorStateButton.setOnClickListener(buttonListener);
                errorStateButton.setVisibility(buttonListener != null ? View.VISIBLE : View.GONE);
                setContentVisibility(false, skipIds);
                break;
        }
    }

    private void setEmptyView() {
        if (emptyViewLayout == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            emptyViewLayout = (LinearLayout) inflater.inflate(R.layout.placeholder_empty_view, null);
            emptyViewLayout.setTag(TAG_EMPTY);

            emptyStateImageView = (AppCompatImageView) emptyViewLayout.findViewById(R.id.placeholder_empty_img);
            emptyStateTitleTextView = (AppCompatTextView) emptyViewLayout.findViewById(R.id.placeholder_empty_title);
            emptyStateContentTextView = (AppCompatTextView) emptyViewLayout.findViewById(R.id.placeholder_empty_content);
            emptyStateButton = (AppCompatButton) emptyViewLayout.findViewById(R.id.placeholder_empty_btn);

            //Set empty state image width and height
            if (emptyStateImageWidth != -1)
                emptyStateImageView.getLayoutParams().width = emptyStateImageWidth;
            if (emptyStateImageHeight != -1)
                emptyStateImageView.getLayoutParams().height = emptyStateImageHeight;
            emptyStateImageView.requestLayout();

            if (emptyStateTitleTextSize != -1)
                emptyStateTitleTextView.setTextSize(emptyStateTitleTextSize);
            if (emptyStateContentTextSize != -1)
                emptyStateContentTextView.setTextSize(emptyStateContentTextSize);
            if (emptyStateTitleTextColor != -1)
                emptyStateTitleTextView.setTextColor(emptyStateTitleTextColor);
            if (emptyStateContentTextColor != -1)
                emptyStateContentTextView.setTextColor(emptyStateContentTextColor);
            if (errorStateButtonTextColor != -1)
                emptyStateButton.setTextColor(emptyStateButtonTextColor);
            if (emptyStateBackgroundColor != -1)
                this.setBackgroundColor(emptyStateBackgroundColor);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(emptyViewLayout, layoutParams);
        } else {
            emptyViewLayout.setVisibility(VISIBLE);
        }
    }

    private void setErrorView() {
        if (errorViewLayout == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            errorViewLayout = (LinearLayout) inflater.inflate(R.layout.placeholder_error_view, null);
            errorViewLayout.setTag(TAG_ERROR);

            errorStateImageView = (AppCompatImageView) errorViewLayout.findViewById(R.id.placeholder_error_img);
            errorStateTitleTextView = (AppCompatTextView) errorViewLayout.findViewById(R.id.placeholder_error_title);
            errorStateButton = (AppCompatButton) errorViewLayout.findViewById(R.id.placeholder_error_action);

            //Set error state image width and height
            if (errorStateImageWidth != -1)
                errorStateImageView.getLayoutParams().width = errorStateImageWidth;
            if (errorStateImageHeight != -1)
                errorStateImageView.getLayoutParams().height = errorStateImageHeight;
            errorStateImageView.requestLayout();

            if (errorStateTitleTextSize != -1)
                errorStateTitleTextView.setTextSize(errorStateTitleTextSize);
            if (errorStateTitleTextColor != -1)
                errorStateTitleTextView.setTextColor(errorStateTitleTextColor);
            if (errorStateButtonTextColor != -1)
                errorStateButton.setTextColor(errorStateButtonTextColor);
            if (errorStateBackgroundColor != -1) this.setBackgroundColor(errorStateBackgroundColor);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(errorViewLayout, layoutParams);
        } else {
            errorViewLayout.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, int... skipIds) {
        for (View v : contentViews) {
            if (skipIds != null) {
                for (int id : skipIds) {
                    if (id != v.getId()) {
                        v.setVisibility(visible ? View.VISIBLE : View.GONE);
                    }
                }
            } else {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }

    }

    private void hideEmptyView() {
        if (emptyViewLayout != null) {
            emptyViewLayout.setVisibility(GONE);

            if (emptyStateBackgroundColor != -1) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    this.setBackground(currentBackground);
                } else {
                    //noinspection deprecation
                    this.setBackgroundDrawable(currentBackground);
                }
            }
        }
    }

    private void hideErrorView() {
        if (errorViewLayout != null) {
            errorViewLayout.setVisibility(GONE);

            if (errorStateBackgroundColor != -1) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    this.setBackground(currentBackground);
                } else {
                    //noinspection deprecation
                    this.setBackgroundDrawable(currentBackground);
                }
            }
        }
    }

    public static class PlaceholderContent {
        /**
         * Ids of views not to show
         */
        private final int[] skipIds;

        /**
         * @param skipIds Ids of views not to show
         */
        private PlaceholderContent(int... skipIds) {
            this.skipIds = skipIds;
        }

        public static class Builder {
            private int[] skipIds;

            public Builder() {
            }

            public Builder setSkipIds(int[] skipIds) {
                this.skipIds = skipIds;
                return this;
            }

            public PlaceholderContent build() {
                return new PlaceholderContent(skipIds);
            }
        }
    }

    public static class PlaceholderEmpty {
        private final Drawable drawable;
        private final CharSequence title;
        private final CharSequence content;
        private final OnClickListener contentListener;
        private final CharSequence buttonText;
        private final OnClickListener buttonListener;
        /**
         * Ids of views not to show
         */
        private int[] skipIds;

        /**
         * Show empty view when there are not data to show
         *
         * @param drawable        Drawable to show
         * @param title           Title of the empty view to show
         * @param content         Content of the empty view to show
         * @param contentListener Listener of the content view button
         * @param buttonText      Text on the error view button to show
         * @param buttonListener  Listener of the empty view button
         * @param skipIds         Ids of views not to show
         */
        private PlaceholderEmpty(Drawable drawable, CharSequence title,
                                 CharSequence content, OnClickListener contentListener,
                                 CharSequence buttonText, OnClickListener buttonListener,
                                 int... skipIds) {
            this.drawable = drawable;
            this.title = title;
            this.content = content;
            this.contentListener = contentListener;
            this.buttonText = buttonText;
            this.buttonListener = buttonListener;
            this.skipIds = skipIds;
        }

        public static class Builder {
            private Drawable drawable;
            private CharSequence title;
            private CharSequence content;
            private OnClickListener contentListener;
            private CharSequence buttonText;
            private OnClickListener buttonListener;
            private int[] skipIds;

            public Builder() {
            }

            /**
             * @param drawable Drawable to show
             * @return
             */
            public Builder setDrawable(Drawable drawable) {
                this.drawable = drawable;
                return this;
            }

            /**
             * @param title Title of the empty view to show
             * @return
             */
            public Builder setTitle(CharSequence title) {
                this.title = title;
                return this;
            }

            /**
             * @param content Content of the empty view to show
             * @return
             */
            public Builder setContent(CharSequence content) {
                this.content = content;
                return this;
            }

            /**
             * @param contentListener Listener of the content view button
             * @return
             */
            public Builder setContentListener(OnClickListener contentListener) {
                this.contentListener = contentListener;
                return this;
            }

            /**
             * @param buttonText Text on the error view button to show
             * @return
             */
            public Builder setButtonText(CharSequence buttonText) {
                this.buttonText = buttonText;
                return this;
            }

            /**
             * @param buttonListener Listener of the empty view button
             * @return
             */
            public Builder setButtonListener(OnClickListener buttonListener) {
                this.buttonListener = buttonListener;
                return this;
            }

            /**
             * @param skipIds Ids of views not to show
             * @return
             */
            public Builder setSkipIds(int... skipIds) {
                this.skipIds = skipIds;
                return this;
            }

            public PlaceholderEmpty build() {
                return new PlaceholderEmpty(drawable, title, content, contentListener, buttonText, buttonListener, skipIds);
            }
        }
    }

    public static class PlaceholderError {
        private final Drawable drawable;
        private final CharSequence title;
        private final CharSequence buttonText;
        private final OnClickListener buttonListener;
        /**
         * Ids of views not to show
         */
        private int[] skipIds;

        /**
         * error view with a button when something goes wrong and prompting the user to try again
         *
         * @param drawable       Drawable to show
         * @param title          Title of the error view to show
         * @param buttonText     Text on the error view button to show
         * @param buttonListener Listener of the error view button
         * @param skipIds        Ids of views not to show
         */
        private PlaceholderError(Drawable drawable, CharSequence title,
                                 CharSequence buttonText, OnClickListener buttonListener,
                                 int... skipIds) {
            this.drawable = drawable;
            this.title = title;
            this.buttonText = buttonText;
            this.buttonListener = buttonListener;
            this.skipIds = skipIds;
        }

        public static class Builder {
            private Drawable drawable;
            private CharSequence title;
            private CharSequence buttonText;
            private OnClickListener buttonListener;
            private int[] skipIds;

            public Builder() {
            }

            /**
             * @param drawable Drawable to show
             * @return
             */
            public Builder setDrawable(Drawable drawable) {
                this.drawable = drawable;
                return this;
            }

            /**
             * @param title Title of the error view to show
             * @return
             */
            public Builder setTitle(CharSequence title) {
                this.title = title;
                return this;
            }

            /**
             * @param buttonText Text on the error view button to show
             * @return
             */
            public Builder setButtonText(CharSequence buttonText) {
                this.buttonText = buttonText;
                return this;
            }

            /**
             * @param buttonListener Listener of the error view button
             * @return
             */
            public Builder setButtonListener(OnClickListener buttonListener) {
                this.buttonListener = buttonListener;
                return this;
            }

            /**
             * @param skipIds Ids of views not to show
             * @return
             */
            public Builder setSkipIds(int... skipIds) {
                this.skipIds = skipIds;
                return this;
            }

            public PlaceholderError build() {
                return new PlaceholderError(drawable, title, buttonText, buttonListener, skipIds);
            }
        }
    }
}
