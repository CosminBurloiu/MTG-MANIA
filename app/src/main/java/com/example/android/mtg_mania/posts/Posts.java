package com.example.android.mtg_mania.posts;

public class Posts {

    private String Title;

    private String Author;

    private String Url;

    private String Content;

    private Integer CommentsNumber;

    /**
     * Constructs a new {@link Posts} object.
     *
     * @param title  The title of the post.
     * @param author The author of the post.
     * @param url    The link where you can find that specific post.
     * @param content The content of that post.
     * @param commentsNumber  The number of comments on that specific post.
     */
    public Posts(String title, String author, String url, String content, Integer commentsNumber ) {
        Title = title;
        Author = author;
        Url = url;
        Content = content;
        CommentsNumber =  commentsNumber;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public Integer getCommentsNumber() {
        return CommentsNumber;
    }

    public void setCommentsNumber(Integer CommentsNumber) {
        this.CommentsNumber = CommentsNumber;
    }
}
