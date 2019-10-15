package com.example.android.mtg_mania.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.mtg_mania.R;

import java.util.ArrayList;

/**
 * An {@link PostsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link Posts} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class PostsAdapter extends ArrayAdapter<Posts> {

    /**
     * Constructs a new {@link PostsAdapter}.
     *
     * @param context of the app
     * @param posts is the list of news, which is the data source of the adapter
     */
    public PostsAdapter(Context context, int resource, ArrayList<Posts> posts) {
        super(context, resource, posts);
    }

    /**
     * Returns a list item view that displays information about the post at the given position
     * in the list of posts.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_post, parent, false);
        }

        // Find the post at the given position in the list of posts
        Posts currentPost = getItem(position);

        if( currentPost.getUrl().equals("error")){
            TextView contentView = (TextView) listItemView.findViewById(R.id.content);

            contentView.setText(currentPost.getContent());

            return listItemView;
        }
        //Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        titleView.setText(currentPost.getTitle());

        //Find the TextView with view ID author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);

        String byAuthor = "By " + currentPost.getAuthor();
        authorView.setText(byAuthor);

        //Find the TextView with view ID comments number
        TextView commentsNumberView = (TextView) listItemView.findViewById(R.id.commentsNumber);
        String commentsNumberString = currentPost.getCommentsNumber() + " Comments";

        commentsNumberView.setText(commentsNumberString);

        //Find the TextView with view ID content
        TextView contentView = (TextView) listItemView.findViewById(R.id.content);

        contentView.setText(currentPost.getContent());

        return listItemView;
    }
}