package org.moto.motravel.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "hidden_gem_bookmarks")
public class HiddenGemBookmark {
    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String hiddenGemId;

    @CreatedDate
    private Instant bookmarkedAt;

    public HiddenGemBookmark() {}

    public HiddenGemBookmark(String userId, String hiddenGemId) {
        this.userId = userId;
        this.hiddenGemId = hiddenGemId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getHiddenGemId() { return hiddenGemId; }
    public void setHiddenGemId(String hiddenGemId) { this.hiddenGemId = hiddenGemId; }
    public Instant getBookmarkedAt() { return bookmarkedAt; }
    public void setBookmarkedAt(Instant bookmarkedAt) { this.bookmarkedAt = bookmarkedAt; }
}
