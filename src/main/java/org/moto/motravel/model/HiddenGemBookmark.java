package org.moto.motravel.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "hidden_gem_bookmarks")
@IdClass(HiddenGemBookmarkId.class)
public class HiddenGemBookmark {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "hidden_gem_id")
    private Long hiddenGemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hidden_gem_id", insertable = false, updatable = false)
    private HiddenGem hiddenGem;

    @CreationTimestamp
    @Column(name = "bookmarked_at", nullable = false, updatable = false)
    private LocalDateTime bookmarkedAt;

    // Constructors
    public HiddenGemBookmark() {}

    public HiddenGemBookmark(Long userId, Long hiddenGemId) {
        this.userId = userId;
        this.hiddenGemId = hiddenGemId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHiddenGemId() {
        return hiddenGemId;
    }

    public void setHiddenGemId(Long hiddenGemId) {
        this.hiddenGemId = hiddenGemId;
    }

    public HiddenGem getHiddenGem() {
        return hiddenGem;
    }

    public void setHiddenGem(HiddenGem hiddenGem) {
        this.hiddenGem = hiddenGem;
    }

    public LocalDateTime getBookmarkedAt() {
        return bookmarkedAt;
    }

    public void setBookmarkedAt(LocalDateTime bookmarkedAt) {
        this.bookmarkedAt = bookmarkedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HiddenGemBookmark)) return false;
        HiddenGemBookmark that = (HiddenGemBookmark) o;
        return userId != null && userId.equals(that.userId) &&
               hiddenGemId != null && hiddenGemId.equals(that.hiddenGemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "HiddenGemBookmark{" +
                "userId=" + userId +
                ", hiddenGemId=" + hiddenGemId +
                ", bookmarkedAt=" + bookmarkedAt +
                '}';
    }
}
